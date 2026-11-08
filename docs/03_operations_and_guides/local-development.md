# Chạy BlueMoon trên localhost

Mỗi thành viên chạy ứng dụng và PostgreSQL trên máy mình với dữ liệu mẫu. Cài JDK 25, mở Docker Desktop và chạy các lệnh từ thư mục gốc dự án. Maven Wrapper đi kèm repo. [Hướng dẫn Google Cloud](google-cloud-database-guide.md)

## 1. Tạo cấu hình local

Nếu chưa có `.env`, sao chép mẫu:

```powershell
Copy-Item .env.example .env
```

Nếu đã có `.env`, đối chiếu từng khóa với mẫu và giữ cặp tài khoản PostgreSQL đang sử dụng. Spring Boot đọc `.env` như file Java properties. Dùng tên có dấu chấm cho các khóa Spring và khởi chạy từ thư mục gốc.

| Khóa | Dùng để làm gì |
| --- | --- |
| `POSTGRES_OWNER_USERNAME`, `POSTGRES_OWNER_PASSWORD` | Tài khoản owner bootstrap PostgreSQL. Volume mới mặc định dùng `bluemoon_owner`. |
| `DB_USERNAME`, `DB_PASSWORD` | Tài khoản ứng dụng `bluemoon_app` và mật khẩu đặt khi tạo role. |
| `spring.security.user.name`, `spring.security.user.password` | Tài khoản demo trên trình duyệt. Dùng username `admin` và mật khẩu demo riêng. |
| `POSTGRES_LOCAL_PORT`, `DB_URL` | Cổng PostgreSQL và URL JDBC. Nếu đổi cổng sang `15432`, dùng `jdbc:postgresql://127.0.0.1:15432/bluemoon`. |
| `PORT`, `server.address` | Cổng HTTP, mặc định `8080`, và địa chỉ loopback `127.0.0.1`. |
| `JPA_DDL_AUTO` | Dùng `validate` để Hibernate kiểm tra schema. |

Thay cả ba placeholder bằng mật khẩu khác nhau. Dùng chuỗi chữ và số dài để tránh ký tự được Compose hoặc Java properties diễn giải. Giữ mật khẩu trong `.env` local đã được Git bỏ qua. Biến môi trường từ terminal hoặc IDE được ưu tiên hơn `.env`. Mở terminal mới hoặc xóa các giá trị ghi đè cũ trong cấu hình Run của IDE nếu gặp sai lệch.

## 2. Khởi động PostgreSQL

```powershell
docker compose config --quiet
docker compose up -d
docker compose ps
```

### Database mới

Đợi PostgreSQL sẵn sàng trước khi tạo role. Các lệnh dưới đây dùng owner mặc định từ mẫu. Nếu đặt owner khác, thay `bluemoon_owner` trong các lệnh bằng tên đó.

```powershell
docker compose exec postgres pg_isready -U bluemoon_owner -d bluemoon
docker compose exec postgres createuser -U bluemoon_owner --login --pwprompt bluemoon_app
docker compose cp database/schema.sql postgres:/tmp/bluemoon-schema.sql
docker compose cp database/permissions.sql postgres:/tmp/bluemoon-permissions.sql
docker compose cp database/seed_data.sql postgres:/tmp/bluemoon-seed.sql
docker compose exec -T postgres psql -X -U bluemoon_owner -d bluemoon -v ON_ERROR_STOP=1 -f /tmp/bluemoon-schema.sql
docker compose exec -T postgres psql -X -U bluemoon_owner -d bluemoon -v ON_ERROR_STOP=1 -f /tmp/bluemoon-permissions.sql
docker compose exec -T postgres psql -X -U bluemoon_owner -d bluemoon -v ON_ERROR_STOP=1 -f /tmp/bluemoon-seed.sql
```

Ở lời nhắc của `createuser`, nhập đúng mật khẩu bạn đặt cho `DB_PASSWORD`. Chỉ chuyển sang lệnh tiếp theo khi lệnh trước thành công. Dùng `docker compose cp` và `psql -f` để giữ nguyên UTF-8 của file SQL trên cả PowerShell và Bash.

### Database đã có

Dùng lại volume, tên owner và mật khẩu đã được thiết lập lúc khởi tạo. Giá trị `POSTGRES_OWNER_*` trong Compose chỉ áp dụng cho quá trình khởi tạo volume mới. Nếu app role đã có, kiểm tra bằng mật khẩu hiện tại. Bootstrap chỉ dùng cho database trống. Seed chỉ chạy khi ứng dụng đã dừng và cả bảy bảng rỗng. [Quy trình database](../../database/README.md)

