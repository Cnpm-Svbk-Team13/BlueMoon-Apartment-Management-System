-- BlueMoon BFMS - Synthetic Sprint 1 seed, PostgreSQL 16/17.
-- US-SEC-01/02, NFR-02 -> DB-01, BE-01;
-- US-001, RAW-001/002 -> DB-02, BE-03, QA-05, BA-03.
-- Run AFTER schema.sql, ONLY when all seven tables are empty and the app is stopped.
-- Demo accounts: admin, ketoan, thuquy (active), bqt_inactive (disabled).
-- Demo password: Password@123; BCrypt cost 10, verified with Spring Security.
-- All identities and contacts are synthetic. Do not use on a live resident DB.
-- One-time seed: refuse repeat/partial seed; never reset existing data/passwords.
-- psql --set=ON_ERROR_STOP=1 --file=database/seed_data.sql.

BEGIN;
SET LOCAL TIME ZONE 'UTC';
SET LOCAL lock_timeout = '2s';

-- Maintenance-only seed. Fail fast if any writer is still active, then reject
-- nonempty tables atomically. Lock order follows table dependencies.
LOCK TABLE persons, users, apartments, households, residents,
    household_head_history, audit_logs IN SHARE ROW EXCLUSIVE MODE;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM users UNION ALL SELECT 1 FROM persons
        UNION ALL SELECT 1 FROM apartments UNION ALL SELECT 1 FROM households
        UNION ALL SELECT 1 FROM residents
        UNION ALL SELECT 1 FROM household_head_history
        UNION ALL SELECT 1 FROM audit_logs
    ) THEN
        RAISE EXCEPTION 'Sprint 1 seed requires all seven tables to be empty; no data was inserted';
    END IF;
END;
$$;

-- 1. Staff profiles and their accounts; staff need not be residents.
INSERT INTO persons (full_name, email, phone) VALUES
    ('Quản trị mẫu', 'admin@example.test', '0900000101'),
    ('Kế toán mẫu', 'ketoan@example.test', '0900000102'),
    ('Thủ quỹ mẫu', 'thuquy@example.test', '0900000103'),
    ('Tài khoản vô hiệu hóa mẫu', 'inactive@example.test', '0900000104');
INSERT INTO users (username, password_hash, person_id, role, is_active)
SELECT sample.username, '$2a$10$ljvESH1dmbST9gRG0Gr1tOKvvvsfcVe3FbO4nV8jFqPsgrxNYXVXu',
    p.id, sample.role, sample.active
FROM (VALUES
    ('admin', 'admin@example.test', 'ROLE_ADMIN', TRUE),
    ('ketoan', 'ketoan@example.test', 'ROLE_ACCOUNTANT', TRUE),
    ('thuquy', 'thuquy@example.test', 'ROLE_CASHIER', TRUE),
    ('bqt_inactive', 'inactive@example.test', 'ROLE_ACCOUNTANT', FALSE)
) AS sample(username, email, role, active)
JOIN persons p ON p.email = sample.email;

-- 2. Shared identities. The child intentionally has no national ID or phone.
INSERT INTO persons
    (full_name, national_id, phone, email, date_of_birth, gender, created_at)
VALUES
    ('Nguyễn An Mẫu', '000000000001', '0900000001', 'an@example.test', '1980-04-12', 'MALE', '2026-09-01 08:00:00+07'),
    ('Trần Lan Mẫu', '000000000002', '0900000002', 'lan@example.test', '1985-07-20', 'FEMALE', '2026-09-01 08:00:00+07'),
    ('Lê Bình Mẫu', '000000000003', '0900000003', NULL, '1987-06-15', 'FEMALE', '2026-09-01 08:00:00+07'),
    ('Bé Mẫu Không CCCD', NULL, NULL, NULL, '2016-11-05', 'MALE', '2026-09-01 08:00:00+07'),
    ('Phạm Đạt Mẫu', '000000000004', '0900000004', NULL, '1998-03-15', 'MALE', '2026-09-01 08:00:00+07'),
    ('Hoàng Cường Mẫu', '000000000005', '0900000005', 'cuong@example.test', '1975-02-18', 'MALE', '2026-09-01 08:00:00+07'),
    ('Hoàng Đức Mẫu', '000000000006', '0900000006', NULL, '2002-08-10', 'MALE', '2026-09-01 08:00:00+07');

-- 3. Four surface types and two apartment states.
INSERT INTO apartments
    (room_number, floor_number, area_sqm, apartment_type, status, description, created_at)
VALUES
    ('K.101', 1, 120.00, 'KIOT', 'VACANT', 'Kiot mẫu', '2026-09-01 08:00:00+07'),
    ('TM.201', 2, 100.00, 'COMMERCIAL', 'VACANT', 'Thương mại mẫu', '2026-09-01 08:00:00+07'),
    ('P.601', 6, 75.50, 'RESIDENTIAL', 'OCCUPIED', 'Căn có lịch sử thay đại diện', '2026-09-01 08:00:00+07'),
    ('P.602', 6, 85.00, 'COMMERCIAL', 'OCCUPIED', 'Căn có người tạm trú', '2026-09-01 08:00:00+07'),
    ('P.701', 7, 75.50, 'RESIDENTIAL', 'VACANT', 'Căn chưa khai báo hộ', '2026-09-01 08:00:00+07'),
    ('P.1204', 12, 92.00, 'RESIDENTIAL', 'OCCUPIED', 'Căn có người tạm vắng', '2026-09-01 08:00:00+07'),
    ('PH.3001', 30, 220.00, 'PENTHOUSE', 'VACANT', 'Penthouse mẫu', '2026-09-01 08:00:00+07');

