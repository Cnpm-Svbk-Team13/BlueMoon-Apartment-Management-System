# 📋 DANH MỤC THẺ GITHUB ISSUES BACKLOG — SPRINT 1

> **Điều chỉnh 01/11/2026:** Có 24 gói cha, **138 giờ / 36 SP**; bảng phần việc cuối tài liệu là đơn vị giao 2–4 giờ. `Issue #xx` là số đề xuất nội bộ, chưa phải ID GitHub thực. Sơn là trưởng nhóm/DB/Backend; Khánh vận hành Sprint và phụ trách BA/UAT. Review theo [quy chuẩn hiện hành](../../PROJECT_CONVENTIONS.md).
## Dự án: BlueMoon BFMS (IT3180 - Team 13)
### Định dạng chuẩn hóa theo Quy tắc Quản trị Dự án `AGENTS.md` (Mục 7)

> **Hướng dẫn sử dụng:**
> Khánh phụ trách điều phối (mỗi thành viên tự cập nhật task của mình) copy từng mục dưới đây để tạo Issue mới trong tab **Issues** trên GitHub repository của nhóm, sau đó gắn thẻ tương ứng lên cột **To Do** trên bảng **GitHub Projects (Kanban)**.

---

<a id="pm-01"></a>

### Issue #01: `[Sprint 1][PM] Vận hành Sprint 1 & Quản trị Tiến độ Kanban`
- **Người phụ trách (Assignee):** Phạm Nam Khánh (TV1; điều phối Sprint/BA/UAT)
- **Ước lượng:** 6 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-01-scrum-kanban-setup`
- **Traceability:** Links to `PRJ-BLUEMOON-2026` / WBS Task 1.3
- **Layer Impact:** Docs / Project Governance
- **Prerequisites:** Không có
- **Acceptance Criteria (Checklist):**
  - [ ] Bảng GitHub Projects được thiết lập đầy đủ 4 cột: `Backlog`, `To Do`, `In Progress`, `Done`.
  - [ ] Toàn bộ gói cha và phần việc 2–4 giờ từ backlog được nạp lên bảng và gán Assignee chính xác cho 5 thành viên.
  - [ ] Tổ chức Daily Standup 10 phút hàng ngày, ghi nhận tiến độ và phụ thuộc/trở ngại theo Burndown Chart; mỗi thành viên tự cập nhật task. Khánh chuyển vướng kỹ thuật cho Sơn/Hoà và đề xuất đổi phạm vi/ưu tiên cho Sơn chốt sau trao đổi với nhóm.
  - [ ] Khánh chủ trì Sprint Review/Retrospective và lập biên bản ngày 06/11; người triển khai trình bày sản phẩm. Sprint Review ở PM-01 là tổ chức họp, không phải review mã nguồn.

---

<a id="pm-02"></a>

### Issue #02: `[Sprint 1][BA] Xây dựng 12 Kịch bản UAT & Báo cáo Nghiệm thu Web`
- **Người phụ trách (Assignee):** Phạm Nam Khánh (TV1; BA/UAT, Sơn nghiệm thu nội bộ)
- **Ước lượng:** 14 giờ (3 SP)
- **Mã nhánh Git:** `feature/issue-02-uat-scenarios-and-testing`
- **Traceability:** Links to `RAW-001`, `RAW-002`, `NFR-02`, `US-001`, `US-SEC-02`
- **Layer Impact:** Docs / UAT Testing
- **Prerequisites:** AC đã chốt để soạn kịch bản ngay; thực thi UAT sau #12, #14, #20, #21, #22
- **Acceptance Criteria (Checklist):**
  - [ ] Tệp `docs/sprints/sprint-1/UAT_SCENARIOS_SPRINT_1.md` gồm 12 kịch bản nền và các kịch bản chủ hộ BA-03 chi tiết được commit lên Git.
  - [ ] Thực hiện kiểm thử thủ công trực tiếp trên trình duyệt web cho chức năng Đăng nhập và CRUD Căn hộ.
  - [ ] Bắt và ghi nhận các lỗi hiển thị tiếng Việt, lỗi validate form gửi cho Dev sửa.
  - [ ] Tệp biên bản nghiệm thu `docs/sprints/sprint-1/UAT_REPORT_SPRINT_1.md` được ký xác nhận đạt chuẩn.

---

<a id="db-01"></a>

### Issue #03: `[Sprint 1][DB] Soạn thảo Kịch bản Migration CSDL V1.1`
- **Người phụ trách (Assignee):** Nguyễn Xuân Sơn (TV2)
- **Ước lượng:** 7 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-03-db-migration-v1-1`
- **Traceability:** Links to `RAW-006`, `RAW-011`, `RAW-022`, `Bất biến 1, 2, 3 AGENTS.md`
- **Layer Impact:** Database (PostgreSQL DDL/DML)
- **Prerequisites:** Database schema gốc `database/schema.sql`
- **Acceptance Criteria (Checklist):**
  - [ ] Kịch bản `database/migrations/V1.1__fix_schema_and_seed.sql` chạy thành công không cảnh báo trên PostgreSQL 17 (Docker chuẩn nhóm).
  - [ ] Bổ sung `residence_status NOT NULL`, `start_date`, `end_date` vào `residents` với check constraint ngày.
  - [ ] Bổ sung `version BIGINT NOT NULL DEFAULT 0` vào `invoices` hỗ trợ khóa lạc quan.
  - [ ] Bổ sung ràng buộc XOR trên `receipts` chống in gộp biên lai quỹ và phí bắt buộc.
  - [ ] Cập nhật toàn bộ mật khẩu băm BCrypt chuẩn cho `Password@123` trong seed data.

