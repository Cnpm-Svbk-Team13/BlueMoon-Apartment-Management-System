# Báo cáo rà soát và hoàn thiện Service

Ngày cập nhật: 15/11/2026

## 1. Phạm vi, nguồn và cách đọc trạng thái

- Rà soát các service trong `src/main/java/com/bluemoon/bfms/service/`: `ApartmentService`, `ApartmentAccessService`, `HouseholdHeadService`, `HouseholdDetailsService`, `AuthService`, `UserService` và `package-info.java`.
- Đối chiếu với US/AC, kế hoạch và backlog Sprint 1, quy ước dự án, repository, entity, controller/call site, schema và test.
- Phân biệt rõ: **có schema/entity** không đồng nghĩa **đã có service nghiệp vụ**; có service/test không đồng nghĩa **đã nghiệm thu US**.
- Đây là rà soát và cập nhật báo cáo; không bổ sung nghiệp vụ ngoài Service. Báo cáo nằm trong `.hoa/`; `.gitignore` đang có thay đổi sẵn trong worktree và không được chỉnh trong lượt này.

## 2. Tóm tắt tình trạng Service

Hiện có service nghiệp vụ cho ba nhóm chính:

1. **Căn hộ:** tạo, cập nhật, tra cứu và kiểm tra quyền truy cập.
2. **Hộ/chủ hộ:** gán/thay chủ hộ, cập nhật hồ sơ chủ hộ, đọc chi tiết và lịch sử liên quan. Một số thao tác trên `Resident` chỉ hỗ trợ bảo đảm quan hệ/chủ hộ nhất quán.
3. **Tài khoản/xác thực:** quản lý tài khoản BQT, đổi/cấp lại mật khẩu, đăng nhập, khóa tài khoản và audit.

Đây **không phải** triển khai đầy đủ quản lý mọi nhân khẩu hoặc mọi nghiệp vụ sản phẩm. Chưa có service đầy đủ cho CRUD nhân khẩu, chuyển trạng thái cư trú và lịch sử cư trú; chưa có các service báo cáo nhân khẩu, biểu phí, lập hóa đơn, thu/gạch nợ, quyên góp và báo cáo tài chính.

Kế hoạch Sprint 1 tập trung vào US-001 cùng US-SEC-01/02. US-002/003 là yêu cầu của sản phẩm tổng thể nhưng ma trận Sprint ghi phần cư trú là chuẩn bị schema và nêu rõ chưa nghiệm thu chức năng US-003; ghi chú đồng bộ cuối Sprint cũng xác nhận US-002/003 chưa nghiệm thu. Do đó, không coi bảng `residents`, enum hoặc constraint trong schema là chức năng hoàn thành.

## 3. Kết quả rà soát các service hiện có

### ApartmentService

- Tạo/sửa căn hộ kiểm tra và chuẩn hóa mã căn, tầng 1–30, diện tích dương và precision phù hợp `DECIMAL(12,2)`, loại mặt bằng/trạng thái; ghi dữ liệu và audit trong transaction.
- Xử lý unique constraint có phân biệt lỗi trùng mã căn với lỗi lưu trữ khác.
- Đã sửa `findByRoomNumber` và `existsByRoomNumber` để chuẩn hóa/kiểm tra mã căn như luồng tạo/sửa.
- Đã sửa `findByFloorNumber` để kiểm tra tầng và trả kết quả theo thứ tự mã căn; `listApartments(null)` vẫn giữ chức năng liệt kê toàn bộ.
- Bổ sung test cho chuẩn hóa mã, đầu vào sai và tra cứu theo tầng.

### ApartmentAccessService

- Xác minh actor theo username; chỉ tài khoản đang hoạt động có vai trò Admin/Kế toán được qua kiểm tra quyền của service.
- Từ chối bằng `AccessDeniedException` nếu actor không hợp lệ.
- Đây là kiểm tra quyền ở một luồng service, không thay thế cấu hình phân quyền toàn bộ route của Spring Security.

### HouseholdHeadService và HouseholdDetailsService

- Gán/thay chủ hộ kiểm tra DTO, actor/IP, căn hộ, chủ hộ kỳ vọng từ form, ngày chuyển vào khi tạo hộ, Person, cư trú ở hộ khác và quan hệ chủ hộ.
- Khóa ghi được lấy trước khi đọc trạng thái; thay đổi hộ/Person, lịch sử và audit nằm trong transaction. Có kiểm tra form cũ để từ chối dữ liệu stale.
- Cập nhật hồ sơ chặn CCCD trùng; không ghi lịch sử nếu dữ liệu không đổi; khi Person được nhiều hộ tham chiếu, cập nhật snapshot cho các hộ đó.
- Truy vấn chi tiết phục vụ hồ sơ chủ hộ/lịch sử. Các thao tác với `Resident` trong luồng này hỗ trợ gán hoặc thay chủ hộ; **không phải** chức năng thêm/sửa/xóa tùy ý mọi thành viên trong hộ.
- Chưa có luồng nghiệp vụ đầy đủ để quản lý danh sách nhân khẩu theo US-002, upload giấy tờ, chuyển Thường trú/Tạm trú/Tạm vắng, kiểm tra khoảng ngày và ghi lịch sử biến động theo US-003.
- Test H2/web bao phủ các nhánh chủ hộ, dữ liệu stale, quan hệ cư trú, lịch sử và rollback. Kiểm thử đồng thời PostgreSQL có trong suite nhưng bị skip nếu không cấu hình PostgreSQL.

