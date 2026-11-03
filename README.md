# BlueMoon BFMS

**BlueMoon BFMS** (BlueMoon Facility Management System) là phần mềm web hỗ trợ quản lý thu phí và vận hành chung cư, phục vụ Ban quản trị, bộ phận Kế toán và cư dân.

> **Trạng thái dự án:** Khung xương mã nguồn (Scaffold), tài liệu đặc tả (SRS, Proposals, Workflows) và cấu hình môi trường đã được hoàn tất trên nền tảng **Java 25 LTS**, **Spring Boot 3.5.16** và **PostgreSQL 17** (Cloud & Docker).

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
| Cơ sở dữ liệu | PostgreSQL 17 (Google Cloud Platform / Docker) |
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

- [x] Thống nhất công nghệ nền tảng (Java 25 LTS, Spring Boot 3.5.16, PostgreSQL 17).
- [x] Tạo cấu trúc Maven chuẩn, Maven Wrapper và Spring Boot application scaffold.
- [x] Cấu hình DataSource linh hoạt hỗ trợ biến môi trường Google Cloud và Docker Compose.
- [x] Tích hợp cấu hình kiểm thử độc lập với H2 In-Memory Database.
- [x] Soạn schema/seed 7 bảng phục vụ Sprint 1; cách khởi tạo, kiểm tra và bàn giao xem [database/README.md](database/README.md). Việc nạp dữ liệu không chứng minh hoàn thành chức năng xác thực hoặc nghiệm thu US.
- [ ] Phát triển các luồng nghiệp vụ Quản trị (Admin); Portal để v2.0.
- [ ] Bổ sung kiểm thử tự động (Unit Test / Integration Test) và hoàn thiện giao diện.

## Bắt đầu phát triển

### 1. Yêu cầu môi trường

- **Java Development Kit:** JDK 25 LTS (chuẩn hóa toàn đội).
- **Maven Wrapper:** Đi kèm sẵn trong dự án (`mvnw` / `mvnw.cmd`), không bắt buộc cài Maven toàn cục.
- **Cơ sở dữ liệu (Chọn 1 trong 2 phương án):**
  - **Phương án A (Khuyên dùng cho toàn đội — KHÔNG CẦN CÀI DOCKER):** Kết nối trực tiếp vào máy chủ CSDL PostgreSQL 17 dùng chung của nhóm trên **Google Cloud Platform**. Thành viên không cần cài đặt Docker Desktop, tiết kiệm 4GB RAM cho máy tính.
  - **Phương án B (Dành cho ai muốn phát triển offline):** Cài đặt Docker Desktop và chạy `docker compose up -d` tại thư mục gốc để khởi động PostgreSQL 17 cục bộ.

### 2. Cấu hình biến môi trường (`.env`)

Sao chép tệp mẫu [`.env.example`](./.env.example) thành tệp `.env` tại thư mục gốc:

```bash
# Phương án A: Kết nối Google Cloud Database dùng chung của nhóm
DB_URL=jdbc:postgresql://<GCP_HOST_OR_IP>:5432/bluemoon?sslmode=prefer
DB_USERNAME=bluemoon_admin
DB_PASSWORD=your_secure_password_here
PORT=8080
```
*(Chi tiết thông số IP và mật khẩu Google Cloud được quản trị viên CSDL chia sẻ nội bộ qua kênh bảo mật của nhóm).*

### 3. Khởi động Ứng dụng 1-Click (1-Click Run)

Nhóm đã tích hợp sẵn bộ script tự động nạp file `.env` và khởi chạy Spring Boot:

- **Trên Windows PowerShell:**
  ```powershell
  .\run.ps1
  ```
- **Trên Windows Command Prompt (CMD):**
  ```cmd
  run
  ```
- **Trên Linux / macOS / Git Bash:**
  ```bash
  chmod +x run.sh && ./run.sh
  ```
- **Hoặc chạy bằng lệnh Maven Wrapper truyền thống:**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

Sau khi ứng dụng khởi động thành công, mở trình duyệt và truy cập: **`http://localhost:8080/`**.

### 4. Chạy kiểm thử tự động (Unit & Integration Tests)

Dự án đã cấu hình H2 In-Memory Database độc lập cho kiểm thử, không phụ thuộc vào kết nối mạng:

```powershell
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

## Tài liệu Dự án

Hệ thống tài liệu được tổ chức chuẩn mực theo 3 giai đoạn của vòng đời phần mềm (xem chi tiết tại **[docs/README.md](./docs/README.md)**):
- **[docs/README.md](./docs/README.md)** — **Cổng Điều Hướng Trung Tâm** (Master Navigation Hub tra cứu toàn bộ hồ sơ đồ án).
- **[docs/01_requirements_and_planning/](./docs/01_requirements_and_planning/README.md)** — Hồ sơ đề xuất, khai phá yêu cầu, 32 RAWs, 11 User Stories (AC Given-When-Then), WBS & Kế hoạch quản lý rủi ro chuẩn SoICT.
- **[docs/02_design_and_architecture/](./docs/02_design_and_architecture/README.md)** — Đề bài gốc, Đặc tả SRS, Kiến trúc phân tầng, Mô hình CSDL ERD 11 bảng, và 4 luồng nghiệp vụ Sequence.
- **[docs/03_operations_and_guides/](./docs/03_operations_and_guides/README.md)** — Hướng dẫn cài đặt local, kết nối PostgreSQL trên Google Cloud Console, Docker và chạy test độc lập.
- **[docs/03_operations_and_guides/google-cloud-database-guide.md](./docs/03_operations_and_guides/google-cloud-database-guide.md)** — Cẩm nang chi tiết triển khai và vận hành PostgreSQL trên Google Cloud.
- **[BLUEMOON.md](./BLUEMOON.md)** — Tổng quan kỹ thuật, phân tầng trách nhiệm và coding guidelines cho dev & AI agents.
- **[database/README.md](./database/README.md)** — Quy trình quản lý CSDL (Database as Code), scripts DDL schema và dữ liệu mẫu.

## Đóng góp và Quy ước

Trước khi triển khai tính năng, hãy đọc [BLUEMOON.md](./BLUEMOON.md), tuân thủ phân tầng Controller → Service → Repository và cập nhật tests/tài liệu liên quan.

## Quy chuẩn và kiểm tra Sprint 1

Xem [quy chuẩn hiện hành](docs/PROJECT_CONVENTIONS.md) về vai trò, truy vết, review chéo Sơn/Hoà, CI, coverage và ba skill chia sẻ. Smoke test H2 không chứng minh hoàn thành chức năng. Nghiệm thu Sprint chạy `verify -Psprint1-quality`, có >=85% line coverage Service/Controller và UAT.

## Bắt đầu công việc Sprint 1

[Đồ thị luồng công việc và bàn giao](docs/sprints/sprint-1/WORKFLOW_GUIDE.md) — xem tổng quan trước, mở luồng của mình rồi nhận phần việc 2–4h. Khánh điều phối Sprint/BA/UAT; Sơn DB/Backend, quyết định phạm vi và nghiệm thu. Tổng 138h/36 SP, 24 gói/44 phần việc.
