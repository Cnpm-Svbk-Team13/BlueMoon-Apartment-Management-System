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

-- 1. Accounts. BCrypt, login lock expiry and sessions belong to Service.
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE CHECK (btrim(username) <> ''),
    password_hash VARCHAR(255) NOT NULL CHECK (btrim(password_hash) <> ''),
    full_name VARCHAR(100) NOT NULL CHECK (btrim(full_name) <> ''),
    email VARCHAR(100) NOT NULL UNIQUE CHECK (btrim(email) <> ''),
    phone VARCHAR(20) NOT NULL CHECK (phone ~ '^[0-9]{10}$'),
    role VARCHAR(20) NOT NULL CHECK (
        role IN ('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_CASHIER', 'ROLE_RESIDENT')
    ),
    failed_attempts INTEGER NOT NULL DEFAULT 0 CHECK (failed_attempts >= 0),
    locked_until TIMESTAMPTZ,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Shared personal identity. Children may have no national ID or phone.
CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL CHECK (btrim(full_name) <> ''),
    national_id VARCHAR(12),
    phone VARCHAR(20),
    email VARCHAR(100),
    date_of_birth DATE,
    gender VARCHAR(10),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_persons_national_id UNIQUE (national_id),
    CONSTRAINT chk_persons_national_id CHECK (
        national_id IS NULL OR national_id ~ '^[0-9]{12}$'
    ),
    CONSTRAINT chk_persons_phone CHECK (phone IS NULL OR phone ~ '^[0-9]{10}$'),
    CONSTRAINT chk_persons_email CHECK (email IS NULL OR btrim(email) <> ''),
    CONSTRAINT chk_persons_gender CHECK (
        gender IS NULL OR gender IN ('MALE', 'FEMALE', 'OTHER')
    )
);

-- 3. Usable area and floor/type agree with the building layout.
CREATE TABLE apartments (
    id BIGSERIAL PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL UNIQUE CHECK (btrim(room_number) <> ''),
    floor_number INTEGER NOT NULL CHECK (floor_number BETWEEN 1 AND 30),
    area_sqm DECIMAL(12, 2) NOT NULL CHECK (area_sqm > 0),
    apartment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'VACANT'
        CHECK (status IN ('VACANT', 'OCCUPIED', 'RENOVATING')),
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_apartments_floor_type CHECK (
        (floor_number = 1 AND apartment_type = 'KIOT')
        OR (floor_number BETWEEN 2 AND 5 AND apartment_type = 'COMMERCIAL')
        OR (floor_number BETWEEN 6 AND 29 AND apartment_type = 'RESIDENTIAL')
        OR (floor_number = 30 AND apartment_type = 'PENTHOUSE')
    )
);

