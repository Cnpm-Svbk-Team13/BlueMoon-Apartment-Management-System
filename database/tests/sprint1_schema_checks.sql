-- HISTORICAL: schema 2026-11-02 only. Current suite: verify_erd_revision.py.
-- Synthetic PostgreSQL checks: US-001 / DB-02 / QA-05,
-- US-SEC-01/02 / NFR-02 / DB-01. Run after schema.sql on an isolated DB.
-- All fixtures roll back. No live resident records or passwords.
BEGIN;

CREATE TEMP TABLE check_results (label TEXT NOT NULL);
CREATE FUNCTION pg_temp.expect_error(label TEXT, statement TEXT, expected_state TEXT)
RETURNS VOID LANGUAGE plpgsql AS $$
DECLARE actual_state TEXT;
BEGIN
    BEGIN
        EXECUTE statement;
    EXCEPTION WHEN OTHERS THEN
        GET STACKED DIAGNOSTICS actual_state = RETURNED_SQLSTATE;
    END;
    IF actual_state IS DISTINCT FROM expected_state THEN
        RAISE EXCEPTION '%: expected SQLSTATE %, got %', label, expected_state, actual_state;
    END IF;
    INSERT INTO check_results VALUES (label);
END;
$$;

INSERT INTO users (id, username, password_hash, full_name, email, phone, role)
VALUES (1, 'synthetic_actor', 'not-a-login-fixture', 'Synthetic Actor',
        'actor@example.test', '0900000001', 'ROLE_ADMIN');
INSERT INTO persons (id, full_name, national_id, phone, created_at)
VALUES (1, 'Synthetic Owner A', '000000000001', '0900000001', '2000-01-01Z'),
       (2, 'Synthetic Owner B', '000000000002', '0900000002', '2000-01-01Z'),
       (3, 'Synthetic Child A', NULL, NULL, '2000-01-01Z'),
       (4, 'Synthetic Child B', NULL, NULL, '2000-01-01Z');
INSERT INTO apartments (id, room_number, floor_number, area_sqm, apartment_type)
VALUES (1, 'SYN-601', 6, 75.50, 'RESIDENTIAL'),
       (2, 'SYN-602', 6, 85.00, 'RESIDENTIAL'),
       (3, 'SYN-K1', 1, 100, 'KIOT'),
       (4, 'SYN-C2', 2, 100, 'COMMERCIAL'),
       (5, 'SYN-P30', 30, 100, 'PENTHOUSE');
INSERT INTO households (id, apartment_id, head_person_id, registration_code)
VALUES (1, 1, 1, 'SYN-H1');
INSERT INTO residents (id, person_id, household_id, relationship_with_head)
VALUES (1, 3, 1, 'CON');

-- Explicit fixture IDs do not advance BIGSERIAL sequences automatically.
DO $$ DECLARE target TEXT; maximum BIGINT; BEGIN
    FOREACH target IN ARRAY ARRAY['users','persons','apartments','households','residents'] LOOP
        EXECUTE format('SELECT max(id) FROM %I', target) INTO maximum;
        PERFORM setval(pg_get_serial_sequence(target, 'id'), maximum);
    END LOOP;
END $$;

SELECT pg_temp.expect_error('CCCD uniqueness',
    $$INSERT INTO persons(full_name,national_id) VALUES ('Duplicate','000000000001')$$, '23505');
SELECT pg_temp.expect_error('CCCD format',
    $$INSERT INTO persons(full_name,national_id) VALUES ('Bad','123')$$, '23514');
SELECT pg_temp.expect_error('Phone format',
    $$INSERT INTO persons(full_name,phone) VALUES ('Bad','123')$$, '23514');
SELECT pg_temp.expect_error('Blank person name',
    $$INSERT INTO persons(full_name) VALUES ('   ')$$, '23514');
