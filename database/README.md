# QUẢN LÝ CƠ SỞ DỮ LIỆU DỰ ÁN BLUEMOON BFMS (DATABASE DIRECTORY)

Thư mục này là **"Single Source of Truth"** lưu trữ toàn bộ mã nguồn cấu trúc cơ sở dữ liệu (Database as Code) của dự án BlueMoon BFMS. Mọi thay đổi về bảng, cột, khóa ngoại và dữ liệu mẫu đều được lưu vết trong Git tại đây.

---

## 1. Cấu trúc thư mục

```text
database/
├── README.md               # Hướng dẫn quy trình phát triển và deploy DB
├── schema.sql              # Toàn bộ câu lệnh CREATE TABLE, INDEX, FOREIGN KEY chuẩn PostgreSQL
├── seed_data.sql           # Dữ liệu khởi tạo mẫu: 30 tầng căn hộ, tài khoản BQT mặc định, biểu phí mẫu
└── migrations/             # Các bản script bổ sung theo từng Sprint (nếu có)
```

---

## 2. Quy trình làm việc Song song giữa Dev và Test Deploy

```text
+-------------------+        +--------------------+        +---------------------+
| 1. Local Dev      |        | 2. Script Version  |        | 3. Cloud / Staging  |
| Mỗi bạn chạy DB   | -----> | Xuất thay đổi vào  | -----> | Deploy lên Google   |
| riêng (Docker/H2) |        | database/schema.sql|        | Cloud qua DDL review|
| ddl-auto: update  |        | Commit lên Git     |        | ddl-auto: validate  |
+-------------------+        +--------------------+        +---------------------+
```

1. **Khi phát triển chức năng mới (Local Dev):**
   - Viết các JPA Entity trong gói `com.bluemoon.bfms.model`.
   - Chạy trên database local cá nhân với cấu hình `ddl-auto: update`.
2. **Khi chốt chức năng và chuẩn bị tạo Pull Request:**
   - Cập nhật các câu lệnh DDL tương ứng vào file [database/schema.sql](./schema.sql).
   - Nếu có dữ liệu mẫu mới (ví dụ loại phí mới), bổ sung vào [database/seed_data.sql](./seed_data.sql).
3. **Cập nhật lên Database Google Cloud:**
   - Người phụ trách Database (hoặc Tech Lead) kết nối tới PostgreSQL trên Google Cloud (bằng DBeaver / pgAdmin / psql) và thực thi script đã được review qua Pull Request.
   - Nhờ đó, Database Cloud luôn sạch sẽ, không bị lỗi schema và luôn sẵn sàng cho Frontend test và demo nghiệm thu.

---

## 3. Khởi tạo Database nhanh trên Google Cloud hoặc Local

Nếu bạn vừa tạo một database PostgreSQL mới tinh (`bluemoon`), hãy chạy 2 lệnh sau:

```bash
# 1. Tạo cấu trúc bảng
psql -h <HOST> -p 5432 -U <USERNAME> -d bluemoon -f database/schema.sql

# 2. Nạp dữ liệu mẫu ban đầu
psql -h <HOST> -p 5432 -U <USERNAME> -d bluemoon -f database/seed_data.sql
```
*(Nếu dùng DBeaver hoặc pgAdmin, chỉ cần mở 2 tệp trên và nhấn Execute Script).*
