# Cơ sở dữ liệu BlueMoon — Sprint 1

Cập nhật: 04/11/2026. PostgreSQL 16/17.

## 1. Cấu trúc hiện hành

Cơ sở dữ liệu phục vụ US-SEC-01/02, NFR-02 và US-001 / RAW-001/002. Công việc liên quan: DB-01, BE-01/02, DB-02, BE-03; kiểm thử QA-05 và UAT BA-03.

| Bảng | Mục đích |
|---|---|
| `users` | Tài khoản đăng nhập, vai trò, trạng thái hoạt động và khóa tạm |
| `persons` | Hồ sơ cá nhân dùng chung: tên, CCCD, liên hệ, ngày sinh |
| `apartments` | Mã căn, tầng, diện tích thông thủy, loại và trạng thái mặt bằng |
| `households` | Hộ gắn với căn hộ và người đại diện |
| `residents` | Liên kết người với hộ, quan hệ và trạng thái cư trú |
| `household_head_history` | Lịch sử chủ hộ, snapshot và tài khoản thực hiện |
| `audit_logs` | Nhật ký thao tác |

Thiết kế: [ERD Sprint 1](../docs/02_design_and_architecture/erd_sprint_1_proposal.md). Tên cột/Java/DTO: [DATA_NAMING_CONVENTIONS.md](../docs/sprints/sprint-1/DATA_NAMING_CONVENTIONS.md).

| Tệp | Nội dung |
|---|---|
| [schema.sql](schema.sql) | Tạo 7 bảng, khóa, ràng buộc, index và trigger |
| [permissions.sql](permissions.sql) | Cấp quyền tối thiểu cho role ứng dụng `bluemoon_app`; chạy bằng owner |
| [seed_data.sql](seed_data.sql) | Dữ liệu giả để phát triển, kiểm thử và demo Sprint 1 |

## 2. Khởi tạo

Chọn một database PostgreSQL trống do role owner quản lý. DBA tạo riêng login `bluemoon_owner` và `bluemoon_app`, cấp mật khẩu qua secret/môi trường, không ghi mật khẩu vào Git. Chạy bootstrap và script quyền bằng owner:

```bash
psql -h <HOST> -p <PORT> -U bluemoon_owner -d <DATABASE> -v ON_ERROR_STOP=1 -f database/schema.sql
psql -h <HOST> -p <PORT> -U bluemoon_owner -d <DATABASE> -v ON_ERROR_STOP=1 -f database/permissions.sql
psql -h <HOST> -p <PORT> -U bluemoon_owner -d <DATABASE> -v ON_ERROR_STOP=1 -f database/seed_data.sql
```

Nếu dùng SSH tunnel từ máy cá nhân tới VM, HOST là `127.0.0.1`, PORT là cổng local đã chuyển tiếp, ví dụ `15432`.

Mỗi script dùng một transaction: lỗi thì rollback, không giữ kết quả một phần. `ON_ERROR_STOP=1` giúp psql dừng ngay khi lỗi. Trong DBeaver/pgAdmin, chạy từng script và cấu hình dừng khi lỗi; nếu phiên còn trong transaction lỗi, dùng `ROLLBACK;` trước khi tiếp tục.

- Schema chỉ dùng để khởi tạo, không tự nâng cấp bảng đã có. Báo “relation already exists” thì dừng và kiểm tra cấu trúc hoặc chọn database thử nghiệm trống; không tự xóa bảng để chạy lại.
- Seed là thao tác maintenance-only: dừng ứng dụng và writer khác trước khi chạy. Seed yêu cầu **cả 7 bảng không có bản ghi**, timeout sau 2 giây nếu không lấy được khóa. Chạy lần hai hoặc chạy trên database đã nhập một phần sẽ báo lỗi trước khi chèn; mọi lỗi rollback toàn bộ. Seed không tự xóa dữ liệu hoặc đặt lại mật khẩu.
- Chỉ nạp seed vào môi trường phát triển/demo, không dùng cùng hồ sơ cư dân thật.

## 3. Dữ liệu mẫu và tài khoản thử

| Tài khoản | Vai trò | Hoạt động |
|---|---|---|
| `admin` | ROLE_ADMIN | Có |
| `ketoan` | ROLE_ACCOUNTANT | Có |
| `thuquy` | ROLE_CASHIER | Có |
| `bqt_inactive` | ROLE_ACCOUNTANT | Không |

Mật khẩu demo chung: `Password@123`, lưu bằng BCrypt cost 10. Đây là mật khẩu mẫu công khai, không phải mật khẩu kết nối PostgreSQL. Tài khoản resident portal không nằm trong dữ liệu demo Sprint 1.

Seed có **4 tài khoản, 11 hồ sơ cá nhân, 7 căn, 3 hộ, 6 bản ghi cư trú, 4 sự kiện chủ hộ và 4 nhật ký**. Các căn đại diện đủ KIOT, COMMERCIAL, RESIDENTIAL, PENTHOUSE; có thường trú, tạm trú, tạm vắng và một trẻ chưa có CCCD.