### AuthService

- Constructor tạo `PasswordEncoder` BCrypt cost 10 và `Clock` UTC; không tạo tài khoản hay mật khẩu.
- `authenticate` nhận mật khẩu thô để gọi `matches(raw, storedHash)`; không so sánh raw trực tiếp với hash và không lưu raw vào audit.
- Xử lý tài khoản không tồn tại/inactive, khóa 15 phút sau lần sai thứ năm, thời điểm hết khóa `now >= locked_until`, reset bộ đếm khi đăng nhập hợp lệ và audit kết quả.
- Tìm tài khoản bằng pessimistic lock trong transaction để các lần đăng nhập đồng thời không làm mất bộ đếm.
- Đổi mật khẩu kiểm tra mật khẩu hiện tại, độ dài tối thiểu 8 ký tự, xác nhận khớp và giới hạn 72 byte UTF-8 của BCrypt; chỉ hash được lưu.

### UserService

- Tạo tài khoản cần mật khẩu khởi tạo do bên gọi truyền vào; service không tự sinh hoặc gửi mật khẩu. Lưu BCrypt hash và trả `UserSummary` không có hash/mật khẩu.
- Tạo/cập nhật Person và User, đổi trạng thái, liệt kê, cấp lại mật khẩu; yêu cầu actor là Admin đang hoạt động và ghi audit trong transaction.
- Chuẩn hóa username/email; kiểm tra email, username, phone 10 chữ số, role, mật khẩu và IP. Cấp lại mật khẩu xóa bộ đếm sai và khóa hiện tại.
- Danh sách dùng `EntityGraph` để tải Person cùng truy vấn, tránh truy vấn bổ sung theo từng tài khoản.
- Kiểm tra Admin trong service không thay thế route authorization; actor ID phải lấy từ principal phía server, không tin ID do request tự gửi.

## 4. Thay đổi và kiểm tra đã thực hiện

- Sửa chuẩn hóa/kiểm tra đầu vào ở các phương thức tra cứu căn hộ; thêm kiểm thử tương ứng.
- Bổ sung kiểm thử Auth/User cho các nhánh nghiệp vụ trước đó chưa được bao phủ.
- Bổ sung kiểm thử H2 dùng repository thật cho `findByUsernameForUpdate`, xác nhận bộ đếm lần sai thứ năm và thời hạn khóa được lưu sau commit.
- Lần chạy cuối đã ghi nhận: `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify` — **BUILD SUCCESS**, 52 test, 0 lỗi, 0 failure, 2 test PostgreSQL bị skip.
- `git diff --check` lần cuối trước khi lập báo cáo trước đó thành công. Lần cập nhật tài liệu này không thay đổi Java.

### Giới hạn xác minh

- H2 chỉ xác minh luồng framework/DB cô lập; không chứng minh tương thích DDL, lock hay trigger PostgreSQL.
- Hai test `Sprint1PostgresTest` bị skip do chưa cấu hình PostgreSQL riêng.
- Chưa chạy `clean verify -Psprint1-quality`; chưa có số coverage xác nhận ngưỡng Service/Controller line coverage >=85%.
- Chưa có bằng chứng UAT, nghiệm thu nội bộ/review chéo đầy đủ hoặc xác nhận Sprint.

## 5. Trạng thái theo yêu cầu sản phẩm

| Phạm vi | Hiện trạng Service | Kết luận |
|---|---|---|
| US-001 — căn hộ và hồ sơ/lịch sử chủ hộ | Có các luồng chính trong `ApartmentService`, `HouseholdHeadService`, `HouseholdDetailsService`; còn phụ thuộc UAT/PostgreSQL/quality gate | Đã triển khai một phần đáng kể; chưa ghi nhận nghiệm thu |
| US-SEC-01/02 — quản lý tài khoản và đăng nhập | Có `UserService`/`AuthService`; chưa nối đầy đủ Controller và Spring Security | Logic Service có; chức năng ứng dụng chưa hoàn chỉnh/nghiệm thu |
| US-002 — quản lý chi tiết nhân khẩu | Có cấu trúc dữ liệu và xử lý quan hệ cần cho chủ hộ; chưa có CRUD đầy đủ thành viên, upload và AC tương ứng | Chưa hoàn thành |
| US-003 — trạng thái/biến động cư trú | Schema chuẩn bị trường và constraint; chưa có service chuyển trạng thái, audit/lịch sử đầy đủ | Chưa hoàn thành; Sprint 1 chỉ chuẩn bị dữ liệu |
| US-004 — báo cáo nhân khẩu | Chưa có report service/export Excel/PDF | Chưa triển khai |
| US-005–007, US-011 — biểu phí, lập phí, thu/gạch nợ, báo cáo nợ | Chưa có service hóa đơn/thanh toán/biểu phí | Chưa triển khai |
| US-008–010 — quyên góp tự nguyện và báo cáo quỹ | Chưa có service nghiệp vụ | Chưa triển khai |

