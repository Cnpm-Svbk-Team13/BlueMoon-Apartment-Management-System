[🏠 Trang chủ Docs](../README.md) > **Giai đoạn 3: Vận hành, Môi trường & Cơ sở Dữ liệu**

---

# VẬN HÀNH, MÔI TRƯỜNG & CƠ SỞ DỮ LIỆU - BLUEMOON BFMS
## Giai đoạn 3: Environment Setup, Cloud DB & Database Lifecycle

---

## 1. Giới thiệu Giai đoạn 3

Giai đoạn 3 cung cấp toàn bộ hướng dẫn kỹ thuật để thiết lập môi trường phát triển cục bộ, kết nối máy chủ cơ sở dữ liệu trên Google Cloud Console, chạy container Docker, thực hiện kiểm thử tự động độc lập và quản lý vòng đời cơ sở dữ liệu (Database as Code).

---

## 2. Danh mục Tài liệu & Công cụ Vận hành

```text
docs/03_operations_and_guides/
├── README.md                      # Tổng quan và hướng dẫn điều hướng Giai đoạn 3
├── local-development.md           # PostgreSQL local, Spring Boot, demo và kiểm thử
└── google-cloud-database-guide.md # Cẩm nang chi tiết triển khai PostgreSQL trên Google Cloud VM
```

### Các tệp cấu hình và mã nguồn CSDL liên quan:
- [`.env.example`](../../.env.example): Tệp mẫu biến môi trường kết nối database an toàn.
- [`docker-compose.yml`](../../docker-compose.yml): Cấu hình 1-click khởi chạy PostgreSQL 17 cục bộ qua Docker.
- [`database/README.md`](../../database/README.md): Quy trình phát triển CSDL song song giữa Dev và Test Deploy.
- [`database/schema.sql`](../../database/schema.sql): Bootstrap khởi tạo 7 bảng Sprint 1 cùng ràng buộc/index/trigger trên PostgreSQL trống; xem database/README.md trước khi nạp.
- [`database/seed_data.sql`](../../database/seed_data.sql): Dữ liệu khởi tạo mẫu (30 tầng căn hộ, tài khoản Admin/Kế toán, biểu phí mẫu).

---

## 3. Tóm tắt Hướng dẫn Nhanh

| Mục tiêu | Công cụ / Lệnh | Tài liệu chi tiết |
|---|---|---|
| **Kết nối Google Cloud** | Cấu hình JDBC do nhóm cấp | [Hướng dẫn Google Cloud](./google-cloud-database-guide.md) |
| **Chạy PostgreSQL local** | Tạo `.env`, khởi động Compose và bootstrap database mới | [Khởi động PostgreSQL](./local-development.md#2-khởi-động-postgresql) |
| **Chạy ứng dụng và demo** | `.\mvnw.cmd spring-boot:run` và mở `/admin/apartments` | [Chạy trên trình duyệt](./local-development.md#3-chạy-và-thử-trên-trình-duyệt) |
| **Chạy kiểm thử độc lập** | Maven Wrapper với profile kiểm thử H2 | [Kiểm thử và đóng gói](./local-development.md#5-kiểm-thử-đóng-gói-và-sao-lưu) |
| **Quy trình cập nhật Schema CSDL** | Chỉnh sửa DDL $\rightarrow$ `database/schema.sql` $\rightarrow$ Review PR $\rightarrow$ Deploy Cloud | [database/README.md](../../database/README.md) |