---

<a id="be-01"></a>

### Issue #04: `[Sprint 1][BE] Xây dựng Tầng JPA Entities & Repositories`
- **Người phụ trách (Assignee):** Nguyễn Xuân Sơn (TV2)
- **Ước lượng:** 9 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-04-jpa-entities-and-repositories`
- **Traceability:** Links to `US-001`, `RAW-001`, `Bất biến 2 AGENTS.md`
- **Layer Impact:** Entity / Repository
- **Prerequisites:** Kịch bản CSDL từ Issue #03
- **Acceptance Criteria (Checklist):**
  - [ ] Ánh xạ đầy đủ JPA Entities: `Apartment`, `Household`, `Resident` và Enums `ApartmentType`, `ApartmentStatus`.
  - [ ] Diện tích căn hộ `areaSqm` bắt buộc dùng kiểu `BigDecimal` với `@DecimalMin("0.01")`.
  - [ ] Số tầng được giới hạn `@Min(1)` và `@Max(30)`.
  - [ ] Các Repositories kế thừa `JpaRepository` có sẵn các query methods: `findByRoomNumber`, `existsByRoomNumber`, `findByFloorNumber`.

---

<a id="be-02"></a>

### Issue #05: `[Sprint 1][BE] Hiện thực hóa Backend CRUD Căn hộ US-001`
- **Người phụ trách (Assignee):** Nguyễn Xuân Sơn (TV2)
- **Ước lượng:** 9 giờ (3 SP)
- **Mã nhánh Git:** `feature/issue-05-apartment-crud-service`
- **Traceability:** Links to `US-001`, `RAW-001`, `RAW-002`
- **Layer Impact:** Service / Controller
- **Prerequisites:** Issue #04 (Entities & Repositories)
- **Acceptance Criteria (Checklist):**
  - [ ] `ApartmentService` đóng gói 100% logic kiểm tra phân tầng: T1 là Kiot, T2-5 là Thương mại, T6-29 là Căn hộ ở, T30 là Penthouse.
  - [ ] Chặn triệt để diện tích $\le 0$ và chặn trùng lặp mã phòng.
  - [ ] Thao tác thêm/sửa bọc trong `@Transactional(rollbackFor = Exception.class)`.
  - [ ] Thao tác đọc bọc trong `@Transactional(readOnly = true)`.
  - [ ] Đơn vị kiểm thử (Unit test) của Huy pass xanh 100%.

---

<a id="arch-01"></a>

### Issue #06: `[Sprint 1][ARCH] Khởi tạo Khung Kiến trúc Spring Boot 3.5.16 & Pure Web`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 5 giờ (1 SP)
- **Mã nhánh Git:** `feature/issue-06-architecture-framework-setup`
- **Traceability:** Links to `ARCH-01`, Pure Web Invariant
- **Layer Impact:** Architecture / Config / Exception
- **Prerequisites:** `pom.xml`
- **Acceptance Criteria (Checklist):**
  - [ ] Ứng dụng biên dịch thành công với lệnh `mvn clean compile` trên Java 25 LTS.
  - [ ] Cấu hình kết nối HikariCP kết nối thông suốt tới PostgreSQL.
  - [ ] `GlobalExceptionHandler` bắt và chuyển đổi ngoại lệ validation `@Valid` thành thông báo lỗi thân thiện.
  - [ ] Cấu trúc package tuân thủ nghiêm ngặt mô hình Pure Web 4 tầng.

---

<a id="sec-01"></a>

### Issue #07: `[Sprint 1][SEC] Cấu hình Spring Security 6 & Quản trị Phiên`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 6 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-07-spring-security-config`
- **Traceability:** Links to `NFR-02`, `US-SEC-02`
- **Layer Impact:** Security / Config
- **Prerequisites:** Issue #06 (Architecture Setup)
- **Acceptance Criteria (Checklist):**
  - [ ] `SecurityFilterChain` chặn toàn bộ truy cập trái phép vào các route `/admin/**`.
  - [ ] Cho phép truy cập công khai tài nguyên tĩnh (`/css/**`, `/js/**`, `/images/**`) và màn hình `/login`.
  - [ ] Khai báo Bean `BCryptPasswordEncoder` độ phức tạp 10 rounds.
  - [ ] Bật cờ bảo vệ CSRF trên các form POST.

