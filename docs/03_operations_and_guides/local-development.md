[🏠 Trang chủ Docs](../README.md) > [Giai đoạn 3: Vận hành & CSDL](./README.md) > **local-development.md**

---

# HƯỚNG DẪN PHÁT TRIỂN CỤC BỘ (LOCAL DEVELOPMENT GUIDE)
## Hệ thống Quản lý Thu phí và Dân cư Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Yêu cầu Tiền đề (Prerequisites)
- **Java Development Kit (JDK):** Phiên bản 25 LTS (hoặc tối thiểu JDK 21 trở lên). Kiểm tra bằng lệnh:
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
Mở terminal tại thư mục gốc của dự án và chạy:
```bash
docker compose up -d
```
Lệnh này sẽ tải và khởi động container PostgreSQL 17 với thông số:
- **Tên container:** `bluemoon-postgres`
- **Port:** `5432`
- **Database:** `bluemoon`
- **Username:** `postgres`
- **Password:** `postgres`

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
| `DB_USERNAME` | `postgres` | Tài khoản quản trị database |
| `DB_PASSWORD` | `postgres` | Mật khẩu database |

Trên Windows PowerShell, bạn có thể thiết lập tạm thời cho phiên làm việc hiện tại:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/bluemoon"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
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

Sau khi ứng dụng khởi động thành công (thông báo *Started BlueMoonBfmsApplication*), mở trình duyệt và truy cập:
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
