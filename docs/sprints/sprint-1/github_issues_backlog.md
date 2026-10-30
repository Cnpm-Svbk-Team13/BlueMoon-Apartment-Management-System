# 📋 DANH MỤC THẺ GITHUB ISSUES BACKLOG — SPRINT 1
## Dự án: BlueMoon BFMS (IT3180 - Team 13)
### Định dạng chuẩn hóa theo Quy tắc Quản trị Dự án `AGENTS.md` (Mục 7)

> **Hướng dẫn sử dụng:**  
> Thành viên phụ trách (hoặc Khánh - PM) copy từng mục dưới đây để tạo Issue mới trong tab **Issues** trên GitHub repository của nhóm, sau đó gắn thẻ tương ứng lên cột **To Do** trên bảng **GitHub Projects (Kanban)**.

---

### Issue #01: `[Sprint 1][PM] Vận hành Sprint 1 & Quản trị Tiến độ Kanban`
- **Người phụ trách (Assignee):** Phạm Nam Khánh (TV1)
- **Ước lượng:** 6 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-01-scrum-kanban-setup`
- **Traceability:** Links to `PRJ-BLUEMOON-2026` / WBS Task 1.3
- **Layer Impact:** Docs / Project Governance
- **Prerequisites:** Không có
- **Acceptance Criteria (Checklist):**
  - [ ] Bảng GitHub Projects được thiết lập đầy đủ 4 cột: `Backlog`, `To Do`, `In Progress`, `Done`.
  - [ ] Toàn bộ 18 issues từ backlog được nạp lên bảng và gán Assignee chính xác cho 5 thành viên.
  - [ ] Tổ chức Daily Standup 10 phút hàng ngày, ghi nhận tiến độ theo Burndown Chart.
  - [ ] Biên bản họp Sprint Review và Retrospective được lập đầy đủ vào ngày 06/11.

---

### Issue #02: `[Sprint 1][BA] Xây dựng 12 Kịch bản UAT & Báo cáo Nghiệm thu Web`
- **Người phụ trách (Assignee):** Phạm Nam Khánh (TV1)
- **Ước lượng:** 14 giờ (3 SP)
- **Mã nhánh Git:** `feature/issue-02-uat-scenarios-and-testing`
- **Traceability:** Links to `RAW-001`, `RAW-002`, `NFR-02`, `US-001`, `US-SEC-02`
- **Layer Impact:** Docs / UAT Testing
- **Prerequisites:** Giao diện hoàn thành từ Issue #12 và Issue #14
- **Acceptance Criteria (Checklist):**
  - [ ] Tệp `docs/sprints/sprint-1/UAT_SCENARIOS_SPRINT_1.md` gồm 12 kịch bản chi tiết được commit lên Git.
  - [ ] Thực hiện kiểm thử thủ công trực tiếp trên trình duyệt web cho chức năng Đăng nhập và CRUD Căn hộ.
  - [ ] Bắt và ghi nhận các lỗi hiển thị tiếng Việt, lỗi validate form gửi cho Dev sửa.
  - [ ] Tệp biên bản nghiệm thu `docs/sprints/sprint-1/UAT_REPORT_SPRINT_1.md` được ký xác nhận đạt chuẩn.

---

### Issue #03: `[Sprint 1][DB] Soạn thảo Kịch bản Migration CSDL V1.1`
- **Người phụ trách (Assignee):** Nguyễn Xuân Sơn (TV2)
- **Ước lượng:** 7 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-03-db-migration-v1-1`
- **Traceability:** Links to `RAW-006`, `RAW-011`, `RAW-022`, `Bất biến 1, 2, 3 AGENTS.md`
- **Layer Impact:** Database (PostgreSQL DDL/DML)
- **Prerequisites:** Database schema gốc `database/schema.sql`
- **Acceptance Criteria (Checklist):**
  - [ ] Kịch bản `database/migrations/V1.1__fix_schema_and_seed.sql` chạy thành công không cảnh báo trên PostgreSQL 15/16.
  - [ ] Bổ sung `residence_status NOT NULL`, `start_date`, `end_date` vào `residents` với check constraint ngày.
  - [ ] Bổ sung `version BIGINT NOT NULL DEFAULT 0` vào `invoices` hỗ trợ khóa lạc quan.
  - [ ] Bổ sung ràng buộc XOR trên `receipts` chống in gộp biên lai quỹ và phí bắt buộc.
  - [ ] Cập nhật toàn bộ mật khẩu băm BCrypt chuẩn cho `Password@123` trong seed data.