---

<a id="sec-02"></a>

### Issue #08: `[Sprint 1][SEC] Hiện thực hóa AuthService & Khóa 15 Phút sau 5 Lần Sai`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 8 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-08-auth-service-and-lockout`
- **Traceability:** Links to `US-SEC-02`, `NFR-02`
- **Layer Impact:** Service / Security
- **Prerequisites:** Issue #07 (Security Config)
- **Acceptance Criteria (Checklist):**
  - [ ] Khớp 100% logic Đồ thị CFG theo mã nguồn đã chốt và các Basis Paths theo CFG đã chốt.
  - [ ] Nhập sai liên tiếp 5 lần $\rightarrow$ Tự động gán `locked_until = now() + 15m`.
  - [ ] Khi hết hạn 15 phút $\rightarrow$ Tự động reset bộ đếm `failed_attempts = 0`.
  - [ ] Tài khoản có `is_active = false` bị chặn ngay lập tức, không băm BCrypt mật khẩu.
  - [ ] Đăng nhập thành công $\rightarrow$ Reset `failed_attempts = 0` và `locked_until = null`.

---

<a id="sec-03"></a>

### Issue #09: `[Sprint 1][SEC] Hiện thực hóa UserService & Quản lý Tài khoản BQT`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 6 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-09-user-service-crud`
- **Traceability:** Links to `US-SEC-01`, `NFR-02`
- **Layer Impact:** Service / Controller
- **Prerequisites:** Issue #08 (Auth Service)
- **Acceptance Criteria (Checklist):**
  - [ ] Cán bộ quản trị (Admin) tạo được tài khoản cho Kế toán, Thủ quỹ.
  - [ ] Kiểm tra tính duy nhất của `username` và `email`.
  - [ ] Có chức năng kích hoạt / vô hiệu hóa trạng thái hoạt động của tài khoản.
  - [ ] Thao tác ghi dữ liệu bọc trong `@Transactional(rollbackFor = Exception.class)`.

---

<a id="sec-04"></a>

### Issue #10: `[Sprint 1][QA] Review chéo cùng Sơn & Thẩm định chất lượng PR`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 3 giờ (1 SP)
- **Mã nhánh Git:** Duyệt trên GitHub PRs
- **Traceability:** US-001, US-SEC-01/02 / NFR-02, NFR-03; DoD IT3180
- **Layer Impact:** Quality Governance
- **Prerequisites:** Có PRs mở từ các thành viên khác
- **Acceptance Criteria (Checklist):**
  - [ ] PR Sơn do Hoà duyệt; PR Hoà do Sơn duyệt; PR khác có một trong hai review trước khi merge vào `develop`.
  - [ ] Kiểm tra không để lọt SQL hoặc business logic vào Controller hay View.
  - [ ] Kiểm tra verify/CI/test liên quan trước review; không tự duyệt; nghiệm thu chạy profile sprint1-quality.

---

<a id="fe-01"></a>

