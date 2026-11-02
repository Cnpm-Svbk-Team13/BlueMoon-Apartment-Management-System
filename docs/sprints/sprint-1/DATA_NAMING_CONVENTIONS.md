# Quy ước tên trường dữ liệu Sprint 1

Cập nhật: 01/11/2026. Dành cho Sơn (DB/Backend), Hoà (tài khoản/bảo mật) và Đức Anh (giao diện).

**Đọc bảng tên trước khi tạo Entity, DTO hoặc form.** Ví dụ cùng một dữ liệu: PostgreSQL `room_number` → Java `roomNumber` → trường form `roomNumber` → nhãn “Mã căn hộ”. Không tự đổi thành `apartmentCode` hoặc `maCanHo` trong cùng giao tiếp.

## 1. Nguồn và trạng thái

- Nguồn cột/kiểu SQL hiện có: [schema](../../../database/schema.sql), đối chiếu [ERD](../../02_design_and_architecture/erd.md).
- Nguồn nghiệp vụ/validation: [US và AC](../../01_requirements_and_planning/05_chi_tiet_us_va_acceptance_criteria.md); nguồn quyết định: [quy chuẩn](../../PROJECT_CONVENTIONS.md).
- Truy vết: US-SEC-01/02, NFR-02 → SEC-01…03, FE-02/03; US-001, RAW-001/002 → BE-01/02, DB-02, BE-03, FE-04/05 trong [backlog](github_issues_backlog.md).
- **Cột hiện có / tên Java-form quy định:** cột có trong schema, còn tên Java/DTO/form là quy ước để triển khai. Chưa có Entity/DTO nghiệp vụ hoàn chỉnh; form login hiện là khung, không coi bảng này là bằng chứng code đã có.
- **Đề xuất, chờ DB-02/BE-03:** hồ sơ/lịch sử chủ hộ chưa đủ thiết kế lưu trữ; chỉ dùng để trao đổi và dựng giao diện mẫu, chưa coi là giao tiếp đã chốt.

Tài liệu này chốt cách gọi và ánh xạ, không thay AC hoặc tự thêm cột, URL, chức năng. Các tên DTO/model minh họa ở mục 5 chưa phải lớp hay giao tiếp đã triển khai.

## 2. Quy tắc dùng chung

| Thành phần | Quy tắc | Ví dụ |
|---|---|---|
| Bảng SQL | Giữ tên hiện có, tiếng Anh, `snake_case` | `users`, `apartments`, `households` |
| Cột SQL | `snake_case` | `full_name`, `area_sqm` |
| Lớp Java | `PascalCase` | `Apartment`, `ApartmentForm` |
| Thuộc tính, biến, phương thức Java | `camelCase`; tên thể hiện ý nghĩa, không viết tắt tùy ý | `roomNumber`, `findByRoomNumber` |
| Hằng và giá trị enum | `UPPER_SNAKE_CASE` | `ROLE_ADMIN`, `RESIDENTIAL` |
| DTO / tên trường gửi form | Khớp chính xác tên thuộc tính DTO, kể cả chữ hoa/thường | `areaSqm`, không dùng `area_sqm` |
| Nhãn hiển thị | Tiếng Việt, tách khỏi tên kỹ thuật | “Diện tích (m²)” |
| Thời gian | Ngày: hậu tố `Date`; thời điểm: `At` hoặc `Until` theo nghĩa | `movedInDate`, `createdAt`, `lockedUntil` |

