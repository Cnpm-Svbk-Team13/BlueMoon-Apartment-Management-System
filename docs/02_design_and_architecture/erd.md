[🏠 Trang chủ Docs](../README.md) > [Giai đoạn 2: Thiết kế Hệ thống](./README.md) > **erd.md**

---

# MÔ HÌNH THỰC THỂ LIÊN KẾT (ENTITY-RELATIONSHIP DIAGRAM - ERD)
## Dự án: Hệ thống Quản lý Thu phí và Dân cư Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Sơ đồ Quan hệ Thực thể (Mermaid ERD)

```mermaid
erDiagram
    USERS ||--o{ RECEIPTS : "issues"
    USERS ||--o{ AUDIT_LOGS : "triggers"
    USERS ||--o{ INVOICES : "confirms"
    
    APARTMENTS ||--o| HOUSEHOLDS : "belongs to"
    APARTMENTS ||--o{ INVOICES : "billed for"
    APARTMENTS ||--o{ FUND_CONTRIBUTIONS : "contributes"
    
    HOUSEHOLDS ||--|{ RESIDENTS : "includes"
    
    FEE_TYPES ||--o{ INVOICE_ITEMS : "defines"
    
    INVOICES ||--|{ INVOICE_ITEMS : "contains"
    INVOICES ||--o| RECEIPTS : "generates"
    
    CAMPAIGN_FUNDS ||--o{ FUND_CONTRIBUTIONS : "receives"
    FUND_CONTRIBUTIONS ||--o| RECEIPTS : "generates"

    USERS {
        bigint id PK
        string username UK
        string password_hash
        string full_name
        string role
        boolean is_active
    }

    APARTMENTS {
        bigint id PK
        string room_number UK
        int floor_number
        decimal area_sqm "DECIMAL(12,2) > 0"
        string apartment_type
        string status
    }

    HOUSEHOLDS {
        bigint id PK
        bigint apartment_id FK "Partial UK on status=ACTIVE"
        string registration_code UK
        string head_name
        date moved_in_date
        string status
    }

    RESIDENTS {
        bigint id PK
        bigint household_id FK
        string full_name
        date date_of_birth
        string gender
        string national_id UK "12 chữ số"
        string relationship_with_head
        string residence_status "NOT NULL: THUONG_TRU, TAM_TRU, TAM_VANG"
        date start_date "Bắt buộc nếu TAM_TRU/TAM_VANG"
        date end_date "Bắt buộc nếu TAM_TRU/TAM_VANG"
    }

    FEE_TYPES {
        bigint id PK
        string fee_code UK
        string fee_name
        string category "SERVICE_MANDATORY, MANAGEMENT_MANDATORY"
        decimal unit_price
        string calculation_unit
        boolean is_mandatory
    }

    CAMPAIGN_FUNDS {
        bigint id PK
        string fund_name
        date start_date
        date end_date
        decimal target_amount
        decimal current_amount
        string status
    }

    INVOICES {
        bigint id PK
        string invoice_code UK
        bigint apartment_id FK
        int period_month
        int period_year
        decimal total_amount
        string payment_status
        date due_date
        timestamp paid_at
        bigint version "Khóa lạc quan @Version"
    }

    INVOICE_ITEMS {
        bigint id PK
        bigint invoice_id FK
        bigint fee_type_id FK
        string item_name
        decimal quantity
        decimal unit_price
        decimal amount
    }

    FUND_CONTRIBUTIONS {
        bigint id PK
        bigint fund_id FK
        bigint apartment_id FK
        string contributor_name
        decimal amount ">= 0"
        timestamp contributed_at
    }

    RECEIPTS {
        bigint id PK
        string receipt_code UK
        bigint invoice_id FK "XOR: hoặc invoice_id"
        bigint contribution_id FK "XOR: hoặc contribution_id"
        string payer_name
        decimal total_amount
        string payment_method
        timestamp created_at
    }

    AUDIT_LOGS {
        bigint id PK
        bigint user_id FK
        string action
        string entity_name
        bigint entity_id
        text details
        timestamp created_at
    }
```

---

## 2. Chi tiết các Thực thể (Data Dictionary)

| Thực thể | Tên bảng | Ý nghĩa nghiệp vụ | Khóa chính | Khóa ngoại & Ràng buộc cốt lõi |
|---|---|---|---|---|
| Người dùng | `users` | Tài khoản BQT, Kế toán, Thủ quỹ, Cư dân | `id` | `username` (UK), BCrypt hash, RBAC |
| Căn hộ | `apartments` | 30 tầng căn hộ (Kiot, Nhà ở, Penthouse) | `id` | `room_number` (UK), `area_sqm > 0` |
| Hộ khẩu | `households` | Sổ hộ khẩu gắn với căn hộ | `id` | `apartment_id` $\rightarrow$ `apartments(id)` (Partial UK on `status = 'ACTIVE'`) |
| Nhân khẩu | `residents` | Cư dân sinh sống (thường trú, tạm trú, tạm vắng) | `id` | `household_id` $\rightarrow$ `households(id)`, `national_id` 12 số, `start_date` & `end_date` bắt buộc với tạm trú/vắng |
| Loại phí | `fee_types` | Biểu phí dịch vụ và quản lý bắt buộc theo $m^2$ | `id` | `fee_code` (UK), chỉ gồm `SERVICE_MANDATORY` và `MANAGEMENT_MANDATORY` |
| Chiến dịch quỹ | `campaign_funds` | Đợt vận động tự nguyện độc lập (Vì người nghèo, Biển đảo) | `id` | Quản lý độc lập khỏi công nợ bắt buộc |
| Hóa đơn | `invoices` | Bảng kê tiền phí định kỳ hàng tháng của căn hộ | `id` | `apartment_id` $\rightarrow$ `apartments(id)`, Khóa lạc quan `version BIGINT` |
| Chi tiết hóa đơn | `invoice_items` | Từng khoản tính theo $m^2$ trong hóa đơn | `id` | `invoice_id` $\rightarrow$ `invoices(id)`, `fee_type_id` $\rightarrow$ `fee_types(id)` |
| Đóng góp quỹ | `fund_contributions` | Giao dịch ủng hộ quỹ tự nguyện tại quầy (tùy tâm $\ge 0$) | `id` | `fund_id` $\rightarrow$ `campaign_funds(id)`, `apartment_id` $\rightarrow$ `apartments(id)` |
| Biên lai | `receipts` | Phiếu thu/xác nhận xuất cho cư dân khi nộp tiền | `id` | Ràng buộc XOR: Trỏ tới `invoice_id` HOẶC `contribution_id`, không gộp chung |
| Nhật ký | `audit_logs` | Lịch sử thao tác biến động nhân khẩu và tiền | `id` | `user_id` $\rightarrow$ `users(id)` |

*Chi tiết các câu lệnh SQL khởi tạo được lưu trữ tại:* [`database/schema.sql`](../../database/schema.sql) và [`database/seed_data.sql`](../../database/seed_data.sql).
