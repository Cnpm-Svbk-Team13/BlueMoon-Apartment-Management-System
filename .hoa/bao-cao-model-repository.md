# Báo cáo hoàn thiện Model và Repository

## 1. Phạm vi và truy vết

- Phạm vi: model/repository Sprint 1 theo `database/schema.sql` và quy ước tên dữ liệu.
- Truy vết chính: `US-SEC-01`, `US-SEC-02`, `US-001`, `RAW-001`, `RAW-002`, `NFR-02`; nền dữ liệu cho `US-002/003` chưa được nghiệm thu nghiệp vụ.
- Đã giữ nguyên thay đổi có trước trong `.gitignore`; file này đã có chỉnh sửa thêm `.hoa/` trước khi bắt đầu công việc. Báo cáo hiện tại nằm trong thư mục bị ignore theo cấu hình đó.

## 2. Đã triển khai

- Bổ sung `User` và `UserRole`, ánh xạ bảng `users`, quan hệ một-một tới `Person`, các trường khóa đăng nhập, vai trò, trạng thái và timestamps.
- Bổ sung `UserRepository` với tra cứu tên đăng nhập, email qua hồ sơ cá nhân và kiểm tra liên kết hồ sơ.
- Bổ sung truy vấn lịch sử theo hộ/lấy sự kiện gần nhất; bổ sung truy vấn audit theo tài khoản hoặc đối tượng.
- Thêm `HouseholdHeadHistory.getEventType()` để suy ra ASSIGNED/PROFILE_UPDATED/REPLACED từ hai hồ sơ chủ hộ, không lưu trùng dữ liệu; Service đọc lại phương thức này.
- Sửa ánh xạ actor trong `AuditLog` và `HouseholdHeadHistory` từ ID rời thành quan hệ tới `User`; giữ các getter ID để read-model hiện tại không phải đổi.
- Cập nhật các Service ghi audit/lịch sử để lấy tham chiếu tài khoản qua `UserRepository`, tránh lưu FK không gắn với entity.
- Hoàn thiện truy cập/cập nhật dữ liệu còn thiếu ở `Person`, `Resident`, `Household`, `Apartment`, bổ sung validation cho các bất biến phù hợp với schema (CCCD, số điện thoại, diện tích, tầng, ngày cư trú và ngày kết thúc hộ).
- Cập nhật package documentation, fixtures kiểm thử theo cấu trúc `users`/`persons` hiện hành và thêm kiểm thử bất biến ngày tháng ở model.
- Thêm assertion kiểm thử cho các truy vấn history/audit vừa bổ sung.
- Rà luồng truy vấn tới Service: không thấy vòng lặp đọc quan hệ lazy tạo N+1 hiện hữu; các trường hợp history chỉ đọc ID qua proxy. Gắn `@EntityGraph(headPerson)` vào truy vấn hộ theo căn/trạng thái vì màn hình chi tiết cần đọc hồ sơ chủ hộ ngay sau đó; thêm kiểm tra `PersistenceUnitUtil` cho fetch graph.
- Các thay đổi mới có giải thích tiếng Việt. Không sửa DDL, seed hay cấu hình hiện có.

## 3. Kiểm tra đã thực hiện

| Kiểm tra | Kết quả | Giới hạn |
|---|---|---|
| `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify` | Đã chạy thành công ở revision trung gian: 32 test, 0 lỗi, 0 failure, 2 test PostgreSQL bị skip | Sau lần chạy này có thêm cập nhật model và `ModelValidationTest`; trạng thái cuối cùng chưa được build/test |
| `git diff --check` | Thành công | Chỉ kiểm tra whitespace |
| Kiểm tra call site/fixture của AuditLog, lịch sử và tài khoản | Không còn các lệnh tạo bảng users rút gọn; call site đã chuyển sang tham chiếu User | Không thay cho kiểm thử PostgreSQL |
| Kiểm tra luồng N+1 bằng đọc mã và kiểm thử fetch graph | Đã thêm kiểm tra headPerson được nạp cùng truy vấn hộ | Test mới chưa được chạy; chưa có số đo số SQL statement trên phiên bản code cuối |

Một lần chạy Maven trước đó thất bại do fixture chưa tạo tài khoản được tham chiếu bằng FK và do Mockito stub thừa; các fixture/stub liên quan đã được sửa trước lần `clean verify` thành công nêu trên.

Lệnh xác minh toàn bộ cuối cùng không được cấp quyền chạy; không thử thay thế bằng lệnh build khác. Các truy vấn history/audit, EntityGraph và assertion bổ sung ở các lượt tiếp theo cũng chưa được chạy kiểm thử. Vì vậy không ghi nhận trạng thái code hiện tại là đã xác minh hoặc nghiệm thu.

## 4. Việc tiếp theo

1. Chạy `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify` trên trạng thái hiện tại; xử lý lỗi nếu có.
2. Xác nhận truy vấn `@EntityGraph` trên Hibernate/H2 và đo SQL statement khi có thể; hai test PostgreSQL bị skip không chứng minh tính tương thích PostgreSQL.
3. Chạy kiểm thử schema trên PostgreSQL riêng biệt.
4. Rà soát và review chéo model/repository cùng các test liên quan trước khi merge.
5. Tiếp tục phần Service/Controller của `US-SEC-01/02` (quản lý tài khoản, xác thực và khóa đăng nhập) theo acceptance criteria; model/repository không đồng nghĩa chức năng nghiệp vụ đã hoàn thành.

## 5. Trạng thái bàn giao

- **Thiết kế:** Đối chiếu theo schema và quy ước hiện hành.
- **Triển khai:** Đã bổ sung/chỉnh sửa model, repository và call site phụ thuộc trực tiếp.
- **Đã chạy:** Một lần `clean verify` thành công trên revision trung gian; `git diff --check` thành công trên trạng thái cuối.
- **Đã nghiệm thu:** Chưa. Cần xác minh trạng thái cuối, kiểm tra PostgreSQL và review nhóm.
