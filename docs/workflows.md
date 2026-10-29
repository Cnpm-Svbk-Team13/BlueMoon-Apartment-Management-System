# LUỒNG NGHIỆP VỤ HỆ THỐNG (SYSTEM WORKFLOWS)
## Dự án: Hệ thống Quản lý Thu phí Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Tổng quan 4 Luồng nghiệp vụ cốt lõi

Theo yêu cầu trọng tâm của đề bài môn Công nghệ phần mềm, phiên bản v1.0 của hệ thống BlueMoon BFMS tập trung xử lý **4 luồng nghiệp vụ chính**:
1. **Luồng 1: Đăng ký & Quản lý tài khoản**
2. **Luồng 2: Tạo & Cấu hình khoản thu**
3. **Luồng 3: Thu phí & Phát hành biên lai tại quầy**
4. **Luồng 4: Thống kê & Báo cáo các khoản đóng góp và thu phí**

---

## 2. Chi tiết từng Luồng nghiệp vụ

### 2.1. Luồng 1: Đăng ký & Quản lý tài khoản (Authentication & Account Management)

```mermaid
sequenceDiagram
    autonumber
    actor User as Thành viên BQT
    participant UI as Giao diện Đăng nhập
    participant Auth as Hệ thống Xác thực
    participant DB as Cơ sở dữ liệu

    User->>UI: Nhập Username & Password
    UI->>Auth: Gửi thông tin đăng nhập
    Auth->>DB: Truy vấn thông tin người dùng & mật khẩu băm
    DB-->>Auth: Trả về kết quả
    alt Mật khẩu chính xác
        Auth->>Auth: Thiết lập phiên làm việc (Session) & Phân quyền (Role)
        Auth-->>UI: Đăng nhập thành công
        UI-->>User: Điều hướng tới Màn hình quản trị (Dashboard)
    else Mật khẩu sai < 5 lần
        Auth-->>UI: Báo lỗi "Sai tài khoản hoặc mật khẩu"
        UI-->>User: Hiển thị cảnh báo
    else Mật khẩu sai >= 5 lần
        Auth->>DB: Tạm khóa tài khoản (15 phút)
        Auth-->>UI: Báo lỗi "Tài khoản tạm thời bị khóa"
        UI-->>User: Hiển thị thông báo khóa
    end
```

- **Quy tắc nghiệp vụ:**
  - Thành viên BQT chỉ được cấp tài khoản qua Quản trị viên cấp cao (Admin).
  - Người dùng có thể chủ động truy cập trang cá nhân để đổi mật khẩu (nhập mật khẩu cũ, mật khẩu mới $\ge 8$ ký tự, xác nhận mật khẩu mới).

---

### 2.2. Luồng 2: Tạo & Cấu hình khoản thu (Fee Configuration & Campaign Creation)

```mermaid
flowchart TD
    Start([Bắt đầu: Kế toán / BQT chọn tạo khoản thu]) --> CheckType{Phân loại khoản thu}
    
    CheckType -->|Khoản phí định kỳ bắt buộc| Periodic[Chọn: Phí dịch vụ hoặc Phí quản lý]
    Periodic --> InputRate[Nhập đơn giá VNĐ / m2 / tháng<br>Phí DV: 2.500 - 16.500 đ<br>Phí QL: >= 7.000 đ]
    InputRate --> SetCycle[Thiết lập chu kỳ áp dụng: Tháng/Năm]
    SetCycle --> SaveFee[Lưu cấu hình biểu phí vào DB]
    SaveFee --> GenBill[Hệ thống tự động quét diện tích từng căn hộ<br>Phát sinh bảng kê công nợ tháng mới]

    CheckType -->|Khoản đóng góp tự nguyện| Campaign[Tạo chiến dịch vận động quyên góp]
    Campaign --> InputCamp[Nhập Tên quỹ: Vì người nghèo, Biển đảo...<br>Ngày bắt đầu - Ngày kết thúc - Mục đích]
    InputCamp --> ActivateCamp[Kích hoạt đợt vận động trên toàn hệ thống]
    ActivateCamp --> ReadyCollect[Sẵn sàng ghi nhận ủng hộ tại quầy thu ngân]
    
    GenBill --> End([Hoàn tất])
    ReadyCollect --> End
```