- Dùng `@Table`, `@Column` và `@JoinColumn` để chỉ rõ bảng/cột/FK. Thuộc tính Java là dữ liệu của đối tượng OOP; annotation nối nó với tên SQL, không buộc hai tên phải giống từng ký tự.
- FK `apartment_id`: Entity có tham chiếu `apartment` kiểu `Apartment`; DTO dùng `apartmentId` kiểu `Long` khi chỉ gửi ID. Tương tự `household_id` → `household` / `householdId`. Tránh hai thuộc tính ghi độc lập vào cùng một FK.
- ID dùng `Long`; diện tích dùng `BigDecimal`; CCCD/SĐT dùng `String` để giữ số 0 đầu. Ngày dùng `LocalDate`, thời điểm `TIMESTAMP WITH TIME ZONE` dùng `OffsetDateTime`.
- Cột `is_active` → thuộc tính `active`, không phải thuộc tính `isActive`. Với `boolean`, getter là `isActive()`; với `Boolean` nullable, dùng `getActive()`. Tên form vẫn là `active` ở cả hai trường hợp.
- Schema hiện cho phép NULL ở một số cột có DEFAULT (`is_active`, `failed_attempts`, `status`…). DEFAULT không đồng nghĩa NOT NULL. Dùng kiểu bao `Boolean`/`Integer` khi cần phản ánh NULL; việc chuẩn hóa dữ liệu/đổi constraint phải được chốt riêng khi triển khai, không âm thầm coi NULL là false/0.
- Form dùng mã enum, không gửi nhãn tiếng Việt. Ví dụ option hiển thị “Căn hộ ở” nhưng value là `RESIDENTIAL`. Lưu enum theo tên, không theo số thứ tự.
- Tách `ApartmentStatus` (`VACANT`, `OCCUPIED`, `RENOVATING`) và `HouseholdStatus` (`ACTIVE`, `MOVED_OUT`, `TRANSFERRED`). `role` dùng các mã `ROLE_*` trong schema; `ROLE_RESIDENT` đã có trong danh mục không đồng nghĩa triển khai Portal ở Sprint 1.
- Không bind trực tiếp Entity từ form. DTO chỉ nhận trường được phép sửa; ID, quyền và tham chiếu phải được server kiểm tra lại.

## 3. Bảng ánh xạ theo phân hệ

Trong từng bảng, cột “Java” là tên thuộc tính Entity dự kiến; cột “DTO/form” là tên trường dữ liệu, không mặc định mọi trường đều là input. Mỗi hàng kế thừa người phụ trách và trạng thái ghi ngay trước bảng.

### 3.1. Tài khoản — Hoà, phối hợp Sơn về schema và Đức Anh về form

**Trạng thái: cột hiện có / tên Java-form quy định.** SEC-03, FE-03 / US-SEC-01, NFR-02.

| Ý nghĩa | Cột DB | Java | DTO/form | Kiểu Java | Ràng buộc / sử dụng |
|---|---|---|---|---|---|
| ID tài khoản | `users.id` | `id` | `id` | `Long` | Sinh bởi DB; không nhập khi tạo; server kiểm tra đối tượng khi sửa |
| Tên đăng nhập | `users.username` | `username` | `username` | `String` | Bắt buộc, duy nhất, tối đa 50 |
| Họ tên | `users.full_name` | `fullName` | `fullName` | `String` | Bắt buộc, tối đa 100 |
| Email | `users.email` | `email` | `email` | `String` | Form BQT bắt buộc/hợp lệ theo AC, duy nhất, tối đa 100; SQL hiện cho NULL |
| SĐT | `users.phone` | `phone` | `phone` | `String` | Form BQT bắt buộc 10 chữ số theo AC; SQL hiện cho NULL/tối đa 20 |
| Vai trò | `users.role` | `role` | `role` | `UserRole` | Bắt buộc, đúng mã enum; chỉ thao tác được phép mới cho sửa |
| Hoạt động | `users.is_active` | `active` | `active` | `Boolean` với schema nullable hiện có | Chỉ form quản trị trạng thái; không dùng để biểu diễn khóa tạm 15 phút; chỉ chuyển `boolean` sau khi bảo đảm không NULL |
| Mật khẩu băm | `users.password_hash` | `passwordHash` | — | `String` | Nội bộ, bắt buộc; không trả về view/DTO hiển thị, không nhận từ form |
| Số lần sai | `users.failed_attempts` | `failedAttempts` | — | `Integer` | Nội bộ; không cho form sửa |
| Khóa đến | `users.locked_until` | `lockedUntil` | — | `OffsetDateTime` | Nội bộ, NULL khi không khóa; không trả trạng thái khóa qua lỗi login |