### Issue #11: `[Sprint 1][FE] Xây dựng Master Layout Thymeleaf Chuẩn mực`
- **Người phụ trách (Assignee):** Nghiêm Đức Anh (TV4)
- **Ước lượng:** 6 giờ (1.5 SP)
- **Mã nhánh Git:** `feature/issue-11-master-layout-thymeleaf`
- **Traceability:** Links to `FE-01`, Deliverable D4
- **Layer Impact:** View / CSS / JS
- **Prerequisites:** Khung thư mục templates
- **Acceptance Criteria (Checklist):**
  - [ ] Tệp `templates/fragments/layout.html` hoàn chỉnh với Header, Sidebar và Footer.
  - [ ] Hiển thị thông tin người dùng đang đăng nhập và vai trò tương ứng.
  - [ ] Sidebar có đủ các menu điều hướng các phân hệ chức năng.
  - [ ] Giao diện responsive trên Desktop và Tablet.

---

<a id="fe-02"></a>

### Issue #12: `[Sprint 1][FE] Xây dựng Giao diện Màn hình Đăng nhập`
- **Người phụ trách (Assignee):** Nghiêm Đức Anh (TV4)
- **Ước lượng:** 6 giờ (1.5 SP)
- **Mã nhánh Git:** `feature/issue-12-login-ui-screen`
- **Traceability:** Links to `US-SEC-02`, Deliverable D4
- **Layer Impact:** View / Auth UI
- **Prerequisites:** Issue #11 (Master Layout)
- **Acceptance Criteria (Checklist):**
  - [ ] Màn hình `templates/auth/login.html` có form POST tương thích Spring Security.
  - [ ] Tích hợp token chống CSRF hợp lệ.
  - [ ] Hiển thị lỗi chung bằng tiếng Việt, không tiết lộ username/trạng thái khóa.
  - [ ] Hiển thị thông báo màu đỏ khi tài khoản bị khóa tạm thời 15 phút.

---

<a id="fe-03"></a>

### Issue #13: `[Sprint 1][FE] Xây dựng Màn hình Quản lý Tài khoản BQT`
- **Người phụ trách (Assignee):** Nghiêm Đức Anh (TV4)
- **Ước lượng:** 6 giờ (1.5 SP)
- **Mã nhánh Git:** `feature/issue-13-admin-users-ui`
- **Traceability:** Links to `US-SEC-01`, Deliverable D4
- **Layer Impact:** View / Admin UI
- **Prerequisites:** Issue #11 (Master Layout)
- **Acceptance Criteria (Checklist):**
  - [ ] Bảng danh sách cán bộ hiển thị đầy đủ thông tin, badge vai trò và trạng thái.
  - [ ] Modal tạo mới tài khoản BQT có validate dữ liệu đầu vào.
  - [ ] Nút đổi trạng thái Kích hoạt / Vô hiệu hóa hoạt động mượt mà.

---

<a id="fe-04"></a>

### Issue #14: `[Sprint 1][FE] Xây dựng Màn hình Dashboard & Danh mục Căn hộ`
- **Người phụ trách (Assignee):** Nghiêm Đức Anh (TV4)
- **Ước lượng:** 6 giờ (1.5 SP)
- **Mã nhánh Git:** `feature/issue-14-apartments-ui`
- **Traceability:** Links to `US-001`, `RAW-001`, Deliverable D4
- **Layer Impact:** View / Apartment UI
- **Prerequisites:** Issue #11 (Master Layout)
- **Acceptance Criteria (Checklist):**
  - [ ] `templates/admin/dashboard.html` hiển thị các thẻ thống kê căn hộ.
  - [ ] `templates/admin/apartments.html` hiển thị danh sách căn hộ theo tầng.
  - [ ] Modal thêm căn hộ có trường diện tích $m^2$ chặn số âm phía client (`min="0.01"`).
  - [ ] Hiển thị thông báo thành công hoặc lỗi từ Controller trả về.

---

<a id="qa-01"></a>

### Issue #15: `[Sprint 1][QA] Thiết lập Môi trường Test & Dữ liệu Mẫu Shift-Left`
- **Người phụ trách (Assignee):** Nguyễn Hợp Huy (TV5)
- **Ước lượng:** 4 giờ (1 SP)
- **Mã nhánh Git:** `feature/issue-15-test-setup-shift-left`
- **Traceability:** Links to `QA-01`, Deliverable D5
- **Layer Impact:** Test Framework
- **Prerequisites:** Issue #06 (Architecture Setup)
- **Acceptance Criteria (Checklist):**
  - [ ] Cấu hình `src/test/resources/application-test.yml` chạy độc lập với H2 in-memory.
  - [ ] Lệnh `mvn test` chạy sạch sẽ không cần kết nối tới PostgreSQL thật.
  - [ ] Dữ liệu fixture mẫu sẵn sàng cho các ca kiểm thử Service và Controller.