SELECT pg_temp.expect_error('Floor/type mismatch',
    $$UPDATE apartments SET apartment_type='PENTHOUSE' WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('Floor boundary',
    $$UPDATE apartments SET floor_number=31 WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('Positive area',
    $$UPDATE apartments SET area_sqm=0 WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('Unique room',
    $$UPDATE apartments SET room_number='SYN-601' WHERE id=2$$, '23505');
SELECT pg_temp.expect_error('One active household',
    $$INSERT INTO households(apartment_id,head_person_id,registration_code)
      VALUES(1,2,'SYN-DUP')$$, '23505');
SELECT pg_temp.expect_error('Owner requires ID and phone',
    $$INSERT INTO households(apartment_id,head_person_id,registration_code)
      VALUES(2,3,'SYN-NO-ID')$$, '23514');
SELECT pg_temp.expect_error('Missing owner',
    $$UPDATE households SET head_person_id=999 WHERE id=1$$, '23503');
SELECT pg_temp.expect_error('Owner cannot lose phone',
    $$UPDATE persons SET phone=NULL WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('Owner cannot lose national ID',
    $$UPDATE persons SET national_id=NULL WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('One residence per person',
    $$INSERT INTO residents(person_id,household_id,relationship_with_head)
      VALUES(3,1,'CON')$$, '23505');
SELECT pg_temp.expect_error('Residence requires household',
    $$UPDATE residents SET household_id=NULL WHERE id=1$$, '23502');
SELECT pg_temp.expect_error('Temporary residence requires dates',
    $$UPDATE residents SET residence_status='TAM_TRU' WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('Absent residence requires dates',
    $$UPDATE residents SET residence_status='TAM_VANG' WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('Residence date order',
    $$UPDATE residents SET residence_status='TAM_TRU',start_date='2026-02-02',
      end_date='2026-02-01' WHERE id=1$$, '23514');
UPDATE residents SET residence_status='TAM_TRU', start_date='2026-02-01',
    end_date='2026-02-01' WHERE id=1;
SELECT pg_temp.expect_error('Referenced person protected',
    $$DELETE FROM persons WHERE id=3$$, '23503');
SELECT pg_temp.expect_error('Referenced household protected',
    $$DELETE FROM households WHERE id=1$$, '23503');
SELECT pg_temp.expect_error('Account cannot have NULL active flag',
    $$UPDATE users SET is_active=NULL WHERE id=1$$, '23502');
SELECT pg_temp.expect_error('Negative login failures',
    $$UPDATE users SET failed_attempts=-1 WHERE id=1$$, '23514');

INSERT INTO apartment_owner_history
    (id,apartment_id,household_id,new_owner_id,event_type,new_snapshot,changed_by_user_id)
SELECT 1,1,1,1,'ASSIGNED',jsonb_build_object('full_name',full_name,
    'national_id',national_id,'phone',phone,'email',email),1
FROM persons WHERE id=1;
SELECT setval(pg_get_serial_sequence('apartment_owner_history','id'), 1);
SELECT pg_temp.expect_error('History household/apartment match',
    $$INSERT INTO apartment_owner_history
      (apartment_id,household_id,new_owner_id,event_type,new_snapshot,changed_by_user_id)
      SELECT 2,1,1,'ASSIGNED',new_snapshot,1 FROM apartment_owner_history WHERE id=1$$, '23503');
SELECT pg_temp.expect_error('Replacement needs previous owner',
    $$INSERT INTO apartment_owner_history
      (apartment_id,household_id,new_owner_id,event_type,new_snapshot,changed_by_user_id)
      SELECT 1,1,2,'REPLACED',new_snapshot,1 FROM apartment_owner_history WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('History snapshot requires keys',
    $$INSERT INTO apartment_owner_history
      (apartment_id,household_id,new_owner_id,event_type,new_snapshot,changed_by_user_id)
      VALUES(1,1,1,'ASSIGNED','{}',1)$$, '23514');
SELECT pg_temp.expect_error('History actor required',
    $$INSERT INTO apartment_owner_history
      (apartment_id,household_id,new_owner_id,event_type,new_snapshot,changed_by_user_id)
      SELECT 1,1,1,'ASSIGNED',new_snapshot,NULL FROM apartment_owner_history WHERE id=1$$, '23502');
SELECT pg_temp.expect_error('History immutable',
    $$UPDATE apartment_owner_history SET event_type='REPLACED' WHERE id=1$$, '23514');
SELECT pg_temp.expect_error('History cannot be deleted',
    $$DELETE FROM apartment_owner_history$$, '23514');
SELECT pg_temp.expect_error('History cannot be truncated',
    $$TRUNCATE apartment_owner_history$$, '23514');
INSERT INTO audit_logs(action,entity_name) VALUES('LOGIN_FAILURE','users');
SELECT pg_temp.expect_error('Audit immutable',
    $$UPDATE audit_logs SET action='changed'$$, '23514');
SELECT pg_temp.expect_error('Audit cannot be deleted',
    $$DELETE FROM audit_logs$$, '23514');
SELECT pg_temp.expect_error('Audit cannot be truncated',
    $$TRUNCATE audit_logs$$, '23514');

-- Verify timestamps in every mutable table, not just one attached trigger.
DO $$
DECLARE target TEXT; old_created TIMESTAMPTZ; new_created TIMESTAMPTZ; updated TIMESTAMPTZ;
BEGIN
    FOREACH target IN ARRAY ARRAY['users','persons','apartments','households','residents'] LOOP
        EXECUTE format('SELECT created_at FROM %I WHERE id=1', target) INTO old_created;
        EXECUTE format('UPDATE %I SET created_at=$1,updated_at=$1 WHERE id=1
            RETURNING created_at,updated_at',target)
            INTO new_created,updated USING '1999-01-01Z'::TIMESTAMPTZ;
        IF new_created IS DISTINCT FROM old_created OR updated IS DISTINCT FROM CURRENT_TIMESTAMP THEN
            RAISE EXCEPTION 'Timestamp trigger failed for %',target;
        END IF;
        INSERT INTO check_results VALUES ('Timestamp ' || target);
    END LOOP;
    IF (SELECT new_snapshot->>'phone' FROM apartment_owner_history WHERE id=1) <> '0900000001' THEN
        RAISE EXCEPTION 'Unexpected initial snapshot';
    END IF;
END;
$$;
UPDATE persons SET phone='0900000099' WHERE id=1;
DO $$ BEGIN
    IF (SELECT new_snapshot->>'phone' FROM apartment_owner_history WHERE id=1) <> '0900000001' THEN
        RAISE EXCEPTION 'Snapshot changed with current profile';
    END IF;
END $$;
INSERT INTO check_results VALUES ('Snapshot preserved after profile edit');

SAVEPOINT before_owner_change;
UPDATE households SET head_person_id=2 WHERE id=1;
ROLLBACK TO SAVEPOINT before_owner_change;
DO $$ BEGIN
    IF (SELECT head_person_id FROM households WHERE id=1) <> 1 THEN
        RAISE EXCEPTION 'Rollback did not restore ownership';
    END IF;
END $$;
INSERT INTO check_results VALUES ('Ownership rollback');

SELECT count(*) AS passed_checks FROM check_results;
ROLLBACK;