`UserRole` là tên enum quy định, chưa có lớp triển khai. Mật khẩu khởi tạo dùng trường nhập `password`, Hoà băm thành `passwordHash` trước khi lưu. DTO tạo tài khoản và DTO hiển thị phải tách trường bí mật, không sao chép toàn bộ Entity cho view.

### 3.2. Đăng nhập / đổi mật khẩu — Hoà và Đức Anh

**Trạng thái: tên trường quy định, chưa có form xử lý hoàn chỉnh.** SEC-01/02, FE-02 và phần đổi mật khẩu FE-03 / US-SEC-02, NFR-02.

| Ý nghĩa | Cột DB liên quan | Java nhận input | Trường form | Kiểu | Ràng buộc / sử dụng |
|---|---|---|---|---|---|
| Tên đăng nhập | `users.username` để tra cứu | `username` | `username` | `String` | Bắt buộc; form login dùng tên parameter này |
| Mật khẩu đăng nhập | So khớp `users.password_hash`, không lưu input | `password` | `password` | `String` | Bắt buộc; không log hoặc hiển thị lại |
| Mật khẩu hiện tại | So khớp hash hiện có | `currentPassword` | `currentPassword` | `String` | Bắt buộc, phải đúng |
| Mật khẩu mới | Băm rồi cập nhật `users.password_hash` | `newPassword` | `newPassword` | `String` | Bắt buộc, ít nhất 8 ký tự |
| Xác nhận mật khẩu mới | Không lưu | `confirmPassword` | `confirmPassword` | `String` | Bắt buộc, khớp `newPassword` |

Form login có thể được Spring Security tiếp nhận trực tiếp, không bắt buộc có DTO login. `password` và `passwordHash` khác nghĩa; `confirmPassword` chỉ là dữ liệu kiểm tra. Tên trường CSRF lấy từ `${_csrf.parameterName}`, không tự đặt lại. URL/method xử lý do Hoà–Đức Anh chốt riêng.

### 3.3. Căn hộ — Sơn và Đức Anh

**Trạng thái: cột hiện có / tên Java-form quy định.** BE-01/02, FE-04 / US-001, RAW-001/002.

| Ý nghĩa | Cột DB | Java | DTO/form | Kiểu Java | Ràng buộc / sử dụng |
|---|---|---|---|---|---|
| ID căn hộ | `apartments.id` | `id` | `id` | `Long` | DB sinh; dùng định danh khi sửa, server kiểm tra lại |
| Mã căn hộ | `apartments.room_number` | `roomNumber` | `roomNumber` | `String` | Bắt buộc, duy nhất, tối đa 20 |
| Tầng | `apartments.floor_number` | `floorNumber` | `floorNumber` | `Integer` | Bắt buộc, 1–30 |
| Diện tích | `apartments.area_sqm` | `areaSqm` | `areaSqm` | `BigDecimal` | Bắt buộc, dương, SQL DECIMAL(12,2); không dùng float/double |
| Loại căn | `apartments.apartment_type` | `apartmentType` | `apartmentType` | `ApartmentType` | Bắt buộc; KIOT/COMMERCIAL/RESIDENTIAL/PENTHOUSE, phù hợp tầng |
| Trạng thái căn | `apartments.status` | `status` | `status` | `ApartmentStatus` | Đúng danh mục; khi cho sửa phải validate phía server |
| Mô tả | `apartments.description` | `description` | `description` | `String` | Tùy chọn, không tự thêm vào AC bắt buộc |

Không đặt `room_number` là `roomId`: đây là mã phòng, khác ID số do DB sinh. `status` ở DTO căn hộ không phải `active` của tài khoản.

### 3.4. Hộ gia đình — Sơn, Đức Anh dùng khi giao tiếp cần đến

**Trạng thái: cột hiện có / tên Java-DTO quy định; không yêu cầu mở form sửa tất cả các trường.** BE-01, DB-02, BE-03 / US-001, RAW-002.