---

<a id="qa-02"></a>

### Issue #16: `[Sprint 1][QA] Lập trình Ca Kiểm thử CFG Basis Path cho Đăng nhập`
- **Người phụ trách (Assignee):** Nguyễn Hợp Huy (TV5)
- **Ước lượng:** 6 giờ (1 SP)
- **Mã nhánh Git:** `feature/issue-16-auth-cfg-basis-path-tests`
- **Traceability:** Links to `US-SEC-02`, SoICT Chapter 8
- **Layer Impact:** Unit Test (JUnit 5 / Mockito)
- **Prerequisites:** Thiết kế test từ AC ngay; chạy test sau #08 và #15
- **Acceptance Criteria (Checklist):**
  - [ ] Dựng trước khung Test Stubs từ Ngày 2.
  - [ ] Bao phủ 100% các Basis Paths theo CFG đã chốt của phương thức `authenticate()`:
    * `TC_LOGIN_BP_01`: User không tồn tại $\rightarrow$ `USER_NOT_FOUND`.
    * `TC_LOGIN_BP_02`: Đang bị khóa $\rightarrow$ `ACCOUNT_LOCKED`.
    * `TC_LOGIN_BP_03`: Sai lần thứ 5 $\rightarrow$ `LOCKED_NOW`.
    * `TC_LOGIN_BP_04`: Sai dưới 5 lần $\rightarrow$ `BAD_CREDENTIALS`.
    * `TC_LOGIN_BP_05`: Đúng mật khẩu $\rightarrow$ `SUCCESS`.
  - [ ] Bao phủ tài khoản vô hiệu hóa, reset bộ đếm, thử trước/đúng/sau hạn khóa theo tài liệu CFG.
  - [ ] 100% ca kiểm thử pass xanh.

---

<a id="qa-03"></a>

### Issue #17: `[Sprint 1][QA] Lập trình Ca Kiểm thử Tham số hóa Bảng Quyết định Căn hộ`
- **Người phụ trách (Assignee):** Nguyễn Hợp Huy (TV5)
- **Ước lượng:** 5 giờ (1 SP)
- **Mã nhánh Git:** `feature/issue-17-apartment-decision-table-tests`
- **Traceability:** Links to `US-001`, SoICT Chapter 9
- **Layer Impact:** Parameterized Unit Test
- **Prerequisites:** Issue #05 (ApartmentService) & Issue #15 (Test Setup)
- **Acceptance Criteria (Checklist):**
  - [ ] Áp dụng `@ParameterizedTest` bao phủ đầy đủ 6 Rules $R_1 \rightarrow R_6$ của Bảng quyết định.
  - [ ] Bắt chính xác ngoại lệ khi trùng mã phòng (R1).
  - [ ] Bắt chính xác ngoại lệ khi tầng $< 1$ hoặc $> 30$ (R2).
  - [ ] Bắt chính xác ngoại lệ khi diện tích $\le 0$ (R3).
  - [ ] Bắt chính xác ngoại lệ khi xung đột tầng và loại hình (R5).
  - [ ] Dữ liệu hợp lệ lưu thành công vào CSDL (R6).

---

<a id="qa-04"></a>

### Issue #18: `[Sprint 1][QA] Kiểm thử Tích hợp MockMvc & Báo cáo Đo lường Chất lượng`
- **Người phụ trách (Assignee):** Nguyễn Hợp Huy (TV5)
- **Ước lượng:** 5 giờ (1 SP)
- **Mã nhánh Git:** `feature/issue-18-mockmvc-and-coverage-report`
- **Traceability:** Links to `QA-04`, SoICT DoD
- **Layer Impact:** Integration Test / Quality Report
- **Prerequisites:** Controller hoàn thành từ Issue #05, Issue #09
- **Acceptance Criteria (Checklist):**
  - [ ] Bộ ca kiểm thử `MockMvc` cho form đăng nhập và chặn các route yêu cầu xác thực.
  - [ ] Đo lường độ bao phủ dòng lệnh (Line Coverage) đạt $\ge 85\%$ cho tầng Service và Controller.
  - [ ] Xuất báo cáo tổng kết chất lượng Sprint 1: `docs/sprints/sprint-1/SPRINT_1_TEST_REPORT.md`.


