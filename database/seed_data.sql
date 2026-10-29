-- ==============================================================================
-- BlueMoon BFMS - Sample Seed Data
-- Dữ liệu mẫu khởi tạo phục vụ phát triển, kiểm thử và demo đồ án
-- ==============================================================================

-- 1. Tài khoản mẫu (Mật khẩu mặc định là: Password@123)
-- Chuỗi băm BCrypt của 'Password@123' là '$2a$10$7j4m7Uq3vW0B8mGZ2Yk0k.k8eWjW9E1W5X6Q2J4K8M0O2P4R6T8V0'
INSERT INTO users (username, password_hash, full_name, email, phone, role, is_active)
VALUES 
    ('admin', '$2a$10$7j4m7Uq3vW0B8mGZ2Yk0k.k8eWjW9E1W5X6Q2J4K8M0O2P4R6T8V0', 'Quản trị viên Ban Quản trị', 'admin@bluemoon.vn', '0912345678', 'ROLE_ADMIN', TRUE),
    ('ketoan', '$2a$10$7j4m7Uq3vW0B8mGZ2Yk0k.k8eWjW9E1W5X6Q2J4K8M0O2P4R6T8V0', 'Nguyễn Thị Kế Toán', 'ketoan@bluemoon.vn', '0923456789', 'ROLE_ACCOUNTANT', TRUE),
    ('thuquy', '$2a$10$7j4m7Uq3vW0B8mGZ2Yk0k.k8eWjW9E1W5X6Q2J4K8M0O2P4R6T8V0', 'Trần Văn Thủ Quỹ', 'thuquy@bluemoon.vn', '0934567890', 'ROLE_CASHIER', TRUE),
    ('cudan1', '$2a$10$7j4m7Uq3vW0B8mGZ2Yk0k.k8eWjW9E1W5X6Q2J4K8M0O2P4R6T8V0', 'Lê Cư Dân', 'cudan1@bluemoon.vn', '0945678901', 'ROLE_RESIDENT', TRUE)
ON CONFLICT (username) DO NOTHING;

-- 2. Danh mục Loại khoản phí chuẩn theo đề bài
INSERT INTO fee_types (fee_code, fee_name, category, unit_price, calculation_unit, is_mandatory, is_active, description)
VALUES
    ('FEE_SERVICE', 'Phí dịch vụ chung cư', 'SERVICE_MANDATORY', 5000.00, 'M2_MONTH', TRUE, TRUE, 'Vệ sinh, rác thải, an ninh bảo vệ 24/7 (2.500 - 16.500 đ/m2)'),
    ('FEE_MANAGEMENT', 'Phí quản lý chung cư', 'MANAGEMENT_MANDATORY', 7000.00, 'M2_MONTH', TRUE, TRUE, 'Vận hành thang máy, máy bơm, PCCC, chiếu sáng (>= 7.000 đ/m2)')
ON CONFLICT (fee_code) DO NOTHING;

-- 3. Các Đợt vận động Quỹ tự nguyện
INSERT INTO campaign_funds (fund_name, start_date, end_date, target_amount, current_amount, description, status)
VALUES
    ('Quỹ vì người nghèo năm 2026', '2026-10-01', '2026-11-30', 50000000.00, 12500000.00, 'Ủng hộ người nghèo và các hộ có hoàn cảnh đặc biệt khó khăn', 'ACTIVE'),
    ('Quỹ Đền ơn đáp nghĩa 2026', '2026-07-01', '2026-08-31', 30000000.00, 31000000.00, 'Tri ân các gia đình thương binh liệt sĩ và người có công', 'CLOSED'),
    ('Quỹ Khuyến học & Vì trẻ thơ BlueMoon', '2026-09-01', '2026-12-31', 20000000.00, 8500000.00, 'Khen thưởng học sinh giỏi và hỗ trợ trẻ em khó khăn', 'ACTIVE');