Không có cơ sở để kết luận toàn bộ Service sản phẩm đã hoàn chỉnh trên 90%; chưa có thước đo phần trăm theo AC và nhiều US chưa được triển khai.

## 6. Việc cần thực hiện tiếp theo

Các bước dưới đây là đề xuất theo phụ thuộc US/AC, chưa phải cam kết đổi phạm vi Sprint. Cần Sơn/Hoà chốt ưu tiên và task trước khi triển khai.

### A. Hoàn tất bằng chứng/đóng Sprint 1

1. Hoàn thiện hoặc xác nhận các Controller đăng nhập/quản lý tài khoản; cấu hình Spring Security `SecurityFilterChain`, bảo vệ route và ánh xạ role. Actor ID phải lấy từ principal.
2. Chạy `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify -Psprint1-quality`; xử lý nếu coverage gộp Service/Controller dưới 85%.
3. Chạy test PostgreSQL riêng cho schema, advisory lock, unique/partial index và truy vấn khóa; không dùng H2 thay bằng chứng PostgreSQL.
4. Thực hiện UAT cho đăng nhập, CRUD căn hộ và chủ hộ; lưu kết quả, review chéo và chỉ đánh dấu nghiệm thu khi có đủ AC/test/UAT.

### B. Triển khai đầy đủ quản lý nhân khẩu và cư trú

5. Sau US-001, triển khai US-002: DTO/validation và service tạo/sửa/xem nhân khẩu; kiểm tra CCCD duy nhất toàn cục, quan hệ chuẩn hóa và ràng buộc cư dân–hộ. Chốt riêng cách lưu/xem tệp đính kèm và kiểm tra loại/dung lượng.
6. Trên dữ liệu nhân khẩu, triển khai US-003: quy tắc ba trạng thái; bắt buộc và kiểm tra khoảng ngày cho Tạm trú/Tạm vắng; audit/lịch sử không mất dữ liệu. Trước khi code, xác nhận schema hiện tại có đủ lưu lịch sử vĩnh viễn hay cần thay đổi/migration được duyệt.
7. Viết test biên, lỗi, rollback và kiểm tra PostgreSQL cho CCCD uniqueness, ngày cư trú, audit và tính đồng thời; sau đó mới làm UAT.
8. Triển khai US-004 sau khi US-001–003 có dữ liệu ổn định: truy vấn/report service, lọc, export Excel/PDF và bảo vệ dữ liệu nhạy cảm theo chế độ công khai.

### C. Triển khai phân hệ tài chính theo thứ tự phụ thuộc

9. US-005: cấu hình khoản thu, phân biệt bắt buộc/tự nguyện, kiểm tra loại phí, diện tích và đơn vị tiền.
10. US-006: tính phí/lập hóa đơn theo `BigDecimal`; batch billing trong `@Transactional(rollbackFor = Exception.class)`.
11. US-007 và US-011: thu/gạch nợ, biên lai, lịch sử và báo cáo nợ. Trước khi nghiệm thu thanh toán, thêm optimistic locking `@Version` cho hóa đơn và test hai giao dịch thanh toán cạnh tranh trên PostgreSQL.
12. US-008–010: triển khai quyên góp tách biệt hoàn toàn khỏi nợ bắt buộc, biên nhận riêng, báo cáo quỹ và masking dữ liệu cá nhân ở public mode.

Với từng US: truy vết RAW/AC và task ID; triển khai theo tầng Entity/DTO → Repository → Service → Controller/View; thêm test nghiệp vụ phù hợp; chạy verify/quality profile, PostgreSQL và UAT tương ứng. Không coi thiết kế/schema hoặc build xanh là bằng chứng hoàn thành nghiệp vụ.

## 7. Trạng thái kết luận

- **Đã thiết kế:** schema cư trú có một phần cấu trúc cho nhân khẩu/trạng thái; tài liệu có US-001–011 và US-SEC-01/02.
- **Đã triển khai:** các Service căn hộ, hộ/chủ hộ và tài khoản/xác thực ở mức nêu trên.
- **Đã chạy:** `clean verify` với 52 test; PostgreSQL tests bị skip.
- **Chưa nghiệm thu:** US-001/US-SEC-01/02 và Sprint chưa đủ hồ sơ UAT/quality/PostgreSQL; US-002 trở đi còn các phần nghiệp vụ chưa triển khai theo bảng trạng thái.
