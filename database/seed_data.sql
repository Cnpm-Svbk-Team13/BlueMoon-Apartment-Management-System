-- ==============================================================================
-- BlueMoon BFMS - Sample Seed Data
-- Dữ liệu mẫu khởi tạo phục vụ phát triển, kiểm thử và demo đồ án
-- ==============================================================================

-- 1. Tài khoản mẫu (Mật khẩu mặc định là: Password@123)
-- Chuỗi băm BCrypt chuẩn 10 rounds của 'Password@123': $2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK
INSERT INTO users (username, password_hash, full_name, email, phone, role, is_active)
VALUES 
    ('admin', '$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK', 'Quản trị viên Ban Quản trị', 'admin@bluemoon.vn', '0912345678', 'ROLE_ADMIN', TRUE),
    ('ketoan', '$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK', 'Nguyễn Thị Kế Toán', 'ketoan@bluemoon.vn', '0923456789', 'ROLE_ACCOUNTANT', TRUE),
    ('thuquy', '$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK', 'Trần Văn Thủ Quỹ', 'thuquy@bluemoon.vn', '0934567890', 'ROLE_CASHIER', TRUE),
    ('cudan1', '$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK', 'Lê Cư Dân', 'cudan1@bluemoon.vn', '0945678901', 'ROLE_RESIDENT', TRUE)
ON CONFLICT (username) DO NOTHING;

-- 2. Danh mục Loại khoản phí chuẩn theo đề bài (Phí bắt buộc theo m2)
INSERT INTO fee_types (fee_code, fee_name, category, unit_price, calculation_unit, is_mandatory, is_active, description)
VALUES
    ('FEE_SERVICE', 'Phí dịch vụ chung cư', 'SERVICE_MANDATORY', 5000.00, 'M2_MONTH', TRUE, TRUE, 'Vệ sinh, rác thải, an ninh bảo vệ 24/7 (2.500 - 16.500 đ/m2)'),
    ('FEE_MANAGEMENT', 'Phí quản lý chung cư', 'MANAGEMENT_MANDATORY', 7000.00, 'M2_MONTH', TRUE, TRUE, 'Vận hành thang máy, máy bơm, PCCC, chiếu sáng (>= 7.000 đ/m2)')
ON CONFLICT (fee_code) DO NOTHING;

-- 3. Các Đợt vận động Quỹ tự nguyện (Độc lập, không gộp vào công nợ tháng)
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

-- 6. Danh sách cư dân trong hộ (Bao gồm Thường trú, Tạm trú và Tạm vắng chuẩn Luật Cư trú 2020)
INSERT INTO residents (household_id, full_name, date_of_birth, gender, national_id, phone, relationship_with_head, residence_status, start_date, end_date)
VALUES
    -- Hộ P.601: Thường trú
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00601'), 'Nguyễn Văn An', '1980-04-12', 'MALE', '001080001234', '0912111222', 'CHỦ HỘ', 'THUONG_TRU', NULL, NULL),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00601'), 'Vũ Thị Hoa', '1983-09-25', 'FEMALE', '001183002345', '0912111333', 'VỢ', 'THUONG_TRU', NULL, NULL),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00601'), 'Nguyễn Minh Quân', '2010-11-05', 'MALE', NULL, NULL, 'CON TRAI', 'THUONG_TRU', NULL, NULL),
    -- Hộ P.602: Thường trú + Tạm trú (có thời hạn từ ngày - đến ngày)
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00602'), 'Trần Thị Bình', '1985-07-20', 'FEMALE', '001185004567', '0922334455', 'CHỦ HỘ', 'THUONG_TRU', NULL, NULL),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-00602'), 'Lê Văn Đạt', '1998-03-15', 'MALE', '001098005678', '0933445566', 'KHÁCH THUÊ', 'TAM_TRU', '2026-01-01', '2026-12-31'),
    -- Hộ P.1204: Thường trú + Tạm vắng (có thời hạn từ ngày - đến ngày)
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-01204'), 'Hoàng Minh Cường', '1975-02-18', 'MALE', '001075003456', '0988777666', 'CHỦ HỘ', 'THUONG_TRU', NULL, NULL),
    ((SELECT id FROM households WHERE registration_code = 'HK-BM-01204'), 'Hoàng Minh Đức', '2002-08-10', 'MALE', '001202006789', '0977889900', 'CON TRAI', 'TAM_VANG', '2026-09-01', '2027-06-30')
ON CONFLICT (national_id) DO NOTHING;