---

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

### Issue #06: `[Sprint 1][ARCH] Khởi tạo Khung Kiến trúc Spring Boot 3.4.5 & Pure Web`
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

### Issue #08: `[Sprint 1][SEC] Hiện thực hóa AuthService & Khóa 15 Phút sau 5 Lần Sai`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 8 giờ (2 SP)
- **Mã nhánh Git:** `feature/issue-08-auth-service-and-lockout`
- **Traceability:** Links to `US-SEC-02`, `NFR-02`
- **Layer Impact:** Service / Security
- **Prerequisites:** Issue #07 (Security Config)
- **Acceptance Criteria (Checklist):**
  - [ ] Khớp 100% logic Đồ thị CFG $V(G)=5$ và 5 Basis Paths.
  - [ ] Nhập sai liên tiếp 5 lần $\rightarrow$ Tự động gán `locked_until = now() + 15m`.
  - [ ] Khi hết hạn 15 phút $\rightarrow$ Tự động reset bộ đếm `failed_attempts = 0`.
  - [ ] Tài khoản có `is_active = false` bị chặn ngay lập tức, không băm BCrypt mật khẩu.
  - [ ] Đăng nhập thành công $\rightarrow$ Reset `failed_attempts = 0` và `locked_until = null`.

---

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

### Issue #10: `[Sprint 1][QA] Gatekeeping & Thẩm định 100% Pull Requests`
- **Người phụ trách (Assignee):** Phạm Thái Hoà (TV3)
- **Ước lượng:** 3 giờ (1 SP)
- **Mã nhánh Git:** Duyệt trên GitHub PRs
- **Traceability:** Links to `DoD Standard IT3180`
- **Layer Impact:** Quality Governance
- **Prerequisites:** Có PRs mở từ các thành viên khác
- **Acceptance Criteria (Checklist):**
  - [ ] 100% Pull Requests từ TV1, TV2, TV4, TV5 được review trước khi merge vào `develop`.
  - [ ] Kiểm tra không để lọt SQL hoặc business logic vào Controller hay View.
  - [ ] Kiểm tra lệnh `mvn test` thực thi pass 100% trên máy trước khi nhấn Merge.

---

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
  - [ ] Hiển thị cảnh báo số lần nhập sai còn lại bằng tiếng Việt rõ ràng.
  - [ ] Hiển thị thông báo màu đỏ khi tài khoản bị khóa tạm thời 15 phút.

---

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

### Issue #16: `[Sprint 1][QA] Lập trình Ca Kiểm thử CFG Basis Path cho Đăng nhập`
- **Người phụ trách (Assignee):** Nguyễn Hợp Huy (TV5)
- **Ước lượng:** 6 giờ (1 SP)
- **Mã nhánh Git:** `feature/issue-16-auth-cfg-basis-path-tests`
- **Traceability:** Links to `US-SEC-02`, SoICT Chapter 8
- **Layer Impact:** Unit Test (JUnit 5 / Mockito)
- **Prerequisites:** Issue #08 (AuthService) & Issue #15 (Test Setup)
- **Acceptance Criteria (Checklist):**
  - [ ] Dựng trước khung Test Stubs từ Ngày 2.
  - [ ] Bao phủ 100% 5 Basis Paths của phương thức `authenticate()`:
    * `TC_LOGIN_BP_01`: User không tồn tại $\rightarrow$ `USER_NOT_FOUND`.
    * `TC_LOGIN_BP_02`: Đang bị khóa $\rightarrow$ `ACCOUNT_LOCKED`.
    * `TC_LOGIN_BP_03`: Sai lần thứ 5 $\rightarrow$ `LOCKED_NOW`.
    * `TC_LOGIN_BP_04`: Sai dưới 5 lần $\rightarrow$ `BAD_CREDENTIALS`.
    * `TC_LOGIN_BP_05`: Đúng mật khẩu $\rightarrow$ `SUCCESS`.
  - [ ] Bao phủ 2 ca kiểm thử biên: Tài khoản vô hiệu hóa và tự động giải phóng khóa sau 15 phút.
  - [ ] 100% ca kiểm thử pass xanh.

---

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
  - [ ] Xuất báo cáo tổng kết chất lượng Sprint 1: `docs/SPRINT_1_TEST_REPORT.md`.