-- 4. Current household representatives. P.601 changed from An to Lan on 01/11.
INSERT INTO households
    (apartment_id, head_person_id, registration_code, moved_in_date, created_at)
SELECT a.id, p.id, sample.registration_code, sample.moved_in_date, sample.created_at
FROM (VALUES
    ('P.601', '000000000002', 'HK-BM-00601', DATE '2026-10-01', TIMESTAMPTZ '2026-10-01 08:00:00+07'),
    ('P.602', '000000000003', 'HK-BM-00602', DATE '2026-10-01', TIMESTAMPTZ '2026-10-01 08:30:00+07'),
    ('P.1204', '000000000005', 'HK-BM-01204', DATE '2026-10-01', TIMESTAMPTZ '2026-10-01 09:00:00+07')
) AS sample(room_number, national_id, registration_code, moved_in_date, created_at)
JOIN apartments a ON a.room_number = sample.room_number
JOIN persons p ON p.national_id = sample.national_id;

-- 5. Residence data. Full-name joins below identify only these known seed rows;
-- names are NOT a general identity key for application code.
INSERT INTO residents
    (person_id, household_id, relationship_with_head, residence_status, start_date, end_date)
SELECT p.id, h.id, sample.relationship, sample.status, sample.start_date, sample.end_date
FROM (VALUES
    ('Trần Lan Mẫu', 'HK-BM-00601', 'HEAD', 'THUONG_TRU', NULL::DATE, NULL::DATE),
    ('Bé Mẫu Không CCCD', 'HK-BM-00601', 'CHILD', 'THUONG_TRU', NULL, NULL),
    ('Lê Bình Mẫu', 'HK-BM-00602', 'HEAD', 'THUONG_TRU', NULL, NULL),
    ('Phạm Đạt Mẫu', 'HK-BM-00602', 'TENANT', 'TAM_TRU', DATE '2026-10-01', DATE '2027-09-30'),
    ('Hoàng Cường Mẫu', 'HK-BM-01204', 'HEAD', 'THUONG_TRU', NULL, NULL),
    ('Hoàng Đức Mẫu', 'HK-BM-01204', 'CHILD', 'TAM_VANG', DATE '2026-10-01', DATE '2027-03-31')
) AS sample(full_name, registration_code, relationship, status, start_date, end_date)
JOIN persons p ON p.full_name = sample.full_name
JOIN households h ON h.registration_code = sample.registration_code;

-- 6. Synthetic history: actor is a users row; heads are persons rows.
-- Snapshots copy actual fixture fields, preserving SQL NULL email as JSON null.
INSERT INTO household_head_history
    (household_id, previous_head_person_id, new_head_person_id,
     previous_snapshot, new_snapshot, changed_at, changed_by_user_id)
SELECT h.id, previous_head.id, new_head.id,
    CASE WHEN previous_head.id IS NULL THEN NULL ELSE jsonb_build_object(
        'full_name', previous_head.full_name, 'national_id', previous_head.national_id,
        'phone', previous_head.phone, 'email', previous_head.email) END,
    jsonb_build_object('full_name', new_head.full_name, 'national_id', new_head.national_id,
        'phone', new_head.phone, 'email', new_head.email),
    sample.changed_at, actor.id
FROM (VALUES
    ('HK-BM-00601', NULL::VARCHAR, '000000000001', TIMESTAMPTZ '2026-10-01 08:00:00+07'),
    ('HK-BM-00601', '000000000001', '000000000002', TIMESTAMPTZ '2026-11-01 09:00:00+07'),
    ('HK-BM-00602', NULL, '000000000003', TIMESTAMPTZ '2026-10-01 08:30:00+07'),
    ('HK-BM-01204', NULL, '000000000005', TIMESTAMPTZ '2026-10-01 09:00:00+07')
) AS sample(registration_code, previous_id_number, new_id_number, changed_at)
JOIN households h ON h.registration_code = sample.registration_code
LEFT JOIN persons previous_head ON previous_head.national_id = sample.previous_id_number
JOIN persons new_head ON new_head.national_id = sample.new_id_number
JOIN users actor ON actor.username = 'admin';

-- 7. Audit matches the synthetic head events, not evidence of real UAT.
INSERT INTO audit_logs (user_id, action, entity_name, entity_id, details, created_at)
SELECT changed_by_user_id, CASE WHEN previous_head_person_id IS NULL THEN 'ASSIGNED'
    WHEN previous_head_person_id = new_head_person_id THEN 'PROFILE_UPDATED' ELSE 'REPLACED' END,
    'household_head_history', id,
    'Dữ liệu mẫu Sprint 1: sự kiện chủ hộ', changed_at
FROM household_head_history;

COMMIT;