## Điều chỉnh Sprint 1: chủ hộ, review và ước lượng

PM-01 (6h/2 SP) do Khánh phụ trách vận hành Sprint, bên cạnh PM-02 (14h/3 SP) và BA-03 (2h/1 SP). Sơn tập trung DB/Backend, chốt phạm vi/ưu tiên sau trao đổi nhóm và xác nhận nghiệm thu nội bộ. SEC-04 của Hoà vẫn 3h/1 SP; PM-03 bổ sung 3h review của Sơn. Thêm 18h/5 SP chủ hộ, tổng 138h/36 SP. Đây là nhu cầu ước lượng, cần theo dõi năng lực mỗi ngày để hoàn thành trong 31/10–06/11.

<a id="db-02"></a>

### Issue #19: `[Sprint 1][DB] Chuẩn bị dữ liệu hồ sơ và lịch sử chủ hộ` (`DB-02`)

- **Người phụ trách:** Sơn
- **Ước lượng:** 4 giờ (1 SP)
- **Traceability:** US-001 / RAW-002 / AC 3–4; DB-01, BE-01
- **Layer Impact:** Database / Entity
- **Prerequisites:** #03, #04
- **Acceptance Criteria (Checklist):**
  - [ ] Thiết kế trường họ tên, CCCD, SĐT, email chủ hộ và lịch sử có thời điểm/người thay đổi; cập nhật DDL/migration và ERD.
  - [ ] Giữ tối đa một hộ ACTIVE/căn hộ và CCCD duy nhất toàn cục; kiểm tra với PostgreSQL cô lập.
  - [ ] Giữ dữ liệu chủ hộ cũ khi thay chủ; không xóa cứng. Ghi rõ cách liên kết hồ sơ với households/residents, tránh hai nguồn định danh khác nhau.

<a id="be-03"></a>

### Issue #20: `[Sprint 1][BE] Thay chủ hộ và lưu lịch sử trong một giao dịch` (`BE-03`)

- **Người phụ trách:** Sơn
- **Ước lượng:** 4 giờ (1 SP)
- **Traceability:** US-001 / RAW-002 / AC 3–4; BE-02, DB-02
- **Layer Impact:** DTO / Service / Controller
- **Prerequisites:** #05, #19
- **Acceptance Criteria (Checklist):**
  - [ ] Tạo/cập nhật hồ sơ chủ hộ: họ tên bắt buộc, CCCD 12 chữ số, SĐT 10 chữ số, email hợp lệ nếu có.
  - [ ] Thay chủ hộ ghi lịch sử và người/thời điểm đổi; giữ đúng một hộ ACTIVE, không sửa mất hồ sơ cũ.
  - [ ] Dùng transaction để lỗi giữa chừng rollback cả đổi chủ và lịch sử; có unit test chính/biên, test rollback và cạnh tranh trên PostgreSQL ở #22.

<a id="fe-05"></a>

### Issue #21: `[Sprint 1][FE] Bổ sung form chủ hộ và màn hình lịch sử` (`FE-05`)

- **Người phụ trách:** Đức Anh
- **Ước lượng:** 4 giờ (1 SP)
- **Traceability:** US-001 / RAW-002 / AC 3–4; FE-04
- **Layer Impact:** View / DTO binding
- **Prerequisites:** #14; hợp đồng form #20 (ghép dữ liệu sau #20)
- **Acceptance Criteria (Checklist):**
  - [ ] Form thêm/sửa căn hộ có hồ sơ chủ hộ, lỗi tiếng Việt từ server và CSRF.
  - [ ] Màn hình lịch sử thể hiện chủ cũ/mới và thời điểm thay; không đưa tính toán nghiệp vụ vào view.
  - [ ] Thao tác lưu hợp lệ, dữ liệu sai bị chặn phía server; test render/form và kiểm tra trình duyệt.

<a id="qa-05"></a>

### Issue #22: `[Sprint 1][QA] Kiểm thử hồ sơ và lịch sử chủ hộ` (`QA-05`)

