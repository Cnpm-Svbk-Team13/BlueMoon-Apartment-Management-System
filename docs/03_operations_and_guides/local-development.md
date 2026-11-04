[🏠 Trang chủ Docs](../README.md) > [Giai đoạn 3: Vận hành & CSDL](./README.md) > **local-development.md**

---

# HƯỚNG DẪN PHÁT TRIỂN CỤC BỘ (LOCAL DEVELOPMENT GUIDE)
## Hệ thống Quản lý Thu phí và Dân cư Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Yêu cầu Tiền đề (Prerequisites)
- **Java Development Kit (JDK):** Phiên bản 25 LTS. Kiểm tra bằng lệnh:
  ```bash
  java -version
  ```
- **Docker & Docker Compose:** Dùng để chạy máy chủ cơ sở dữ liệu PostgreSQL cục bộ.
  ```bash
  docker compose version
  ```
- **Maven:** Đi kèm sẵn trong dự án thông qua Maven Wrapper (`mvnw` trên Linux/macOS hoặc `mvnw.cmd` trên Windows). Không bắt buộc cài Maven toàn cục.
- **Git:** Quản lý mã nguồn.

---

## 2. Khởi động Cơ sở Dữ liệu PostgreSQL qua Docker Compose

Dự án đã cấu hình sẵn tập tin `docker-compose.yml` tại thư mục gốc.

### 2.1. Khởi động Container Database
Sao chép `.env.example` thành `.env`, đặt `POSTGRES_OWNER_PASSWORD`, `DB_PASSWORD` bằng hai mật khẩu khác nhau và không commit `.env`. Sau đó mở terminal tại thư mục gốc và chạy:
```bash
docker compose up -d
```
Lệnh này sẽ tải và khởi động container PostgreSQL 17 với thông số:
- **Tên container:** `bluemoon-postgres`
- **Port:** `5432`
- **Database:** `bluemoon`
- **Owner:** `bluemoon_owner` (chỉ bootstrap/quản trị)
- **App role:** `bluemoon_app` (tạo riêng, dùng cho Spring Boot)

Với volume local mới, tạo app role bằng lời nhắc mật khẩu rồi chạy ba script theo thứ tự:

```bash
docker exec -it bluemoon-postgres createuser -U bluemoon_owner --login --pwprompt bluemoon_app
docker exec -i bluemoon-postgres psql -U bluemoon_owner -d bluemoon -v ON_ERROR_STOP=1 < database/schema.sql
docker exec -i bluemoon-postgres psql -U bluemoon_owner -d bluemoon -v ON_ERROR_STOP=1 < database/permissions.sql
docker exec -i bluemoon-postgres psql -U bluemoon_owner -d bluemoon -v ON_ERROR_STOP=1 < database/seed_data.sql
```

Seed chỉ chạy khi ứng dụng đã dừng và cả bảy bảng rỗng. Việc đổi `POSTGRES_OWNER_*` không thay user trong volume đã khởi tạo trước đó; database cũ cần kế hoạch chuyển đổi riêng, không tự xóa volume có dữ liệu.

### 2.2. Kiểm tra Trạng thái Container
```bash
docker compose ps
```

### 2.3. Dừng Container khi không dùng
```bash
docker compose down
```
*(Dữ liệu CSDL được lưu bền vững trong Docker volume `postgres_data`).*

---

## 3. Cấu hình Biến Môi trường (Environment Variables)

Ứng dụng Spring Boot đọc thông tin kết nối CSDL từ `src/main/resources/application.yml` thông qua các biến môi trường:

| Tên biến môi trường | Giá trị mặc định khi chạy Local | Mô tả |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/bluemoon` | Địa chỉ JDBC tới CSDL PostgreSQL |
| `DB_USERNAME` | `bluemoon_app` | Role ứng dụng ít quyền, không phải owner |
| `DB_PASSWORD` | Không có mặc định | Mật khẩu lấy từ secret hoặc `.env` không commit |
| `JPA_DDL_AUTO` | `validate` | Chỉ xác minh Entity, không tự sửa schema |

Trên Windows PowerShell, bạn có thể thiết lập tạm thời cho phiên làm việc hiện tại:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/bluemoon"
$env:DB_USERNAME="bluemoon_app"
$env:DB_PASSWORD="<LOCAL_APP_PASSWORD>"
$env:JPA_DDL_AUTO="validate"
```

---

## 4. Biên dịch và Chạy Ứng dụng

### 4.1. Chạy Ứng dụng qua Maven Wrapper
- **Trên Windows PowerShell:**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
- **Trên Linux / macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```

Sau khi khởi động thành công, các URL mục tiêu dưới đây cần controller/security hoàn chỉnh mới dùng được; scaffold hiện chưa có các route nghiệp vụ:
- **Trang chủ / Cổng cư dân:** [http://localhost:8080/](http://localhost:8080/)
- **Trang Đăng nhập BQT:** [http://localhost:8080/login](http://localhost:8080/login)
- **Trang Quản trị (Admin Dashboard):** [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard)

### 4.2. Chạy Kiểm thử Tự động (Unit & Integration Tests)
```powershell
.\mvnw.cmd test
```

### 4.3. Đóng gói Ứng dụng thành file JAR
```powershell
.\mvnw.cmd clean package -DskipTests
```
File JAR thực thi sẽ nằm trong thư mục `target/bluemoon-bfms-0.0.1-SNAPSHOT.jar`. Để chạy:
```bash
java -jar target/bluemoon-bfms-0.0.1-SNAPSHOT.jar
```

---

## 5. Sao lưu và Khôi phục Cơ sở Dữ liệu (Backup & Restore)

### 5.1. Sao lưu dữ liệu ra file SQL
```bash
docker exec -t bluemoon-postgres pg_dump -U postgres bluemoon > backup_bluemoon.sql
```

### 5.2. Khôi phục dữ liệu từ file SQL
```bash
docker exec -i bluemoon-postgres psql -U postgres -d bluemoon < backup_bluemoon.sql
```

## Kiểm tra và skills Sprint 1

Xem [quy chuẩn](../PROJECT_CONVENTIONS.md). Chạy `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify` để build, smoke test H2 và tạo báo cáo JaCoCo. Nghiệm thu chạy thêm `-Psprint1-quality`, kiểm tra >=85% line coverage gộp Service/Controller và đầy đủ UAT. Smoke test không cần PostgreSQL; test schema/migration vẫn cần PostgreSQL thật cô lập. Mockito được nạp sẵn bằng javaagent cùng JaCoCo để tránh tự attach khi test trên Java 25.

Spring Boot hiện không tự nạp file `.env`; đặt biến môi trường qua PowerShell/IDE. Ba skill repo ở `.agents/skills/`, kéo Git rồi mở phiên Codex mới nếu chưa thấy. Không tự thực thi migration hoặc tạo issue trên GitHub.
