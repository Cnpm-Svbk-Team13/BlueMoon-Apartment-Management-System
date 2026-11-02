# Quy ước tên trường dữ liệu Sprint 1

Cập nhật: 02/11/2026. Dành cho Sơn (DB/Backend), Hoà (tài khoản/bảo mật), Đức Anh (giao diện) và Huy (QA).

## 1. Nguồn và phạm vi

- Cột, kiểu, khóa và NULL lấy từ [schema.sql](../../../database/schema.sql); quan hệ xem [ERD Sprint 1](../../02_design_and_architecture/erd_sprint_1_proposal.md); dữ liệu giả xem [seed](../../../database/seed_data.sql).
- Truy vết: US-SEC-01/02, NFR-02 → DB-01, BE-01, SEC-01…03, FE-02/03; US-001, RAW-001/002 → BE-01/02, DB-02, BE-03, FE-04/05, QA-05, BA-03 trong [backlog](github_issues_backlog.md).
- Tên Java/DTO dưới đây là quy ước để triển khai, không phải bằng chứng các lớp hoặc nghiệp vụ đã được cài đặt. Không tự thêm cột hoặc URL dựa vào tài liệu này.
- Sơn phụ trách hồ sơ/căn hộ/hộ/cư trú/lịch sử; Hoà phụ trách tài khoản; Đức Anh thống nhất form với chủ backend; Huy dùng cùng tên trong fixtures/test. Khánh theo dõi bàn giao.

## 2. Quy tắc chung

- SQL dùng `snake_case`; Java và DTO/form dùng `camelCase`; lớp dùng `PascalCase`. Nhãn tiếng Việt độc lập với tên gửi lên server.
- ID dùng `Long`; CCCD/SĐT dùng `String` giữ số 0 đầu; diện tích dùng `BigDecimal`; DATE dùng `LocalDate`; TIMESTAMPTZ dùng `OffsetDateTime`.
- FK được ánh xạ thành tham chiếu đối tượng: `person_id` → `Resident.person`. DTO gửi ID dùng `personId`; không có hai thuộc tính Java cùng ghi độc lập một FK.
- `@Table`/`@Column` nối lớp và thuộc tính OOP với bảng/cột SQL; `@JoinColumn` chỉ cột FK. Ví dụ `Resident.person` tương đương JOIN bằng `residents.person_id = persons.id`.
- Enum lưu theo tên, không theo số thứ tự. Giá trị form là mã trong schema, không phải nhãn tiếng Việt.
- `users.is_active` → `User.active` kiểu `boolean`, getter `isActive()`; DTO dùng `Boolean` nếu cần phát hiện input thiếu trước khi validation.
- NULL ở cột tùy chọn khác chuỗi rỗng. Service trim đầu vào và chuyển trường tùy chọn trống thành NULL; không dùng 0 thay ID thiếu.
- Entity phản ánh dữ liệu lưu; DTO chỉ nhận trường cho phép sửa. ID, FK, quyền và dữ liệu tra cứu phải được server kiểm tra.
- Mọi bảng bên dưới có `id BIGSERIAL PK` → `id: Long` trong Entity; ID do DB sinh. Form tạo mới không tự cấp ID.
- Chỉ cột ghi “NULL” bên dưới mới cho phép NULL. DEFAULT không thay thế giá trị NULL được truyền rõ.

## 3. Ánh xạ SQL → Entity → DTO/form

Cột DTO ghi “đọc” là dữ liệu trả về; “nội bộ” không nhận từ form. Các enum và lớp Entity là tên quy định cho triển khai.

### 3.1. Tài khoản — `users` → `User`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO/form | Ràng buộc |
|---|---|---|---|
| `username` | `username: String` | `username` | Bắt buộc, không rỗng, duy nhất, ≤50 |
| `password_hash` | `passwordHash: String` | Nội bộ | Bắt buộc, ≤255; không trả hash về UI |
| `full_name` | `fullName: String` | `fullName` | Bắt buộc, không rỗng, ≤100 |
| `email` | `email: String` | `email` | Bắt buộc, duy nhất, ≤100; định dạng kiểm tra DTO/Service |
| `phone` | `phone: String` | `phone` | Bắt buộc, 10 chữ số |
| `role` | `role: UserRole` | `role` | ROLE_ADMIN, ROLE_ACCOUNTANT, ROLE_CASHIER, ROLE_RESIDENT |
| `failed_attempts` | `failedAttempts: int` | Nội bộ | ≥0, mặc định 0 |
| `locked_until` | `lockedUntil: OffsetDateTime` | Nội bộ | NULL khi không có hạn khóa |
| `is_active` | `active: boolean` | `active` | Mặc định true; độc lập với khóa tạm |