| Ý nghĩa | Cột DB | Java | DTO nếu cần | Kiểu Java | Ràng buộc / sử dụng |
|---|---|---|---|---|---|
| ID hộ | `households.id` | `id` | `householdId` trong dữ liệu tổng hợp | `Long` | DB sinh; tách khỏi `apartmentId` |
| Căn hộ liên quan | `households.apartment_id` | `apartment` | `apartmentId` | `Apartment` / `Long` | FK bắt buộc, tham chiếu căn hộ tồn tại |
| Mã đăng ký hộ | `households.registration_code` | `registrationCode` | `registrationCode` | `String` | SQL bắt buộc, duy nhất, tối đa 50; cách cấp do BE-03 chốt |
| Tên đại diện hiện có | `households.head_name` | `headName` | `headName` khi đọc trường hiện có | `String` | SQL bắt buộc, tối đa 100; chưa phải thiết kế đủ hồ sơ chủ hộ |
| Ngày vào ở | `households.moved_in_date` | `movedInDate` | `movedInDate` | `LocalDate` | Ngày, không dùng timestamp; SQL có DEFAULT nhưng cho NULL |
| Trạng thái hộ | `households.status` | `status` | `status` trong DTO hộ | `HouseholdStatus` | Tối đa một hộ ACTIVE/căn hộ theo partial unique index |

`households.head_name` và `residents.full_name` không được tự coi là hai bản sao cập nhật độc lập của cùng người. `residents` hiện có `full_name`, `national_id`, `phone` nhưng chưa có `email`, và có các trường NOT NULL khác như `date_of_birth`, `relationship_with_head`. Vì vậy không ánh xạ hồ sơ chủ hộ vào `residents` bằng cách đoán; DB-02 phải xác định nguồn định danh và liên kết trước.

### 3.5. Hồ sơ / lịch sử chủ hộ — Sơn đề xuất, Đức Anh xác nhận giao tiếp

**Trạng thái toàn bảng: đề xuất, chờ DB-02/BE-03.** FE-05 nhận giao tiếp đã chốt; QA-05/BA-03 đối chiếu US-001 AC 3–4, RAW-002. Không có tên bảng/cột mới nào được chốt bằng bảng này.

| Ý nghĩa | Lưu trữ DB / Entity | DTO/form đề xuất | Kiểu Java | Ràng buộc / sử dụng |
|---|---|---|---|---|
| Căn hộ cần thay chủ | Chờ thiết kế tham chiếu | `apartmentId` | `Long` | Căn hộ tồn tại, server kiểm tra |
| Họ tên chủ hộ | Chờ DB-02 xác định nguồn chuẩn | `owner.fullName` | `String` | Bắt buộc; không tự đồng nhất với `headName` |
| CCCD | Chờ DB-02; `residents.national_id` chỉ là cột hiện có để đối chiếu | `owner.nationalId` | `String` | Bắt buộc 12 chữ số, duy nhất toàn cục |
| SĐT | Chờ DB-02 | `owner.phone` | `String` | Bắt buộc 10 chữ số |
| Email | Chưa có cột email hồ sơ chủ hộ | `owner.email` | `String` | Tùy chọn; hợp lệ nếu có |
| Chủ cũ / chủ mới | Chờ mô hình lịch sử và định danh | `previousOwner`, `newOwner` | DTO hiển thị hồ sơ | Chỉ đọc; giữ dữ liệu lịch sử, không xóa cứng |
| Thời điểm thay | Chưa có cột lịch sử được chốt | `changedAt` | `OffsetDateTime` | Server ghi, chỉ đọc |
| Người thay | Chờ FK tới tài khoản | `changedByUserId` | `Long` | Server lấy từ người đăng nhập, không tin input form |