-- 4. Multiple historical households; at most one ACTIVE per apartment.
CREATE TABLE households (
    id BIGSERIAL PRIMARY KEY,
    apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT,
    head_person_id BIGINT NOT NULL REFERENCES persons(id) ON DELETE RESTRICT,
    registration_code VARCHAR(50) NOT NULL UNIQUE
        CHECK (btrim(registration_code) <> ''),
    moved_in_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        CHECK (status IN ('ACTIVE', 'MOVED_OUT', 'TRANSFERRED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Target for the composite history FK below.
    CONSTRAINT uq_households_id_apartment UNIQUE (id, apartment_id)
);

CREATE UNIQUE INDEX uq_active_household_per_apt
    ON households(apartment_id) WHERE status = 'ACTIVE';

-- 5. Residence data, not duplicate identity data.
-- Canonical relationship codes remain a team decision; reject blank values.
CREATE TABLE residents (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL REFERENCES persons(id) ON DELETE RESTRICT,
    household_id BIGINT NOT NULL REFERENCES households(id) ON DELETE RESTRICT,
    relationship_with_head VARCHAR(50) NOT NULL
        CHECK (btrim(relationship_with_head) <> ''),
    residence_status VARCHAR(20) NOT NULL DEFAULT 'THUONG_TRU'
        CHECK (residence_status IN ('THUONG_TRU', 'TAM_TRU', 'TAM_VANG')),
    start_date DATE,
    end_date DATE,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_residents_person UNIQUE (person_id),
    CONSTRAINT chk_residents_dates CHECK (
        residence_status = 'THUONG_TRU'
        OR (start_date IS NOT NULL AND end_date IS NOT NULL AND end_date >= start_date)
    )
);

-- 6. Service writes snapshots and authenticated actor in the SAME transaction
-- as the ownership/profile change. A DB connection user is NOT a website user.
CREATE TABLE apartment_owner_history (
    id BIGSERIAL PRIMARY KEY,
    apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT,
    household_id BIGINT NOT NULL,
    previous_owner_id BIGINT REFERENCES persons(id) ON DELETE RESTRICT,
    new_owner_id BIGINT NOT NULL REFERENCES persons(id) ON DELETE RESTRICT,
    event_type VARCHAR(20) NOT NULL,
    previous_snapshot JSONB,
    new_snapshot JSONB NOT NULL,
    changed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_owner_history_household_apartment
        FOREIGN KEY (household_id, apartment_id)
        REFERENCES households(id, apartment_id) ON DELETE RESTRICT,
    CONSTRAINT chk_owner_history_event CHECK (
        (event_type = 'ASSIGNED' AND previous_owner_id IS NULL
            AND previous_snapshot IS NULL)
        OR (event_type = 'REPLACED' AND previous_owner_id IS NOT NULL
            AND previous_owner_id <> new_owner_id AND previous_snapshot IS NOT NULL)
        OR (event_type = 'PROFILE_UPDATED' AND previous_owner_id IS NOT NULL
            AND previous_owner_id = new_owner_id AND previous_snapshot IS NOT NULL)
    ),
    CONSTRAINT chk_owner_history_snapshots CHECK (
        jsonb_typeof(new_snapshot) = 'object'
        AND new_snapshot ?& ARRAY['full_name', 'national_id', 'phone', 'email']
        AND (previous_snapshot IS NULL OR (
            jsonb_typeof(previous_snapshot) = 'object'
            AND previous_snapshot ?& ARRAY['full_name', 'national_id', 'phone', 'email']
        ))
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
CREATE INDEX idx_owner_history_apartment_time
    ON apartment_owner_history(apartment_id, changed_at, id);
CREATE INDEX idx_owner_history_household ON apartment_owner_history(household_id);
CREATE INDEX idx_owner_history_previous_owner ON apartment_owner_history(previous_owner_id);
CREATE INDEX idx_owner_history_new_owner ON apartment_owner_history(new_owner_id);
CREATE INDEX idx_owner_history_actor ON apartment_owner_history(changed_by_user_id);
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

-- 10. Cross-table owner validation (US-001 / DB-02).
-- Lock the person row: assigning an owner must serialize with editing that
-- person's required contact data. A plain EXISTS check is insufficient here.
CREATE FUNCTION validate_household_head()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    owner_record RECORD;
BEGIN
    SELECT national_id, phone INTO owner_record
    FROM persons WHERE id = NEW.head_person_id
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Household head must reference an existing person'
            USING ERRCODE = '23503';
    END IF;
    IF owner_record.national_id IS NULL OR owner_record.phone IS NULL THEN
        RAISE EXCEPTION 'Household head requires national ID and phone'
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_households_validate_head
    BEFORE INSERT OR UPDATE OF head_person_id ON households
    FOR EACH ROW EXECUTE FUNCTION validate_household_head();

-- Guard the reverse direction too: a valid owner cannot lose required fields.
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

CREATE TRIGGER trg_owner_history_append_only
    BEFORE UPDATE OR DELETE OR TRUNCATE ON apartment_owner_history
    FOR EACH STATEMENT EXECUTE FUNCTION reject_history_mutation();
CREATE TRIGGER trg_audit_logs_append_only
    BEFORE UPDATE OR DELETE OR TRUNCATE ON audit_logs
    FOR EACH STATEMENT EXECUTE FUNCTION reject_history_mutation();

COMMIT;
