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
├── local-development.md           # Hướng dẫn thiết lập môi trường, kết nối GCP, Docker & Run tests
└── google-cloud-database-guide.md # Cẩm nang chi tiết triển khai PostgreSQL trên Google Cloud VM
```

### Các tệp cấu hình và mã nguồn CSDL liên quan:
- [`.env.example`](../../.env.example): Tệp mẫu biến môi trường kết nối database an toàn.
- [`docker-compose.yml`](../../docker-compose.yml): Cấu hình 1-click khởi chạy PostgreSQL 17 cục bộ qua Docker.
- [`database/README.md`](../../database/README.md): Quy trình phát triển CSDL song song giữa Dev và Test Deploy.
- [`database/schema.sql`](../../database/schema.sql): Script DDL khởi tạo toàn bộ 11 bảng và chỉ mục PostgreSQL.
- [`database/seed_data.sql`](../../database/seed_data.sql): Dữ liệu khởi tạo mẫu (30 tầng căn hộ, tài khoản Admin/Kế toán, biểu phí mẫu).

---

## 3. Tóm tắt Hướng dẫn Nhanh

| Mục tiêu | Công cụ / Lệnh | Tài liệu chi tiết |
|---|---|---|
| **Chạy kết nối Google Cloud** | Cấu hình `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` trong IDE hoặc PowerShell | [google-cloud-database-guide.md](./google-cloud-database-guide.md) & [local-development.md](./local-development.md#2-thiết-lập-cơ-sở-dữ-liệu-database-setup) |
| **Chạy Docker Postgres Local** | `docker compose up -d` | [Mục 2 trong local-development.md](./local-development.md#phương-án-b-chạy-postgresql-cục-bộ-bằng-docker-offline-development) |
| **Khởi động ứng dụng Spring Boot** | `.\mvnw.cmd spring-boot:run` (truy cập `http://localhost:8080/`) | [Mục 4 trong local-development.md](./local-development.md#4-chạy-và-kiểm-thử-ứng-dụng) |
| **Chạy kiểm thử độc lập (H2)** | `.\mvnw.cmd test` | [Mục 4 trong local-development.md](./local-development.md#chạy-kiểm-thử-độc-lập-unit--integration-tests) |
| **Quy trình cập nhật Schema CSDL** | Chỉnh sửa DDL $\rightarrow$ `database/schema.sql` $\rightarrow$ Review PR $\rightarrow$ Deploy Cloud | [database/README.md](../../database/README.md) |