P.601 có người đại diện ban đầu là Nguyễn An Mẫu, sau đó chuyển sang Trần Lan Mẫu. Hộ hiện tại tham chiếu Lan, lịch sử giữ cả hai hồ sơ qua snapshot. Mã quan hệ HEAD/SPOUSE/CHILD/PARENT/TENANT/OTHER đã chốt. P.601 và P.602 cùng tầng 6 nhưng có loại khác nhau.

## 4. Trigger và trách nhiệm ứng dụng

- `set_row_updated_at`: dùng chung cho 5 bảng có updated_at, giữ created_at và cập nhật updated_at khi sửa. INSERT bỏ qua timestamps thì DEFAULT điền thời gian. CURRENT_TIMESTAMP lấy thời điểm bắt đầu transaction.
- `validate_household_head`, `protect_household_head_profile`: người đại diện phải có CCCD và SĐT, không được sửa mất các thông tin bắt buộc khi còn được hộ tham chiếu.
- `is_valid_head_snapshot`: kiểm tra object JSONB, khóa bắt buộc, kiểu và định dạng; không xác minh nội dung có đúng là dữ liệu trước/sau thực tế.
- `reject_history_mutation`: chặn sửa/xóa/truncate lịch sử và audit. Trigger không thay thế việc giới hạn quyền tài khoản database.
- CHECK/FK/UNIQUE bảo vệ diện tích, tầng và loại độc lập, CCCD, thời hạn cư trú, một hộ ACTIVE/căn và một hồ sơ cư trú/người.
- Service vẫn phải kiểm tra email, ngày sinh khi khai báo cư dân, danh mục quan hệ, phân quyền, BCrypt và quy tắc khóa đăng nhập.
- Service lấy changed_by_user_id từ phiên đăng nhập; ghi thay đổi hộ/hồ sơ, snapshot và audit trong cùng transaction. Trigger không tự xác định tài khoản web hoặc tự sinh lịch sử.
- Snapshot có khóa `full_name`, `national_id`, `phone`, `email`. Đây là dữ liệu nhạy cảm, chỉ hiển thị cho người có quyền.

## 5. Kiểm tra sau khi nạp

```sql
SELECT 'users' AS table_name, count(*) FROM users
UNION ALL SELECT 'persons', count(*) FROM persons
UNION ALL SELECT 'apartments', count(*) FROM apartments
UNION ALL SELECT 'households', count(*) FROM households
UNION ALL SELECT 'residents', count(*) FROM residents
UNION ALL SELECT 'household_head_history', count(*) FROM household_head_history
UNION ALL SELECT 'audit_logs', count(*) FROM audit_logs;
```

Kết nối Spring Boot bằng DB_URL, `DB_USERNAME=bluemoon_app`, DB_PASSWORD và `JPA_DDL_AUTO=validate`. Role ứng dụng không sở hữu bảng, không được ALTER/TRIGGER/TRUNCATE; history/audit chỉ cho SELECT/INSERT. Không commit mật khẩu kết nối hoặc để Hibernate tự đổi cấu trúc database dùng chung.

Chạy DDL và seed thành công chỉ xác nhận khởi tạo dữ liệu. Nghiệp vụ cần test Service/Controller, UAT và kiểm tra PostgreSQL phù hợp trước nghiệm thu Sprint; H2 smoke test không thay thế kiểm tra DDL/trigger.

## 6. Quyết định dữ liệu 04/11/2026

- users chỉ lưu tài khoản và person_id UNIQUE NOT NULL; tên/email/điện thoại ở persons. Hồ sơ có tài khoản cần email/SĐT; email duy nhất trong tập người có tài khoản, không duy nhất toàn bộ persons.
- Hộ gắn cố định một căn; chuyển căn kết thúc hộ cũ, tạo hộ mới. `moved_out_date` NULL với ACTIVE; hộ đã kết thúc được để NULL nếu chưa rõ ngày. Lịch sử chỉ lưu household_id, truy căn qua hộ; không có apartment_id/event_type trong lịch sử. Event type suy từ cặp ID.
- Username/email lưu lowercase; mã căn/mã hộ lưu uppercase; các chuỗi nghiệp vụ phải được trim trước khi ghi. Database từ chối dạng chưa chuẩn hóa.
- Loại mặt bằng độc lập tầng. status chỉ VACANT/OCCUPIED do BQT xác nhận; không quản lý sửa chữa.
- Ghi persons/users/households/residents yêu cầu READ COMMITTED, cùng advisory lock (3180,13) trước khóa hàng. Kiểm tra hồ sơ tài khoản và HEAD hoãn cuối transaction. Đây là khóa thô có giới hạn thông lượng, chưa nghiệm thu NFR.
- Dữ liệu tham khảo thử nghiệm, không là migration. Không nạp cùng dữ liệu thật.
- Kiểm tra mới: `python database/tests/verify_erd_revision.py --image postgres:17-alpine` và lặp với `postgres:16-alpine` (Python chuẩn + Docker đang chạy). Runner tạo container riêng không publish cổng, không gắn volume và dọn container trong finally; không truy cập VM.
- [Báo cáo mới](tests/ERD_REVISION_VERIFICATION.md); [báo cáo cũ](tests/SPRINT1_SCHEMA_VERIFICATION.md) chỉ là bằng chứng lịch sử.