-- 4. Danh sách căn hộ mẫu đại diện cho các phân khu
INSERT INTO apartments (room_number, floor_number, area_sqm, apartment_type, status, description)
VALUES
    ('K.101', 1, 120.00, 'KIOT', 'OCCUPIED', 'Ki-ốt kinh doanh tầng 1'),
    ('P.601', 6, 75.50, 'RESIDENTIAL', 'OCCUPIED', 'Căn hộ gia đình 2 phòng ngủ'),
    ('P.602', 6, 85.00, 'RESIDENTIAL', 'OCCUPIED', 'Căn hộ gia đình 3 phòng ngủ'),
    ('P.701', 7, 75.50, 'RESIDENTIAL', 'OCCUPIED', 'Căn hộ gia đình 2 phòng ngủ'),
    ('P.1204', 12, 92.00, 'RESIDENTIAL', 'OCCUPIED', 'Căn hộ gia đình 3 phòng ngủ căn góc'),
    ('PH.3001', 30, 220.00, 'PENTHOUSE', 'OCCUPIED', 'Căn hộ Penthouse cao cấp tầng 30')
ON CONFLICT (room_number) DO NOTHING;

-- 5. Sổ hộ khẩu mẫu
INSERT INTO households (apartment_id, registration_code, head_name, moved_in_date, status)
VALUES
    ((SELECT id FROM apartments WHERE room_number = 'P.601'), 'HK-BM-00601', 'Nguyễn Văn An', '2023-05-15', 'ACTIVE'),
    ((SELECT id FROM apartments WHERE room_number = 'P.602'), 'HK-BM-00602', 'Trần Thị Bình', '2023-06-20', 'ACTIVE'),
    ((SELECT id FROM apartments WHERE room_number = 'P.1204'), 'HK-BM-01204', 'Hoàng Minh Cường', '2023-08-10', 'ACTIVE')
ON CONFLICT (registration_code) DO NOTHING;

-- 6. Danh sách cư dân trong hộ
INSERT INTO residents (household_id, full_name, date_of_birth, gender, national_id, phone, relationship_with_head, residence_status)
VALUES
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00601'), 'Nguyễn Văn An', '1980-04-12', 'MALE', '001080001234', '0912111222', 'CHỦ HỘ', 'THUONG_TRU'),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00601'), 'Vũ Thị Hoa', '1983-09-25', 'FEMALE', '001183002345', '0912111333', 'VỢ', 'THUONG_TRU'),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00601'), 'Nguyễn Minh Quân', '2010-11-05', 'MALE', NULL, NULL, 'CON TRAI', 'THUONG_TRU'),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-01204'), 'Hoàng Minh Cường', '1975-02-18', 'MALE', '001075003456', '0988777666', 'CHỦ HỘ', 'THUONG_TRU')
ON CONFLICT (national_id) DO NOTHING;

-- 7. Hóa đơn mẫu tháng 10/2026
-- Căn P.601: Diện tích 75.5m2 => Dịch vụ (75.5 * 5000 = 377.500) + Quản lý (75.5 * 7000 = 528.500) = 906.000 VNĐ
INSERT INTO invoices (invoice_code, apartment_id, period_month, period_year, total_amount, payment_status, due_date)
VALUES
    ('INV-202610-P601', (SELECT id FROM apartments WHERE room_number = 'P.601'), 10, 2026, 906000.00, 'UNPAID', '2026-10-31'),
    ('INV-202610-P602', (SELECT id FROM apartments WHERE room_number = 'P.602'), 10, 2026, 1020000.00, 'UNPAID', '2026-10-31'),
    ('INV-202610-P1204', (SELECT id FROM apartments WHERE room_number = 'P.1204'), 10, 2026, 1104000.00, 'PAID', '2026-10-31')
ON CONFLICT (apartment_id, period_month, period_year) DO NOTHING;

-- Chi tiết hóa đơn P.601
INSERT INTO invoice_items (invoice_id, fee_type_id, item_name, quantity, unit_price, amount)
VALUES
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P601'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_SERVICE'), 'Phí dịch vụ chung cư', 75.50, 5000.00, 377500.00),
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P601'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_MANAGEMENT'), 'Phí quản lý chung cư', 75.50, 7000.00, 528500.00);
