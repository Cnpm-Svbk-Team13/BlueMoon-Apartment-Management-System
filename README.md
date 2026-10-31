# BlueMoon BFMS

**BlueMoon BFMS** (BlueMoon Facility Management System) là phần mềm web hỗ trợ quản lý thu phí và vận hành chung cư, phục vụ Ban quản trị, bộ phận Kế toán và cư dân.

> **Trạng thái dự án:** Khung xương mã nguồn (Scaffold), tài liệu đặc tả (SRS, Proposals, Workflows) và cấu hình môi trường đã được hoàn tất trên nền tảng **Java 25 LTS**, **Spring Boot 3.5.16** và **PostgreSQL**.

## Mục tiêu

- Cung cấp **Trang Quản trị** để quản lý căn hộ, cư dân, khoản phí, hóa đơn và báo cáo.
- Định hướng v2.0: cung cấp **Cổng Cư dân** để tra cứu công nợ/hóa đơn và gửi phản ánh (ticket).
- Tập trung nghiệp vụ và dữ liệu trong một ứng dụng web có phân tầng rõ ràng (Controller → Service → Repository).

## Công nghệ nền tảng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java 25 LTS |
| Framework Backend | Spring Boot 3.5.16, Spring MVC |
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

- [x] Thống nhất công nghệ nền tảng (Java 25 LTS, Spring Boot 3.5.16, PostgreSQL).
- [x] Tạo cấu trúc Maven chuẩn, Maven Wrapper và Spring Boot application scaffold.
- [x] Cấu hình DataSource linh hoạt hỗ trợ biến môi trường Google Cloud và Docker Compose.
- [x] Tích hợp cấu hình kiểm thử độc lập với H2 In-Memory Database.
- [x] Soạn Database Schema và migration scripts (chưa xác nhận chạy PostgreSQL thực tế) cho 30 tầng căn hộ & cư dân.
- [ ] Phát triển các luồng nghiệp vụ Quản trị (Admin); Portal để v2.0.
- [ ] Bổ sung kiểm thử tự động (Unit Test / Integration Test) và hoàn thiện giao diện.

## Bắt đầu phát triển

### 1. Yêu cầu môi trường

- JDK 25 LTS.
- Maven Wrapper (`mvnw` / `mvnw.cmd`) có sẵn trong repository.
- Cơ sở dữ liệu PostgreSQL (kết nối tới Google Cloud instance của nhóm hoặc chạy qua Docker).

### 2. Cấu hình biến môi trường

Copy tệp [`.env.example`](./.env.example) và cấu hình thông tin kết nối database (xem chi tiết tại [docs/local-development.md](./docs/03_operations_and_guides/local-development.md)):

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

Hệ thống tài liệu được tổ chức chuẩn mực theo 3 giai đoạn của vòng đời phần mềm (xem chi tiết tại **[docs/README.md](./docs/README.md)**):
- **[docs/README.md](./docs/README.md)** — **Cổng Điều Hướng Trung Tâm** (Master Navigation Hub tra cứu toàn bộ hồ sơ đồ án).
- **[docs/01_requirements_and_planning/](./docs/01_requirements_and_planning/README.md)** — Hồ sơ đề xuất, khai phá yêu cầu, 32 RAWs, 11 User Stories (AC Given-When-Then), WBS & Kế hoạch quản lý rủi ro chuẩn SoICT.
- **[docs/02_design_and_architecture/](./docs/02_design_and_architecture/README.md)** — Đề bài gốc, Đặc tả SRS, Kiến trúc phân tầng, Mô hình CSDL ERD 11 bảng, và 4 luồng nghiệp vụ Sequence.
- **[docs/03_operations_and_guides/](./docs/03_operations_and_guides/README.md)** — Hướng dẫn cài đặt local, kết nối PostgreSQL trên Google Cloud Console, Docker và chạy test độc lập.
- **[BLUEMOON.md](./BLUEMOON.md)** — Tổng quan kỹ thuật, phân tầng trách nhiệm và coding guidelines cho dev & AI agents.
- **[database/README.md](./database/README.md)** — Quy trình quản lý CSDL (Database as Code), scripts DDL schema và dữ liệu mẫu.

## Đóng góp và Quy ước

Trước khi triển khai tính năng, hãy đọc [BLUEMOON.md](./BLUEMOON.md), tuân thủ phân tầng Controller → Service → Repository và cập nhật tests/tài liệu liên quan.

## Quy chuẩn và kiểm tra Sprint 1

Xem [quy chuẩn hiện hành](docs/PROJECT_CONVENTIONS.md) về vai trò, truy vết, review chéo Sơn/Hoà, CI, coverage và ba skill chia sẻ. Smoke test H2 không chứng minh hoàn thành chức năng. Nghiệm thu Sprint chạy `verify -Psprint1-quality`, có >=85% line coverage Service/Controller và UAT.