---

### 2.3. Luồng 3: Thu phí & Phát hành biên lai (Payment & Receipt Issuance)

```mermaid
sequenceDiagram
    autonumber
    actor Resident as Cư dân / Người nộp
    actor Cashier as Thủ quỹ BQT
    participant System as Phần mềm BlueMoon
    participant DB as Cơ sở dữ liệu

    Resident->>Cashier: Đọc số phòng (ví dụ: P1204) và nộp tiền
    Cashier->>System: Tìm kiếm mã phòng "P1204"
    System->>DB: Truy vấn công nợ phí định kỳ & các quỹ đang mở
    DB-->>System: Trả về danh sách nợ phí & đợt vận động
    System-->>Cashier: Hiển thị giao diện quầy thu ngân
    Cashier->>System: Tích chọn khoản phí cần đóng (Dịch vụ, Quản lý)
    Cashier->>System: Nhập số tiền ủng hộ quỹ tự nguyện (nếu có)
    Cashier->>System: Nhập tên người nộp thực tế & xác nhận thanh toán
    System->>DB: Bắt đầu giao dịch (Transaction):<br>1. Đổi trạng thái hóa đơn -> "ĐÃ THANH TOÁN"<br>2. Lưu bản ghi đóng góp quỹ<br>3. Tạo phiếu thu & sinh mã biên lai duy nhất
    DB-->>System: Cam kết giao dịch thành công (Commit)
    System-->>Cashier: Mở bản xem trước biên lai thu tiền
    Cashier->>System: Nhấn "In biên lai"
    System-->>Cashier: Xuất phiếu thu / In hóa đơn
    Cashier-->>Resident: Giao biên lai xác nhận đã nộp tiền
```

---

### 2.4. Luồng 4: Thống kê & Báo cáo các khoản đóng góp và thu phí (Reporting & Analytics)

```mermaid
flowchart TD
    StartReport([Bắt đầu: BQT / Kế toán mở module Báo cáo]) --> SelectMode{Chọn loại báo cáo}
    
    SelectMode -->|Báo cáo Phí định kỳ| Mode1[Chọn Tháng / Năm cần xem]
    Mode1 --> QueryDB1[(Truy vấn dữ liệu hóa đơn)]
    QueryDB1 --> Calc1[Tổng hợp:<br>- Tổng số căn hộ phải nộp<br>- Số căn hộ đã nộp vs còn nợ<br>- Tổng tiền thực thu vs dự thu<br>- Tỷ lệ hoàn thành %]
    Calc1 --> Display1[Hiển thị biểu đồ & bảng kê chi tiết từng tầng]
    
    SelectMode -->|Báo cáo Quỹ đóng góp tự nguyện| Mode2[Chọn Chiến dịch / Tên Quỹ vận động]
    Mode2 --> QueryDB2[(Truy vấn dữ liệu đóng góp)]
    QueryDB2 --> Calc2[Tổng hợp:<br>- Tổng số tiền quyên góp lũy kế<br>- Danh sách từng hộ, số tiền đã ủng hộ<br>- Thay thế hoàn toàn sổ tay Hình 1-1]
    Calc2 --> Display2[Hiển thị bảng danh sách ủng hộ minh bạch]

    Display1 --> Export[Hỗ trợ Xuất dữ liệu ra file Excel hoặc in báo cáo PDF]
    Display2 --> Export
    Export --> EndReport([Kết thúc])
```

---

## 3. Liên kết tài liệu yêu cầu chi tiết
- Để xem các kịch bản kiểm thử tương ứng cho từng luồng: [Tài liệu Scrum Artefacts & AC](./proposal/05_chi_tiet_us_va_acceptance_criteria.md).
- Để xem ma trận truy vết từ nguồn gốc yêu cầu: [Tài liệu Traceability Matrix](./proposal/07_traceability_matrix_va_quan_ly_rui_ro.md).
