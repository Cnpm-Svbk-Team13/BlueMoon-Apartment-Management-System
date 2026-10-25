# BlueMoon BFMS

**BlueMoon BFMS** (BlueMoon Facility Management System) là phần mềm web hỗ trợ quản lý thu phí và vận hành chung cư, phục vụ Ban quản trị, bộ phận Kế toán và cư dân.

> **Giai đoạn hiện tại:** khởi động dự án và thống nhất kiến trúc. Tài liệu đã được chuẩn bị; mã nguồn ứng dụng và Maven build scaffold chưa được thêm vào repository.

## Mục tiêu

- Cung cấp **Trang Quản trị** để quản lý căn hộ, cư dân, khoản phí, hóa đơn và báo cáo.
- Cung cấp **Cổng Cư dân** để tra cứu công nợ/hóa đơn và gửi phản ánh (ticket).
- Tập trung nghiệp vụ và dữ liệu trong một ứng dụng web có phân tầng rõ ràng.

## Công nghệ dự kiến

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java |
| Backend/Web | Spring Boot, Spring MVC |
| Giao diện máy chủ | Thymeleaf |
| Persistence | Spring Data JPA / Hibernate, JDBC |
| Database giai đoạn đầu | SQLite (`database/bluemoon.db`) |
| Build và quản lý thư viện | Maven |

## Kiến trúc

```text
Admin Panel ───────┐
                   ├── HTTP ──► Spring MVC Controller
Cổng Cư dân ───────┘                    │
                                        ▼
                                  Service (nghiệp vụ)
                                        │
                                        ▼
                                Repository / JPA
                                        │
                                        ▼
                             SQLite: database/bluemoon.db
```

Ứng dụng theo **Pure Web Architecture** và **Layered Architecture**. Trình duyệt chỉ giao tiếp với Spring Boot; không truy cập database trực tiếp. Chi tiết kiến trúc và cấu trúc thư mục xem trong [BLUEMOON.md](./BLUEMOON.md).

## Trạng thái khởi động

- [x] Thống nhất công nghệ nền tảng và database phát triển.
- [x] Đề xuất cấu trúc Maven và phân chia các tầng.
- [ ] Tạo Maven Wrapper, `pom.xml` và Spring Boot application scaffold.
- [ ] Cấu hình SQLite JDBC/JPA và kiểm tra kết nối database.
- [ ] Phát triển các luồng nghiệp vụ Admin và Cổng Cư dân.
- [ ] Bổ sung kiểm thử và hướng dẫn vận hành.

Các mục trạng thái phản ánh giai đoạn hiện tại và cần được cập nhật khi dự án có tiến triển.

## Bắt đầu phát triển

### Yêu cầu

- JDK 17 trở lên (khuyến nghị JDK 21 LTS).
- Maven 3.9 trở lên hoặc Maven Wrapper khi được thêm vào repository.
- Không cần cài SQLite server; SQLite sử dụng file local.

### Chạy ứng dụng

Maven project chưa được khởi tạo trong repository. Sau khi có `pom.xml` và Spring Boot application, chạy từ thư mục gốc dự án:

```bash
mvn spring-boot:run
```

Hoặc dùng Maven Wrapper:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

Khi ứng dụng khởi động, địa chỉ mặc định dự kiến là `http://localhost:8080/`. Cấu hình SQLite và hướng dẫn thiết lập đầy đủ nằm trong [BLUEMOON.md](./BLUEMOON.md).

## Tài liệu

- [BLUEMOON.md](./BLUEMOON.md) — tổng quan kỹ thuật, sơ đồ kiến trúc, cây thư mục, cấu hình SQLite, hướng dẫn chạy và coding guidelines.
- [docs/requirements/](./docs/requirements/README.md) — bộ hồ sơ khai phá và đặc tả yêu cầu chi tiết (Wish List, As-Is/To-Be Scenarios, Scrum Artefacts, Product Backlog, Traceability Matrix).
- [docs/SRS.md](./docs/SRS.md) — tài liệu Đặc tả Yêu cầu Phần mềm (SRS) tổng quan.
- [docs/workflows.md](./docs/workflows.md) — sơ đồ và mô tả chi tiết 4 luồng nghiệp vụ cốt lõi.
- `docs/design/` — sơ đồ thiết kế, ERD và luồng nghiệp vụ (sẽ bổ sung).

## Đóng góp

Trước khi triển khai một tính năng, hãy đọc [BLUEMOON.md](./BLUEMOON.md), tuân thủ phân tầng Controller → Service → Repository và cập nhật tests/tài liệu liên quan. Ưu tiên thay đổi nhỏ, có thể kiểm chứng và không đưa dữ liệu cư dân hoặc thông tin nhạy cảm vào repository.