- **Người phụ trách:** Huy
- **Ước lượng:** 4 giờ (1 SP)
- **Traceability:** US-001 / RAW-002 / AC 3–4; NFR-03
- **Layer Impact:** Service / Repository / Controller tests
- **Prerequisites:** Soạn từ AC ngay; chạy sau #19–#21 và #15
- **Acceptance Criteria (Checklist):**
  - [ ] Bao phủ tạo/sửa hợp lệ, thiếu tên, CCCD/SĐT sai độ dài, CCCD trùng, email sai.
  - [ ] Đổi chủ giữ lịch sử; cố tình gây lỗi để xác nhận rollback; hai lần đổi đồng thời không tạo hai hộ ACTIVE (PostgreSQL).
  - [ ] Test web kiểm tra dữ liệu đã lưu và lịch sử hiển thị; ghi kết quả thực chạy, không thay bằng khung test rỗng.

<a id="ba-03"></a>

### Issue #23: `[Sprint 1][BA] Bổ sung kịch bản UAT chủ hộ` (`BA-03`)

- **Người phụ trách:** Khánh
- **Ước lượng:** 2 giờ (1 SP)
- **Traceability:** US-001 / RAW-002 / AC 3–4; PM-02
- **Layer Impact:** Docs / UAT
- **Prerequisites:** AC đã chốt; thực thi cùng PM-02 sau #20–#22
- **Acceptance Criteria (Checklist):**
  - [ ] Thêm kịch bản hồ sơ hợp lệ, dữ liệu sai và thay chủ/lịch sử vào bộ 12 kịch bản nền; không giới hạn tổng UAT ở 12.
  - [ ] Mỗi kịch bản ghi AC, dữ liệu giả, bước làm, kỳ vọng và kết quả thực chạy; chuẩn bị từ ngày 1.
  - [ ] Khánh ghi lỗi/kết quả, Sơn nghiệm thu nội bộ khi đủ AC; không ghi khách hàng đã ký nếu chưa có bằng chứng.

<a id="pm-03"></a>

### Issue #24: `[Sprint 1][Review] Review chéo và nghiệm thu tích hợp Sprint` (`PM-03`)

- **Người phụ trách:** Sơn
- **Ước lượng:** 3 giờ (1 SP)
- **Traceability:** US-001, US-SEC-01, US-SEC-02 / NFR-02, NFR-03; SEC-04
- **Layer Impact:** Technical Review / Internal Acceptance
- **Prerequisites:** Thực hiện xuyên tuần; nghiệm thu sau test/UAT
- **Acceptance Criteria (Checklist):**
  - [ ] PR Hoà do Sơn duyệt; PR Sơn do Hoà duyệt; PR khác có ít nhất một người duyệt, không tự duyệt.
  - [ ] Sơn kiểm tra CI, truy vết và bằng chứng kiểm thử do Huy cung cấp, hồ sơ/UAT do Khánh tổng hợp; không làm lại báo cáo của Huy/Khánh và không coi CI xanh nền tảng là hoàn thành nghiệp vụ.
  - [ ] Xác nhận nghiệm thu dựa trên clean verify -Psprint1-quality và đủ AC/test/UAT; ghi giới hạn chưa kiểm tra, task thiếu bằng chứng vẫn mở.

### Bảng tổng cập nhật

Lưu ý kiểm tổng ngày 01/11/2026: các gói của Sơn là DB-01 (2 SP), BE-01 (2 SP), BE-02 (3 SP), DB-02 (1 SP), BE-03 (1 SP), PM-03 (1 SP), tổng **10 SP**. Con số 11 SP trong phương án chuyển PM-01 là lỗi cộng kế thừa từ bảng cũ; giữ nguyên SP từng gói để tổng đội đúng 36 SP.

| Thành viên | Giờ | SP | Gói cha |
|---|---:|---:|---|
| Khánh (điều phối/BA/UAT) | 22 | 6 | PM-01, PM-02, BA-03 |
| Sơn (trưởng nhóm/DB/BE) | 36 | 10 | PM-03, DB-01, BE-01, BE-02, DB-02, BE-03 |
| Hoà (Tech Lead) | 28 | 8 | ARCH-01, SEC-01…04 |
| Đức Anh (UI) | 28 | 7 | FE-01…05 |
| Huy (QA) | 24 | 5 | QA-01…05 |
| **Tổng** | **138** | **36** | **24 gói cha** |

