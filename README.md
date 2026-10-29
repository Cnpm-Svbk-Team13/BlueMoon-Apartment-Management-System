# BlueMoon BFMS

**BlueMoon BFMS** (BlueMoon Facility Management System) là phần mềm web hỗ trợ quản lý thu phí và vận hành chung cư, phục vụ Ban quản trị, bộ phận Kế toán và cư dân.

> **Trạng thái dự án:** Khung xương mã nguồn (Scaffold), tài liệu đặc tả (SRS, Proposals, Workflows) và cấu hình môi trường đã được hoàn tất trên nền tảng **Java 25 LTS**, **Spring Boot 3.4.5** và **PostgreSQL**.

## Mục tiêu

- Cung cấp **Trang Quản trị** để quản lý căn hộ, cư dân, khoản phí, hóa đơn và báo cáo.
- Cung cấp **Cổng Cư dân** để tra cứu công nợ/hóa đơn và gửi phản ánh (ticket).
- Tập trung nghiệp vụ và dữ liệu trong một ứng dụng web có phân tầng rõ ràng (Controller → Service → Repository).

## Công nghệ nền tảng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java 25 LTS (tương thích JDK 21+) |
| Framework Backend | Spring Boot 3.4.5, Spring MVC |
| Giao diện máy chủ | Thymeleaf |
| Xác thực & Bảo mật | Spring Security (RBAC, BCrypt) |
| Persistence | Spring Data JPA / Hibernate |
| Cơ sở dữ liệu | PostgreSQL 16/17 (Google Cloud Console / Docker) |
| Kiểm thử | JUnit 5, Mockito, H2 In-Memory Database |
| Build & Quản lý thư viện | Maven Wrapper (Apache Maven 3.9.9) |

## Kiến trúc Hệ thống

```text
Admin Panel ───────┐
                   ├── HTTP/HTTPS ──► Spring Boot / Spring MVC
Cổng Cư dân ───────┘                            │
                                                ▼
                                          Service (nghiệp vụ)
                                                │
                                                ▼
                                        Repository / JPA
                                                │
                                                ▼
                                 PostgreSQL (Google Cloud / Local)
```

Ứng dụng theo **Pure Web Architecture** và **Layered Architecture**. Trình duyệt chỉ giao tiếp với Spring Boot; không truy cập database trực tiếp. Chi tiết kiến trúc xem trong [BLUEMOON.md](./BLUEMOON.md).

## Trạng thái triển khai

- [x] Thống nhất công nghệ nền tảng (Java 25 LTS, Spring Boot 3.4.5, PostgreSQL).
- [x] Tạo cấu trúc Maven chuẩn, Maven Wrapper và Spring Boot application scaffold.
- [x] Cấu hình DataSource linh hoạt hỗ trợ biến môi trường Google Cloud và Docker Compose.
- [x] Tích hợp cấu hình kiểm thử độc lập với H2 In-Memory Database.
- [ ] Xây dựng Database Schema và migration scripts cho 30 tầng căn hộ & cư dân.
- [ ] Phát triển các luồng nghiệp vụ Quản trị (Admin) và Cổng Cư dân (Portal).
- [ ] Bổ sung kiểm thử tự động (Unit Test / Integration Test) và hoàn thiện giao diện.

## Bắt đầu phát triển

### 1. Yêu cầu môi trường

- JDK 25 LTS (hoặc JDK 21 trở lên).
- Maven Wrapper (`mvnw` / `mvnw.cmd`) có sẵn trong repository.
- Cơ sở dữ liệu PostgreSQL (kết nối tới Google Cloud instance của nhóm hoặc chạy qua Docker).

### 2. Cấu hình biến môi trường

Copy tệp [`.env.example`](./.env.example) và cấu hình thông tin kết nối database (xem chi tiết tại [docs/local-development.md](./docs/local-development.md)):

```bash
DB_URL=jdbc:postgresql://<GCP_HOST>:5432/bluemoon?sslmode=prefer
DB_USERNAME=bluemoon_admin
DB_PASSWORD=your_password
```

### 3. Chạy ứng dụng

Chạy từ thư mục gốc dự án bằng Maven Wrapper:

```powershell
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

Khi ứng dụng khởi động thành công, truy cập tại: `http://localhost:8080/`.

### 4. Chạy kiểm thử

```powershell
.\mvnw.cmd test
```

## Tài liệu Dự án

- [docs/local-development.md](./docs/local-development.md) — Hướng dẫn chi tiết thiết lập môi trường, kết nối Google Cloud PostgreSQL và quy trình phát triển.
- [docs/proposal/](./docs/proposal/README.md) — Trọn bộ hồ sơ đề xuất & khai phá yêu cầu chính thức của **Team 13** (biên dịch đầy đủ từ Google Docs `CNPM_Proposal_Team13`, Google Sheets `IT3180_BTL_Team13`, `CNPM_Team13_Epic-US`).
- [BLUEMOON.md](./BLUEMOON.md) — Tổng quan kỹ thuật, sơ đồ kiến trúc, phân tầng và coding guidelines.
- [docs/DE_BAI_GOC.md](./docs/DE_BAI_GOC.md) — Toàn văn đề bài gốc bài tập lớn môn Công nghệ phần mềm và phân tích ảnh mẫu phiếu thu viết tay Hình 1-1.
- [docs/SRS.md](./docs/SRS.md) — Tài liệu Đặc tả Yêu cầu Phần mềm (SRS) tổng quan.
- [docs/workflows.md](./docs/workflows.md) — Sơ đồ và mô tả chi tiết 4 luồng nghiệp vụ cốt lõi.
- [docs/erd.md](./docs/erd.md) — Mô hình thực thể liên kết (ERD) và cấu trúc dữ liệu.

## Đóng góp và Quy ước

Trước khi triển khai tính năng, hãy đọc [BLUEMOON.md](./BLUEMOON.md), tuân thủ phân tầng Controller → Service → Repository và cập nhật tests/tài liệu liên quan.
