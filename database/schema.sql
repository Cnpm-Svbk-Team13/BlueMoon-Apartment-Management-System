-- ==============================================================================
-- BlueMoon BFMS - PostgreSQL Database Schema
-- Dự án: Hệ thống Quản lý Thu phí và Dân cư Chung cư BlueMoon (IT3180)
-- Chuẩn CSDL: PostgreSQL 16+ / 17
-- ==============================================================================

-- 1. Bảng Tài khoản người dùng & Phân quyền (RBAC)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL CHECK (role IN ('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_CASHIER', 'ROLE_RESIDENT')),
    failed_attempts INT DEFAULT 0,
    locked_until TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Căn hộ (30 tầng: Tầng 1 Kiot, Tầng 2-5 Tiện ích, Tầng 6-29 Nhà ở, Tầng 30 Penthouse)
CREATE TABLE IF NOT EXISTS apartments (
    id BIGSERIAL PRIMARY KEY,
    room_number VARCHAR(20) UNIQUE NOT NULL,
    floor_number INT NOT NULL CHECK (floor_number BETWEEN 1 AND 30),
    area_sqm NUMERIC(8, 2) NOT NULL CHECK (area_sqm > 0),
    apartment_type VARCHAR(20) NOT NULL CHECK (apartment_type IN ('KIOT', 'RESIDENTIAL', 'PENTHOUSE', 'COMMERCIAL')),
    status VARCHAR(20) DEFAULT 'OCCUPIED' CHECK (status IN ('VACANT', 'OCCUPIED', 'RENOVATING')),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bảng Sổ Hộ khẩu
CREATE TABLE IF NOT EXISTS households (
    id BIGSERIAL PRIMARY KEY,
    apartment_id BIGINT UNIQUE REFERENCES apartments(id) ON DELETE RESTRICT,
    registration_code VARCHAR(50) UNIQUE NOT NULL,
    head_name VARCHAR(100) NOT NULL,
    moved_in_date DATE DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'MOVED_OUT', 'TRANSFERRED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Bảng Nhân khẩu / Cư dân
CREATE TABLE IF NOT EXISTS residents (
    id BIGSERIAL PRIMARY KEY,
    household_id BIGINT REFERENCES households(id) ON DELETE CASCADE,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    national_id VARCHAR(20) UNIQUE, -- Số CCCD / CMND
    phone VARCHAR(20),
    relationship_with_head VARCHAR(50) NOT NULL,
    residence_status VARCHAR(20) DEFAULT 'THUONG_TRU' CHECK (residence_status IN ('THUONG_TRU', 'TAM_TRU', 'TAM_VANG')),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Bảng Danh mục Loại khoản phí
CREATE TABLE IF NOT EXISTS fee_types (
    id BIGSERIAL PRIMARY KEY,
    fee_code VARCHAR(30) UNIQUE NOT NULL,
    fee_name VARCHAR(100) NOT NULL,
    category VARCHAR(30) NOT NULL CHECK (category IN ('SERVICE_MANDATORY', 'MANAGEMENT_MANDATORY', 'VOLUNTARY_FUND')),
    unit_price NUMERIC(12, 2) NOT NULL DEFAULT 0, -- Đơn giá VNĐ/m2 hoặc giá cố định
    calculation_unit VARCHAR(30) NOT NULL DEFAULT 'M2_MONTH', -- 'M2_MONTH', 'FIXED', 'DONATION'
    is_mandatory BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Bảng Chiến dịch Vận động Đóng góp Quỹ tự nguyện
CREATE TABLE IF NOT EXISTS campaign_funds (
    id BIGSERIAL PRIMARY KEY,
    fund_name VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    target_amount NUMERIC(15, 2) DEFAULT 0,
    current_amount NUMERIC(15, 2) DEFAULT 0,
    description TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CLOSED', 'PLANNED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. Bảng Hóa đơn Thu phí định kỳ hàng tháng
CREATE TABLE IF NOT EXISTS invoices (
    id BIGSERIAL PRIMARY KEY,
    invoice_code VARCHAR(50) UNIQUE NOT NULL,
    apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT,
    period_month INT NOT NULL CHECK (period_month BETWEEN 1 AND 12),
    period_year INT NOT NULL CHECK (period_year >= 2020),
    total_amount NUMERIC(15, 2) NOT NULL DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'UNPAID' CHECK (payment_status IN ('UNPAID', 'PAID', 'OVERDUE', 'CANCELLED')),
    due_date DATE NOT NULL,
    paid_at TIMESTAMP WITH TIME ZONE,
    cashier_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_invoice_period UNIQUE (apartment_id, period_month, period_year)
);

-- 8. Bảng Chi tiết Khoản thu trong Hóa đơn
CREATE TABLE IF NOT EXISTS invoice_items (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    fee_type_id BIGINT NOT NULL REFERENCES fee_types(id) ON DELETE RESTRICT,
    item_name VARCHAR(100) NOT NULL,
    quantity NUMERIC(10, 2) NOT NULL, -- Diện tích m2 hoặc số lượng
    unit_price NUMERIC(12, 2) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL
);

-- 9. Bảng Ghi nhận Đóng góp Quỹ tự nguyện (Tùy tâm)
CREATE TABLE IF NOT EXISTS fund_contributions (
    id BIGSERIAL PRIMARY KEY,
    fund_id BIGINT NOT NULL REFERENCES campaign_funds(id) ON DELETE RESTRICT,
    apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT,
    contributor_name VARCHAR(100) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL CHECK (amount > 0),
    contributed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    receipt_code VARCHAR(50) UNIQUE,
    cashier_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    notes TEXT
);

-- 10. Bảng Biên lai / Phiếu thu tiền
CREATE TABLE IF NOT EXISTS receipts (
    id BIGSERIAL PRIMARY KEY,
    receipt_code VARCHAR(50) UNIQUE NOT NULL,
    invoice_id BIGINT REFERENCES invoices(id) ON DELETE SET NULL,
    contribution_id BIGINT REFERENCES fund_contributions(id) ON DELETE SET NULL,
    payer_name VARCHAR(100) NOT NULL,
    total_amount NUMERIC(15, 2) NOT NULL,
    payment_method VARCHAR(30) DEFAULT 'CASH' CHECK (payment_method IN ('CASH', 'BANK_TRANSFER', 'VNPAY', 'OTHER')),
    cashier_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    printed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 11. Bảng Nhật ký Kiểm toán hệ thống (Audit Logs)
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(50) NOT NULL,
    entity_name VARCHAR(50) NOT NULL,
    entity_id BIGINT,
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================================================
-- Chỉ mục (Indexes) tối ưu hóa hiệu năng tra cứu
-- ==============================================================================
CREATE INDEX IF NOT EXISTS idx_apartments_room ON apartments(room_number);
CREATE INDEX IF NOT EXISTS idx_apartments_floor ON apartments(floor_number);
CREATE INDEX IF NOT EXISTS idx_residents_household ON residents(household_id);
CREATE INDEX IF NOT EXISTS idx_residents_national_id ON residents(national_id);
CREATE INDEX IF NOT EXISTS idx_invoices_apt_period ON invoices(apartment_id, period_month, period_year);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(payment_status);
CREATE INDEX IF NOT EXISTS idx_contributions_fund ON fund_contributions(fund_id);
CREATE INDEX IF NOT EXISTS idx_receipts_code ON receipts(receipt_code);