<a id="work-units"></a>

### Đơn vị giao việc 2–4 giờ

Các hàng là phần việc con của gói cha, kế thừa người phụ trách, truy vết, layer và AC ở thẻ tương ứng. Không tạo issue cha 14h rồi đợi đến cuối tuần. SP đánh giá ở cấp gói, không quy đổi SP sang giờ.

| Gói cha | Phần việc con (giờ) | Phụ thuộc bàn giao |
|---|---|---|
| PM-01 (6h) | Kanban/lịch họp (3); theo dõi tiến độ/tổ chức Sprint Review và Retrospective (3) | Ngày 1; xuyên tuần |
| PM-02 (14h) | Kịch bản đăng nhập (4); căn hộ/tài khoản (4); thực thi UAT (3); kết quả/lỗi (3) | Soạn từ AC ngay; chạy khi chức năng sẵn sàng |
| DB-01 (7h) | Review schema/migration hiện có (3); chạy PostgreSQL và ghi kết quả (4) | Trước ánh xạ JPA |
| BE-01 (9h) | Apartment mapping/repository (3); Household/Resident (3); query/integration check (3) | DB-01; User mapping do Hoà phối hợp SEC-03 |
| BE-02 (9h) | Quy tắc căn hộ/test (3); thêm/sửa và audit (3); web binding (3) | BE-01; chủ hộ chi tiết ở BE-03 |
| ARCH-01 (5h) | Review cấu hình/build nền tảng (2); exception handler (3) | Cấu hình nền tảng được bàn giao trong đợt này |
| SEC-01 (6h) | SecurityFilterChain/session (3); CSRF/role checks (3) | ARCH-01 |
| SEC-02 (8h) | Auth/provider integration (4); lockout/Clock/đổi mật khẩu (4) | SEC-01; test QA-02 song song |
| SEC-03 (6h) | User mapping/repository và validation (3); quản trị tài khoản/controller (3) | DB-01, SEC-01 |
| SEC-04 (3h) | Review chéo của Hoà (3) | Xuyên tuần |
| FE-01 (6h) | Layout/nav (3); responsive/fragments (3) | ARCH-01 |
| FE-02 (6h) | Form login/lỗi chung (3); ghép Security/CSRF (3) | FE-01; hợp đồng SEC-01 |
| FE-03 (6h) | Danh sách/form user (3); đổi mật khẩu/ghép backend (3) | FE-01, SEC-03 |
| FE-04 (6h) | Danh sách/filter/dashboard (3); form căn hộ/ghép backend (3) | FE-01, BE-02 |
| QA-01 (4h) | Fixtures và kiểm tra nền tảng test (4) | Smoke/CI/JaCoCo đã có cấu hình, bổ sung fixtures nghiệp vụ |
| QA-02 (6h) | Thiết kế CFG/test từ AC (3); chạy và chốt CFG theo mã (3) | SEC-02 để chạy |
| QA-03 (5h) | Decision table/biên (2); test tham số hóa (3) | BE-02 để chạy |
| QA-04 (5h) | MockMvc bảo mật/form (3); coverage và test report (2) | SEC-01…03, BE-02, FE |
| DB-02 (4h) | Hồ sơ/lịch sử chủ hộ và migration/ERD (4) | 01–02/11; DB-01 |
| BE-03 (4h) | Transaction thay chủ hộ và truy vấn lịch sử (4) | 03–04/11; DB-02 |
| FE-05 (4h) | Form chủ hộ/lịch sử và ghép dữ liệu (4) | 04–05/11; BE-03 |
| QA-05 (4h) | Test chủ hộ, rollback và concurrent update (4) | Thiết kế sớm; chạy 04–05/11 |
| BA-03 (2h) | Kịch bản UAT chủ hộ (2) | 31/10; thực thi trong PM-02 |
| PM-03 (3h) | Review của Sơn và kiểm tra nghiệm thu nội bộ (3) | Xuyên tuần; chốt 06/11 |

Tổng các phần việc con phải bằng 138h. Chuyển PM-01 không đổi tổng 36 SP/24 gói/44 phần việc; chưa xác nhận tải phù hợp khi chưa biết thời gian thực tế từng người có thể dành. Review không tự động thực hiện merge; chỉ cập nhật tài liệu trong đợt đồng bộ này, chưa tạo issue/PR trên GitHub.