-- 7. Hóa đơn mẫu tháng 10/2026
-- Căn P.601: Diện tích 75.5m2 => Dịch vụ (75.5 * 5000 = 377.500) + Quản lý (75.5 * 7000 = 528.500) = 906.000 VNĐ (UNPAID)
-- Căn P.602: Diện tích 85.0m2 => Dịch vụ (85 * 5000 = 425.000) + Quản lý (85 * 7000 = 595.000) = 1.020.000 VNĐ (UNPAID)
-- Căn P.1204: Diện tích 92.0m2 => Dịch vụ (92 * 5000 = 460.000) + Quản lý (92 * 7000 = 644.000) = 1.104.000 VNĐ (PAID)
INSERT INTO invoices (invoice_code, apartment_id, period_month, period_year, total_amount, payment_status, due_date, paid_at, cashier_id, version)
VALUES
    ('INV-202610-P601', (SELECT id FROM apartments WHERE room_number = 'P.601'), 10, 2026, 906000.00, 'UNPAID', '2026-10-31', NULL, NULL, 0),
    ('INV-202610-P602', (SELECT id FROM apartments WHERE room_number = 'P.602'), 10, 2026, 1020000.00, 'UNPAID', '2026-10-31', NULL, NULL, 0),
    ('INV-202610-P1204', (SELECT id FROM apartments WHERE room_number = 'P.1204'), 10, 2026, 1104000.00, 'PAID', '2026-10-31', '2026-10-15 14:30:00+07', (SELECT id FROM users WHERE username = 'thuquy'), 1)
ON CONFLICT (apartment_id, period_month, period_year) DO NOTHING;

-- 8. Chi tiết hóa đơn
INSERT INTO invoice_items (invoice_id, fee_type_id, item_name, quantity, unit_price, amount)
VALUES
    -- Hóa đơn P.601
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P601'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_SERVICE'), 'Phí dịch vụ chung cư', 75.50, 5000.00, 377500.00),
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P601'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_MANAGEMENT'), 'Phí quản lý chung cư', 75.50, 7000.00, 528500.00),
    -- Hóa đơn P.602
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P602'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_SERVICE'), 'Phí dịch vụ chung cư', 85.00, 5000.00, 425000.00),
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P602'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_MANAGEMENT'), 'Phí quản lý chung cư', 85.00, 7000.00, 595000.00),
    -- Hóa đơn P.1204
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P1204'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_SERVICE'), 'Phí dịch vụ chung cư', 92.00, 5000.00, 460000.00),
    ((SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P1204'), (SELECT id FROM fee_types WHERE fee_code = 'FEE_MANAGEMENT'), 'Phí quản lý chung cư', 92.00, 7000.00, 644000.00);

-- 9. Ghi nhận Đóng góp Quỹ tự nguyện (Độc lập, tùy tâm)
INSERT INTO fund_contributions (fund_id, apartment_id, contributor_name, amount, contributed_at, receipt_code, cashier_id, notes)
VALUES
    ((SELECT id FROM campaign_funds WHERE fund_name = 'Quỹ vì người nghèo năm 2026'), 
     (SELECT id FROM apartments WHERE room_number = 'P.601'), 
     'Nguyễn Văn An', 500000.00, '2026-10-10 09:15:00+07', 'REC-202610-F001', 
     (SELECT id FROM users WHERE username = 'thuquy'), 'Ủng hộ người nghèo'),
    ((SELECT id FROM campaign_funds WHERE fund_name = 'Quỹ Khuyến học & Vì trẻ thơ BlueMoon'), 
     (SELECT id FROM apartments WHERE room_number = 'P.1204'), 
     'Hoàng Minh Cường', 1000000.00, '2026-10-12 10:00:00+07', 'REC-202610-F002', 
     (SELECT id FROM users WHERE username = 'thuquy'), 'Ủng hộ phong trào khuyến học');

-- 10. Biên lai / Phiếu thu tiền (Tuân thủ ràng buộc XOR: hoặc Invoice hoặc Contribution, không gộp chung)
INSERT INTO receipts (receipt_code, invoice_id, contribution_id, payer_name, total_amount, payment_method, cashier_id, printed_at, created_at)
VALUES
    -- Biên lai thu phí bắt buộc căn P.1204 (invoice_id NOT NULL, contribution_id NULL)
    ('REC-202610-INV-1204', 
     (SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P1204'), 
     NULL, 
     'Hoàng Minh Cường', 1104000.00, 'BANK_TRANSFER', 
     (SELECT id FROM users WHERE username = 'thuquy'), 
     '2026-10-15 14:30:00+07', '2026-10-15 14:30:00+07'),
    -- Phiếu xác nhận đóng góp Quỹ vì người nghèo căn P.601 (invoice_id NULL, contribution_id NOT NULL)
    ('REC-202610-F001', 
     NULL, 
     (SELECT id FROM fund_contributions WHERE receipt_code = 'REC-202610-F001'), 
     'Nguyễn Văn An', 500000.00, 'CASH', 
     (SELECT id FROM users WHERE username = 'thuquy'), 
     '2026-10-10 09:15:00+07', '2026-10-10 09:15:00+07')
ON CONFLICT (receipt_code) DO NOTHING;

-- 11. Nhật ký Kiểm toán hệ thống mẫu (Audit Logs)
INSERT INTO audit_logs (user_id, action, entity_name, entity_id, details, ip_address)
VALUES
    ((SELECT id FROM users WHERE username = 'admin'), 'LOGIN_SUCCESS', 'users', (SELECT id FROM users WHERE username = 'admin'), 'Đăng nhập hệ thống thành công', '192.168.1.10'),
    ((SELECT id FROM users WHERE username = 'thuquy'), 'PAY_INVOICE', 'invoices', (SELECT id FROM invoices WHERE invoice_code = 'INV-202610-P1204'), 'Gạch nợ hóa đơn tháng 10/2026 cho căn P.1204 số tiền 1.104.000 VNĐ', '192.168.1.15');
