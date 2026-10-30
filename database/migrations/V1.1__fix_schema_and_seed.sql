-- ==============================================================================
-- BlueMoon BFMS - Database Migration Script V1.1
-- Mục đích: Khắc phục toàn bộ các điểm sai lệch kỹ thuật & đồng bộ CSDL với AGENTS.md
-- Tác giả: Nhóm 13 (Team 13)
-- Ngày thực hiện: 29/10/2026
-- ==============================================================================

-- 1. DEV-13: Chuẩn hóa kiểu dữ liệu diện tích căn hộ thành DECIMAL(12,2)
ALTER TABLE apartments ALTER COLUMN area_sqm TYPE DECIMAL(12, 2);

-- 2. DEV-09: Chuyển ràng buộc UNIQUE trên households.apartment_id thành Partial Unique Index
ALTER TABLE households DROP CONSTRAINT IF EXISTS households_apartment_id_key;
CREATE UNIQUE INDEX IF NOT EXISTS uq_active_household_per_apt ON households(apartment_id) WHERE status = 'ACTIVE';

-- 3. DEV-01: Bổ sung start_date, end_date, ràng buộc NOT NULL và kiểm tra CCCD 12 số cho residents
ALTER TABLE residents ALTER COLUMN residence_status SET NOT NULL;
ALTER TABLE residents ADD COLUMN IF NOT EXISTS start_date DATE;
ALTER TABLE residents ADD COLUMN IF NOT EXISTS end_date DATE;

ALTER TABLE residents DROP CONSTRAINT IF EXISTS chk_residence_dates;
ALTER TABLE residents ADD CONSTRAINT chk_residence_dates 
CHECK (
    residence_status = 'THUONG_TRU' OR 
    (start_date IS NOT NULL AND end_date IS NOT NULL AND end_date >= start_date)
);

ALTER TABLE residents DROP CONSTRAINT IF EXISTS chk_national_id_format;
ALTER TABLE residents ADD CONSTRAINT chk_national_id_format 
CHECK (national_id IS NULL OR national_id ~ '^[0-9]{12}$');

-- 4. DEV-06: Loại bỏ 'VOLUNTARY_FUND' khỏi danh mục fee_types (tuân thủ Nghị định 93/2021)
ALTER TABLE fee_types DROP CONSTRAINT IF EXISTS fee_types_category_check;
ALTER TABLE fee_types ADD CONSTRAINT fee_types_category_check 
CHECK (category IN ('SERVICE_MANDATORY', 'MANAGEMENT_MANDATORY'));

-- 5. DEV-02: Bổ sung cột version phục vụ Khóa lạc quan (@Version) cho invoices
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

-- 6. DEV-08: Chuẩn hóa số tiền đóng góp quỹ tự nguyện tùy tâm (amount >= 0)
ALTER TABLE fund_contributions DROP CONSTRAINT IF EXISTS fund_contributions_amount_check;
ALTER TABLE fund_contributions ADD CONSTRAINT fund_contributions_amount_check 
CHECK (amount >= 0);

-- 7. DEV-07: Bổ sung ràng buộc XOR cho receipts (trỏ tới hóa đơn HOẶC đóng góp quỹ, không gộp chung)
ALTER TABLE receipts DROP CONSTRAINT IF EXISTS chk_receipt_target;
ALTER TABLE receipts ADD CONSTRAINT chk_receipt_target 
CHECK (
    (invoice_id IS NOT NULL AND contribution_id IS NULL) OR 
    (invoice_id IS NULL AND contribution_id IS NOT NULL)
);

-- 8. DEV-12: Dọn dẹp 4 chỉ mục trùng lặp và tạo chỉ mục khóa ngoại tối ưu truy vấn
DROP INDEX IF EXISTS idx_apartments_room;
DROP INDEX IF EXISTS idx_residents_national_id;
DROP INDEX IF EXISTS idx_invoices_apt_period;
DROP INDEX IF EXISTS idx_receipts_code;

CREATE INDEX IF NOT EXISTS idx_apartments_floor ON apartments(floor_number);
CREATE INDEX IF NOT EXISTS idx_households_apartment ON households(apartment_id);
CREATE INDEX IF NOT EXISTS idx_residents_household ON residents(household_id);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(payment_status);
CREATE INDEX IF NOT EXISTS idx_invoice_items_invoice ON invoice_items(invoice_id);
CREATE INDEX IF NOT EXISTS idx_invoice_items_fee_type ON invoice_items(fee_type_id);
CREATE INDEX IF NOT EXISTS idx_contributions_fund ON fund_contributions(fund_id);
CREATE INDEX IF NOT EXISTS idx_contributions_apt ON fund_contributions(apartment_id);
CREATE INDEX IF NOT EXISTS idx_receipts_invoice ON receipts(invoice_id);
CREATE INDEX IF NOT EXISTS idx_receipts_contribution ON receipts(contribution_id);

-- 9. DEV-03: Cập nhật mã băm BCrypt chuẩn cho mật khẩu 'Password@123'
UPDATE users 
SET password_hash = '$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK'
WHERE username IN ('admin', 'ketoan', 'thuquy', 'cudan1');
