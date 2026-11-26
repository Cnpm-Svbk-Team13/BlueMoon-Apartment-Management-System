-- BlueMoon BFMS - Sprint 1 bootstrap, PostgreSQL 16/17.
-- US-SEC-01/02, NFR-02 -> DB-01, BE-01; US-001, RAW-001/002
-- -> BE-02, DB-02, BE-03; verification: QA-05.
-- ERD: docs/02_design_and_architecture/erd_sprint_1_proposal.md.
-- EMPTY DATABASE ONLY. This is not a migration. No DROP/TRUNCATE.
-- No IF NOT EXISTS: fail rather than retain an incompatible existing table.
-- After successful bootstrap, seed_data.sql supplies synthetic Sprint 1 fixtures.
-- Run this bootstrap directly; do not append migrations from another structure.
-- Run with psql --set=ON_ERROR_STOP=1 --file=database/schema.sql.

BEGIN;
-- 1. Shared personal identity. Children may have no national ID or phone.
CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL CHECK (
        full_name = btrim(full_name) AND full_name <> ''
    ),
    national_id VARCHAR(12),
    phone VARCHAR(20),
    email VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(10),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_persons_national_id UNIQUE (national_id),
    CONSTRAINT chk_persons_national_id CHECK (
        national_id IS NULL OR national_id ~ '^[0-9]{12}$'
    ),
    CONSTRAINT chk_persons_phone CHECK (phone IS NULL OR phone ~ '^[0-9]{10}$'),
    CONSTRAINT chk_persons_email CHECK (
        email IS NULL OR (
            email = lower(btrim(email))
            AND email <> ''
        )
    ),
    CONSTRAINT chk_persons_gender CHECK (
        gender IS NULL OR gender IN ('MALE', 'FEMALE', 'OTHER')
    )
);

-- 2. Accounts. BCrypt, login lock expiry and sessions belong to Service.
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE CHECK (
        username = lower(btrim(username)) AND username <> ''
    ),
    password_hash VARCHAR(255) NOT NULL CHECK (btrim(password_hash) <> ''),
    person_id BIGINT NOT NULL UNIQUE REFERENCES persons(id) ON DELETE RESTRICT,
    role VARCHAR(20) NOT NULL CHECK (
        role IN ('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_CASHIER', 'ROLE_RESIDENT')
    ),
    failed_attempts INTEGER NOT NULL DEFAULT 0 CHECK (failed_attempts >= 0),
    locked_until TIMESTAMPTZ,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. Usable area; type is independent of floor (RAW-001, decision 2026-11-04).
CREATE TABLE apartments (
    id BIGSERIAL PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL UNIQUE CHECK (
        room_number = upper(btrim(room_number)) AND room_number <> ''
    ),
    floor_number INTEGER NOT NULL CHECK (floor_number BETWEEN 1 AND 30),
    area_sqm DECIMAL(12, 2) NOT NULL CHECK (area_sqm > 0),
    apartment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'VACANT'
        CHECK (status IN ('VACANT', 'OCCUPIED')),
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_apartments_type CHECK (
        apartment_type IN ('KIOT', 'COMMERCIAL', 'RESIDENTIAL', 'PENTHOUSE')
    )
);