Kiểm tra bằng kết nối TCP để PostgreSQL thực sự xác thực mật khẩu app role:

```powershell
docker compose exec postgres psql -X -h 127.0.0.1 -U bluemoon_app -d bluemoon -W -v ON_ERROR_STOP=1 -c "SELECT current_user, count(*) FROM apartments;"
```

Kết quả phải hiện `bluemoon_app`. Sau đó kiểm tra phiên bản schema bằng lần khởi động Hibernate ở bước 3. Khi cần dừng database, `docker compose stop postgres` giữ lại container và volume.

## 3. Chạy và thử trên trình duyệt

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
sh ./mvnw spring-boot:run
```

Mở [danh mục căn hộ](http://127.0.0.1:8080/admin/apartments). Nếu đã đổi `PORT`, thay cổng trong URL. Spring Security sẽ chuyển tới `/login`. Đăng nhập bằng `admin` và mật khẩu ở `spring.security.user.password`. Đây là tài khoản demo local của Spring Security. Phần đăng nhập bằng tài khoản database do Hoà tích hợp.

- Kiểm tra danh sách và bộ lọc tầng.
- Mở **Create apartment** hoặc **Edit** trên dữ liệu mẫu.
- Mở **Household** của một căn để xem hồ sơ chủ hộ và lịch sử.

## 4. Xử lý lỗi thường gặp

| Triệu chứng | Kiểm tra |
| --- | --- |
| Cổng đã được sử dụng | Đổi `POSTGRES_LOCAL_PORT` cùng cổng trong `DB_URL`, hoặc đổi `PORT` nếu xung đột ở ứng dụng. |
| `password authentication failed` | Kiểm tra đúng cặp `DB_USERNAME` và `DB_PASSWORD` của volume đang chạy. Thử lệnh TCP ở mục 2. |
| Đăng nhập thất bại | Kiểm tra mật khẩu demo, thư mục chạy và cấu hình ghi đè từ terminal/IDE. Khởi động lại ứng dụng sau khi sửa `.env`. |
| `403` khi mở danh mục | Mở cửa sổ ẩn danh mới, đăng nhập bằng `admin` và kiểm tra database local có dòng `admin` đang hoạt động với quyền `ROLE_ADMIN`. |
| `Schema-validation` | Kiểm tra URL trỏ đúng database, phiên bản schema và quyền app role. Dùng quy trình chuyển đổi riêng cho database đã có dữ liệu. |

Kiểm tra dòng demo mà không truy xuất mật khẩu:

```powershell
docker compose exec postgres psql -X -h 127.0.0.1 -U bluemoon_app -d bluemoon -W -c "SELECT username, role, is_active FROM users WHERE username = 'admin';"
```

## 5. Kiểm thử, đóng gói và sao lưu

Nếu đang chạy file JAR trên Windows, dừng ứng dụng trước khi chạy `clean` để Maven có thể ghi lại file.

```powershell
.\mvnw.cmd --batch-mode --no-transfer-progress clean verify
.\mvnw.cmd --batch-mode --no-transfer-progress clean verify -Psprint1-quality
java -jar target/bluemoon-bfms-0.0.1-SNAPSHOT.jar
```

Bộ test H2/MockMvc chạy độc lập với PostgreSQL. Khi nghiệm thu Sprint, cổng chất lượng yêu cầu độ bao phủ dòng Service + Controller từ 85% và bằng chứng kiểm thử nghiệp vụ, PostgreSQL, UAT. [Quy chuẩn dự án](../PROJECT_CONVENTIONS.md) · [Hướng dẫn kiểm thử PostgreSQL](../sprints/sprint-1/SON_BACKEND_HANDOFF.md#verification)

Để sao lưu bằng owner mặc định, ghi file trong container rồi sao chép ra máy.

```powershell
docker compose exec -T postgres pg_dump -U bluemoon_owner -d bluemoon -f /tmp/bluemoon-backup.sql
docker compose cp postgres:/tmp/bluemoon-backup.sql backup_bluemoon.sql
```

Để kiểm tra bản sao lưu, tạo một database trống riêng và khôi phục vào đó.

```powershell
docker compose exec -T postgres createdb -U bluemoon_owner bluemoon_restore
docker compose cp backup_bluemoon.sql postgres:/tmp/bluemoon-restore.sql
docker compose exec -T postgres psql -X -U bluemoon_owner -d bluemoon_restore -v ON_ERROR_STOP=1 -f /tmp/bluemoon-restore.sql
```