`owner.fullName` là đường dẫn thuộc tính lồng nhau: DTO gốc có `owner`, đối tượng này có `fullName`; không tạo một thuộc tính Java chứa dấu chấm. Bảng đề xuất không bắt buộc kiến trúc lưu hồ sơ hay snapshot cụ thể. Sơn hoàn thiện DDL/ERD và bảng ánh xạ thật tại DB-02, rồi cùng Đức Anh xác nhận trước ghép BE-03/FE-05; dữ liệu mẫu UI phải ghi rõ đang chờ chốt.

### 3.6. Trường thời gian dùng chung

Các bảng `users`, `apartments`, `households` đã có `created_at` → `createdAt`, `updated_at` → `updatedAt`, kiểu `OffsetDateTime`. Người phụ trách kế thừa phân hệ phía trên. Đây là metadata server/DB quản lý, chỉ đưa vào DTO đọc khi cần; không nhận giá trị tùy ý từ form. Không dùng `createdAt` thay cho thời điểm thay chủ `changedAt` khi thiết kế lịch sử chưa xác định quan hệ giữa chúng.

## 4. Ai chốt và cách đổi tên

1. Sơn xác nhận cột/FK và Entity của DB/căn hộ/chủ hộ; Hoà xác nhận tài khoản/bảo mật và phối hợp Sơn về `users`.
2. Đức Anh xác nhận tên trường DTO/form cùng chủ backend. Khánh theo dõi việc thống nhất; Huy dùng đúng tên đã chốt cho fixtures/test. Vai trò chuyên môn không thay đổi.
3. Muốn đổi tên: nêu task/US và lý do, cập nhật dòng ánh xạ, trao đổi với người nhận trước khi ghép. Phân biệt đổi tên Java với đổi cột SQL; đổi cột cần migration tương ứng khi triển khai.
4. Cùng PR phải cập nhật các chỗ bị ảnh hưởng: Entity/DTO, form, binding, validation, test/fixture và tài liệu; không chỉ đổi một đầu. Review chéo theo quy chuẩn hiện hành.

Checklist trước bàn giao: [ ] đúng tên và kiểu; [ ] form khớp DTO; [ ] enum gửi mã; [ ] FK được kiểm tra; [ ] không có mật khẩu/hash trong dữ liệu hiển thị; [ ] phần đề xuất chưa bị ghi thành đã triển khai.

## 5. Hai ví dụ nối SQL → OOP → form

**Ví dụ minh họa, không phải code đã triển khai.** Tên `apartmentForm`/`userForm` dưới đây là model attribute minh họa; chủ backend và Đức Anh còn phải chốt chúng với Controller.

```java
// Entity: thuộc tính OOP areaSqm lưu vào cột SQL area_sqm.
@Column(name = "area_sqm")
private BigDecimal areaSqm;

// Cùng nguyên tắc: room_number → roomNumber.
@Column(name = "room_number")
private String roomNumber;

// Ví dụ khi đã bảo đảm dữ liệu không NULL; xem lưu ý nullable ở mục 2.
@Column(name = "is_active")
private boolean active;
public boolean isActive() { return active; }
public void setActive(boolean active) { this.active = active; }
```

```html
<!-- Bên trong form th:object="${apartmentForm}" có DTO roomNumber/areaSqm. -->
<label>Mã căn hộ <input type="text" th:field="*{roomNumber}"></label>
<label>Diện tích (m²) <input type="number" step="0.01" th:field="*{areaSqm}"></label>
<!-- Bên trong form th:object="${userForm}" có DTO active, không phải isActive. -->
<label><input type="checkbox" th:field="*{active}">Đang hoạt động</label>
```

`th:field` liên kết input với thuộc tính DTO đang chọn bằng `th:object`; nhãn tiếng Việt không đổi tên trường gửi đi. Checkbox có xử lý giá trị khi bỏ chọn; dùng binding Thymeleaf thay vì tự đoán tên input phụ. Ví dụ chưa gồm action/method, CSRF, quyền hoặc validation đầy đủ và không phải form hoàn chỉnh. Server vẫn kiểm tra theo AC; `min`/`step` ở UI không thay thế quy tắc nghiệp vụ.