Tạo tài khoản nhận `password`, không nhận `passwordHash`. Login dùng `username`, `password`; đổi mật khẩu dùng `currentPassword`, `newPassword`, `confirmPassword`. Ba trường mật khẩu không trở thành cột mới. CSRF dùng tên do Spring Security cung cấp; URL/method do Hoà và Đức Anh thống nhất. Không phơi trạng thái khóa qua thông báo login.

### 3.2. Hồ sơ cá nhân — `persons` → `Person`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO/form hồ sơ chủ hộ | Ràng buộc |
|---|---|---|---|
| `full_name` | `fullName: String` | `owner.fullName` | Bắt buộc, không rỗng, ≤100 |
| `national_id` | `nationalId: String` | `owner.nationalId` | NULL hoặc 12 chữ số, duy nhất; chủ hộ bắt buộc |
| `phone` | `phone: String` | `owner.phone` | NULL hoặc 10 chữ số; chủ hộ bắt buộc |
| `email` | `email: String` | `owner.email` | NULL hoặc không rỗng, ≤100; định dạng kiểm tra DTO/Service |
| `date_of_birth` | `dateOfBirth: LocalDate` | `dateOfBirth` ở DTO cư dân | NULL trong SQL; Service khai báo cư dân yêu cầu ngày sinh |
| `gender` | `gender: Gender` | `gender` ở DTO cư dân | NULL hoặc MALE, FEMALE, OTHER |

`persons` là nguồn hồ sơ dùng chung, không phải tài khoản đăng nhập. `owner.fullName` là đường dẫn thuộc tính lồng trong DTO, không phải tên cột SQL hoặc thuộc tính Java chứa dấu chấm. Đã có CCCD thì tìm hồ sơ hiện có, không tạo người trùng hay tự ghi đè hồ sơ khác.

### 3.3. Căn hộ — `apartments` → `Apartment`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO/form | Ràng buộc |
|---|---|---|---|
| `room_number` | `roomNumber: String` | `roomNumber` | Bắt buộc, duy nhất, không rỗng, ≤20 |
| `floor_number` | `floorNumber: Integer` | `floorNumber` | Bắt buộc, 1–30 |
| `area_sqm` | `areaSqm: BigDecimal` | `areaSqm` | DECIMAL(12,2), >0 |
| `apartment_type` | `apartmentType: ApartmentType` | `apartmentType` | T1 KIOT; T2–5 COMMERCIAL; T6–29 RESIDENTIAL; T30 PENTHOUSE |
| `status` | `status: ApartmentStatus` | `status` | VACANT, OCCUPIED, RENOVATING; mặc định VACANT |
| `description` | `description: String` | `description` | TEXT, NULL |

Mã phòng `roomNumber` khác ID `id`. DTO tổng hợp dùng `apartmentId` khi cần chỉ rõ đối tượng.

### 3.4. Hộ gia đình — `households` → `Household`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO | Ràng buộc |
|---|---|---|---|
| `apartment_id` | `apartment: Apartment` | `apartmentId: Long` | FK bắt buộc |
| `head_person_id` | `headPerson: Person` | `headPersonId: Long` (đọc/tham chiếu) | FK bắt buộc; hồ sơ có CCCD và SĐT |
| `registration_code` | `registrationCode: String` | `registrationCode` | Bắt buộc, duy nhất, không rỗng, ≤50; Service cấp mã |
| `moved_in_date` | `movedInDate: LocalDate` | `movedInDate` | Bắt buộc, mặc định ngày hiện tại |
| `status` | `status: HouseholdStatus` | `status` | ACTIVE, MOVED_OUT, TRANSFERRED; mặc định ACTIVE |

Tối đa một hộ ACTIVE/căn qua partial unique index. Tên đại diện đọc qua `headPerson.fullName`, không có cột `head_name`. `householdId` trong DTO tổng hợp ánh xạ `households.id`; không nhầm với `headPersonId`. API tạo/thay chủ dùng nhóm `owner.*`; Service giải quyết ID, không tin FK do trình duyệt gửi.

### 3.5. Cư trú — `residents` → `Resident`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO | Ràng buộc |
|---|---|---|---|
| `person_id` | `person: Person` | `personId: Long` | FK bắt buộc, UNIQUE: một hồ sơ cư trú/người |
| `household_id` | `household: Household` | `householdId: Long` | FK bắt buộc |
| `relationship_with_head` | `relationshipWithHead: String` | `relationshipWithHead` | Bắt buộc, không rỗng, ≤50 |
| `residence_status` | `residenceStatus: ResidenceStatus` | `residenceStatus` | THUONG_TRU, TAM_TRU, TAM_VANG; mặc định THUONG_TRU |
| `start_date` | `startDate: LocalDate` | `startDate` | NULL với thường trú; bắt buộc với tạm trú/vắng |
| `end_date` | `endDate: LocalDate` | `endDate` | Tạm trú/vắng: bắt buộc và ≥ startDate |
| `notes` | `notes: String` | `notes` | TEXT, NULL |