-- 4. Multiple historical households; at most one ACTIVE per apartment.
CREATE TABLE households (
    id BIGSERIAL PRIMARY KEY,
    apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT,
    head_person_id BIGINT NOT NULL REFERENCES persons(id) ON DELETE RESTRICT,
    registration_code VARCHAR(50) NOT NULL UNIQUE
        CHECK (
            registration_code = upper(btrim(registration_code))
            AND registration_code <> ''
        ),
    moved_in_date DATE NOT NULL DEFAULT CURRENT_DATE,
    moved_out_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        CHECK (status IN ('ACTIVE', 'MOVED_OUT', 'TRANSFERRED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_households_moved_out_date CHECK (
        (status = 'ACTIVE' AND moved_out_date IS NULL)
        OR (
            status IN ('MOVED_OUT', 'TRANSFERRED')
            AND (moved_out_date IS NULL OR moved_out_date >= moved_in_date)
        )
    )
);

CREATE UNIQUE INDEX uq_active_household_per_apt
    ON households(apartment_id) WHERE status = 'ACTIVE';

-- 5. Residence data, not duplicate identity data.
-- Current residence only. HEAD must agree with household head at transaction end.
CREATE TABLE residents (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL REFERENCES persons(id) ON DELETE RESTRICT,
    household_id BIGINT NOT NULL REFERENCES households(id) ON DELETE RESTRICT,
    relationship_with_head VARCHAR(50) NOT NULL
        CHECK (relationship_with_head IN ('HEAD', 'SPOUSE', 'CHILD', 'PARENT', 'TENANT', 'OTHER')),
    residence_status VARCHAR(20) NOT NULL DEFAULT 'THUONG_TRU'
        CHECK (residence_status IN ('THUONG_TRU', 'TAM_TRU', 'TAM_VANG')),
    start_date DATE,
    end_date DATE,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_residents_person UNIQUE (person_id),
    CONSTRAINT chk_residents_dates CHECK (
        (
            residence_status = 'THUONG_TRU'
            AND start_date IS NULL
            AND end_date IS NULL
        )
        OR (
            residence_status IN ('TAM_TRU', 'TAM_VANG')
            AND start_date IS NOT NULL
            AND end_date IS NOT NULL
            AND end_date >= start_date
        )
    )
);

-- 6. Service writes snapshots and authenticated actor in the SAME transaction
-- as the head/profile change. A DB connection user is NOT a website user.
-- This immutable helper validates snapshot structure only. Service remains
-- responsible for proving that values are the real before/after profile.
CREATE FUNCTION is_valid_head_snapshot(snapshot JSONB)
RETURNS BOOLEAN
LANGUAGE sql
IMMUTABLE
STRICT
PARALLEL SAFE
AS $$
    SELECT jsonb_typeof(snapshot) = 'object'
        AND snapshot ?& ARRAY['full_name', 'national_id', 'phone', 'email']
        AND jsonb_typeof(snapshot->'full_name') = 'string'
        AND snapshot->>'full_name' = btrim(snapshot->>'full_name')
        AND snapshot->>'full_name' <> ''
        AND char_length(snapshot->>'full_name') <= 100
        AND jsonb_typeof(snapshot->'national_id') = 'string'
        AND snapshot->>'national_id' ~ '^[0-9]{12}$'
        AND jsonb_typeof(snapshot->'phone') = 'string'
        AND snapshot->>'phone' ~ '^[0-9]{10}$'
        AND (
            jsonb_typeof(snapshot->'email') = 'null'
            OR (
                jsonb_typeof(snapshot->'email') = 'string'
                AND snapshot->>'email' = lower(btrim(snapshot->>'email'))
                AND char_length(snapshot->>'email') <= 255
                AND snapshot->>'email' ~ '^[^[:space:]@]+@[^[:space:]@]+[.][^[:space:]@]+$'
            )
        );
$$;

CREATE TABLE household_head_history (
    id BIGSERIAL PRIMARY KEY,
    household_id BIGINT NOT NULL REFERENCES households(id) ON DELETE RESTRICT,
    previous_head_person_id BIGINT REFERENCES persons(id) ON DELETE RESTRICT,
    new_head_person_id BIGINT NOT NULL REFERENCES persons(id) ON DELETE RESTRICT,
    previous_snapshot JSONB,
    new_snapshot JSONB NOT NULL,
    changed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT chk_head_history_previous CHECK (
        (previous_head_person_id IS NULL AND previous_snapshot IS NULL)
        OR (previous_head_person_id IS NOT NULL AND previous_snapshot IS NOT NULL)
    ),
    CONSTRAINT chk_head_history_snapshots CHECK (
        is_valid_head_snapshot(new_snapshot)
        AND (
            previous_snapshot IS NULL
            OR is_valid_head_snapshot(previous_snapshot)
        )
    )
);

-- 7. Unknown usernames may produce an audit without user_id.
-- RESTRICT preserves known actors; deactivate accounts instead of deleting.
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
    action VARCHAR(50) NOT NULL CHECK (btrim(action) <> ''),
    entity_name VARCHAR(50) NOT NULL CHECK (btrim(entity_name) <> ''),
    entity_id BIGINT,
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. Index only where PK/UNIQUE does not already provide the needed prefix.
CREATE INDEX idx_apartments_floor ON apartments(floor_number);
CREATE INDEX idx_households_apartment ON households(apartment_id);
CREATE INDEX idx_households_head_person ON households(head_person_id);
CREATE INDEX idx_residents_household ON residents(household_id);
CREATE INDEX idx_head_history_household_time
    ON household_head_history(household_id, changed_at, id);
CREATE INDEX idx_head_history_previous_person ON household_head_history(previous_head_person_id);
CREATE INDEX idx_head_history_new_person ON household_head_history(new_head_person_id);
CREATE INDEX idx_head_history_actor ON household_head_history(changed_by_user_id);
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity_time
    ON audit_logs(entity_name, entity_id, created_at);

-- 9. Shared timestamp trigger. DEFAULT handles INSERT when fields are omitted;
-- UPDATE keeps the original creation time, even if a caller tries to change it.
-- CURRENT_TIMESTAMP is the transaction start time, not a wall-clock stopwatch.
CREATE FUNCTION set_row_updated_at()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.created_at := OLD.created_at;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_row_updated_at();
CREATE TRIGGER trg_persons_updated_at
    BEFORE UPDATE ON persons
    FOR EACH ROW EXECUTE FUNCTION set_row_updated_at();
CREATE TRIGGER trg_apartments_updated_at
    BEFORE UPDATE ON apartments
    FOR EACH ROW EXECUTE FUNCTION set_row_updated_at();
CREATE TRIGGER trg_households_updated_at
    BEFORE UPDATE ON households
    FOR EACH ROW EXECUTE FUNCTION set_row_updated_at();
CREATE TRIGGER trg_residents_updated_at
    BEFORE UPDATE ON residents
    FOR EACH ROW EXECUTE FUNCTION set_row_updated_at();

-- 10. Cross-table head validation (US-001 / DB-02).
-- Lock the person row: assigning a head must serialize with editing that
-- person's required contact data. A plain EXISTS check is insufficient here.
CREATE FUNCTION validate_household_head()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    head_record RECORD;
BEGIN
    SELECT national_id, phone INTO head_record
    FROM persons WHERE id = NEW.head_person_id
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Household head must reference an existing person'
            USING ERRCODE = '23503';
    END IF;
    IF head_record.national_id IS NULL OR head_record.phone IS NULL THEN
        RAISE EXCEPTION 'Household head requires national ID and phone'
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_households_validate_head
    BEFORE INSERT OR UPDATE OF head_person_id ON households
    FOR EACH ROW EXECUTE FUNCTION validate_household_head();

-- Guard the reverse direction too: a valid head cannot lose required fields.
-- All referenced households count, including historical/inactive households.
CREATE FUNCTION protect_household_head_profile()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF (NEW.national_id IS NULL OR NEW.phone IS NULL)
        AND EXISTS (SELECT 1 FROM households WHERE head_person_id = OLD.id) THEN
        RAISE EXCEPTION 'Cannot remove national ID or phone from a household head'
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_persons_protect_head_profile
    BEFORE UPDATE OF national_id, phone ON persons
    FOR EACH ROW EXECUTE FUNCTION protect_household_head_profile();

-- 11. History/audit are append-only, including when a statement affects no rows.
-- This is not tamper-proof against a database owner/superuser who disables triggers.
CREATE FUNCTION reject_history_mutation()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    RAISE EXCEPTION '% is append-only; % is not allowed', TG_TABLE_NAME, TG_OP
        USING ERRCODE = '23514';
END;
$$;

CREATE TRIGGER trg_head_history_append_only
    BEFORE UPDATE OR DELETE OR TRUNCATE ON household_head_history
    FOR EACH STATEMENT EXECUTE FUNCTION reject_history_mutation();
CREATE TRIGGER trg_audit_logs_append_only
    BEFORE UPDATE OR DELETE OR TRUNCATE ON audit_logs
    FOR EACH STATEMENT EXECUTE FUNCTION reject_history_mutation();

-- 12. Serialize cross-table invariants before row locks. READ COMMITTED is
-- required so VOLATILE trigger queries see commits after advisory-lock waits.
-- Deliberately coarse for Sprint 1: persons/users/households/residents writers
-- serialize for the transaction. This is not a claim of NFR throughput.
CREATE FUNCTION lock_identity_household_writes()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    IF current_setting('transaction_isolation') <> 'read committed' THEN
        RAISE EXCEPTION 'Identity/household writes require READ COMMITTED'
            USING ERRCODE = '25000';
    END IF;
    PERFORM pg_advisory_xact_lock(3180, 13);
    RETURN NULL;
END;
$$;
CREATE TRIGGER trg_persons_write_lock BEFORE INSERT OR UPDATE OR DELETE ON persons
    FOR EACH STATEMENT EXECUTE FUNCTION lock_identity_household_writes();
CREATE TRIGGER trg_users_write_lock BEFORE INSERT OR UPDATE OR DELETE ON users
    FOR EACH STATEMENT EXECUTE FUNCTION lock_identity_household_writes();
CREATE TRIGGER trg_households_write_lock BEFORE INSERT OR UPDATE OR DELETE ON households
    FOR EACH STATEMENT EXECUTE FUNCTION lock_identity_household_writes();
CREATE TRIGGER trg_residents_write_lock BEFORE INSERT OR UPDATE OR DELETE ON residents
    FOR EACH STATEMENT EXECUTE FUNCTION lock_identity_household_writes();

CREATE FUNCTION keep_household_apartment()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    IF NEW.apartment_id IS DISTINCT FROM OLD.apartment_id THEN
        RAISE EXCEPTION 'A household stays with its apartment; close it and create a new household'
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER trg_households_fixed_apartment BEFORE UPDATE OF apartment_id ON households
    FOR EACH ROW EXECUTE FUNCTION keep_household_apartment();

-- Check final state, allowing coordinated edits in either order. Account email
-- comparison is exact (same semantics as the former users.email UNIQUE).
-- Ordinary persons may share email; only account holders must have unique email.
CREATE FUNCTION validate_account_profiles()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM users u JOIN persons p ON p.id = u.person_id
        WHERE p.phone IS NULL OR p.email IS NULL
            OR p.email !~ '^[^[:space:]@]+@[^[:space:]@]+[.][^[:space:]@]+$'
    ) THEN
        RAISE EXCEPTION 'Account holder requires valid email and phone'
            USING ERRCODE = '23514';
    END IF;
    IF EXISTS (
        SELECT lower(btrim(p.email))
        FROM users u JOIN persons p ON p.id = u.person_id
        GROUP BY lower(btrim(p.email)) HAVING count(*) > 1
    ) THEN
        RAISE EXCEPTION 'Account holder email must be unique'
            USING ERRCODE = '23505';
    END IF;
    RETURN NULL;
END;
$$;
CREATE CONSTRAINT TRIGGER trg_users_account_profile AFTER INSERT OR UPDATE ON users
    DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION validate_account_profiles();
CREATE CONSTRAINT TRIGGER trg_persons_account_profile AFTER INSERT OR UPDATE ON persons
    DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION validate_account_profiles();

CREATE FUNCTION validate_resident_head()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM residents r JOIN households h ON h.id = r.household_id
        WHERE (r.relationship_with_head = 'HEAD') <> (r.person_id = h.head_person_id)
    ) THEN
        RAISE EXCEPTION 'HEAD relationship must agree with household head'
            USING ERRCODE = '23514';
    END IF;
    RETURN NULL;
END;
$$;
CREATE CONSTRAINT TRIGGER trg_residents_head AFTER INSERT OR UPDATE ON residents
    DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION validate_resident_head();
CREATE CONSTRAINT TRIGGER trg_households_head AFTER INSERT OR UPDATE ON households
    DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION validate_resident_head();

COMMIT;