Đọc họ tên/CCCD/SĐT qua `person`; không có bản sao các cột này trong `residents`. FK này là duy nhất nên trong JPA là tham chiếu một–một từ Resident tới Person; phía Person có thể chưa có Resident. Danh mục mã quan hệ chưa chốt: các nhãn trong seed chỉ là mẫu, không tự coi là enum chính thức.

### 3.6. Lịch sử — `apartment_owner_history` → `ApartmentOwnerHistory`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO đọc | Ràng buộc |
|---|---|---|---|
| `apartment_id` | `apartment: Apartment` | `apartmentId: Long` | FK bắt buộc |
| `household_id` | `household: Household` | `householdId: Long` | FK bắt buộc, phải thuộc căn trong sự kiện |
| `previous_owner_id` | `previousOwner: Person` | `previousOwnerId: Long` | FK, NULL khi gán lần đầu |
| `new_owner_id` | `newOwner: Person` | `newOwnerId: Long` | FK bắt buộc |
| `event_type` | `eventType: OwnerHistoryEventType` | `eventType` | ASSIGNED, REPLACED, PROFILE_UPDATED |
| `previous_snapshot` | `previousSnapshot: JsonNode` | `previousOwner` | JSONB, NULL khi gán lần đầu |
| `new_snapshot` | `newSnapshot: JsonNode` | `newOwner` | JSONB object bắt buộc |
| `changed_at` | `changedAt: OffsetDateTime` | `changedAt` | Bắt buộc, mặc định thời điểm transaction |
| `changed_by_user_id` | `changedByUser: User` | `changedByUserId: Long` | FK bắt buộc; lấy từ phiên đăng nhập |

Snapshot giữ khóa JSON `full_name`, `national_id`, `phone`, `email` theo schema; DTO trình bày đổi thành `fullName`, `nationalId`, `phone`, `email`. Ánh xạ JSONB trong JPA dùng `@JdbcTypeCode(SqlTypes.JSON)` trên JsonNode; không lưu JSON thành chuỗi bằng cách đoán kiểu cột. Lịch sử hiển thị thông tin từ snapshot, không lấy hồ sơ hiện tại thay thông tin tại thời điểm sự kiện.

ASSIGNED không có chủ trước; REPLACED có hai ID khác nhau; PROFILE_UPDATED có hai ID bằng nhau. Service ghi lịch sử và thay đổi hồ sơ/hộ trong cùng transaction. Không nhận snapshot hoặc người thực hiện tùy ý từ form; không sửa/xóa lịch sử.

### 3.7. Nhật ký — `audit_logs` → `AuditLog`

| Cột SQL | Thuộc tính Entity / kiểu Java | DTO | Ràng buộc |
|---|---|---|---|
| `user_id` | `user: User` | `userId: Long` (đọc) | FK, NULL nếu chưa xác định tài khoản |
| `action` | `action: String` | Đọc | Bắt buộc, không rỗng, ≤50 |
| `entity_name` | `entityName: String` | Đọc | Bắt buộc, không rỗng, ≤50 |
| `entity_id` | `entityId: Long` | Đọc | NULL; định danh đối tượng, không phải FK đa bảng |
| `details` | `details: String` | Đọc có quyền | TEXT, NULL; không ghi mật khẩu/hash/CCCD/SĐT đầy đủ |
| `ip_address` | `ipAddress: String` | Đọc có quyền | NULL, ≤50 |
| `created_at` | `createdAt: OffsetDateTime` | Đọc | Bắt buộc, mặc định thời điểm transaction |

Nhật ký chỉ ghi thêm, không nhận dữ liệu trực tiếp từ form và không có `updated_at`.

## 4. Thời gian và bàn giao

`users`, `persons`, `apartments`, `households`, `residents` đều có `created_at` → `createdAt`, `updated_at` → `updatedAt`, kiểu OffsetDateTime, NOT NULL. DEFAULT điền khi INSERT bỏ qua cột; trigger giữ createdAt và cập nhật updatedAt khi UPDATE. Không nhận timestamps từ form. Khi DB quản lý thời gian, JPA phải không tự ghi đè timestamps; refresh khi cần đọc ngay giá trị do trigger tạo.

Lịch sử dùng `changedAt`; audit dùng `createdAt`. CURRENT_TIMESTAMP lấy thời điểm đầu transaction, không bảo đảm mỗi câu lệnh có timestamp khác nhau.

Trước bàn giao: đối chiếu schema và DTO/form; kiểm tra enum, NULL, FK, bảo vệ bí mật; có test liên quan và review chéo Sơn–Hoà. Thay tên/cấu trúc phải cập nhật đồng bộ tài liệu này, schema, seed và nơi sử dụng. Không suy từ bảng ánh xạ rằng chức năng đã triển khai hoặc nghiệm thu.
