# KẾ HOẠCH CHI TIẾT TRIỂN KHAI SPRINT 1 — DỰ ÁN BLUEMOON BFMS
## Phân hệ: Khởi tạo Nền tảng Kiến trúc, Database Schema & Quản lý Tài khoản BQT / Đăng nhập
### Học phần: Nhập môn Công nghệ Phần mềm (IT3180) — SoICT / HUST
**Nhóm thực hiện:** Nhóm 13 (Team 13)  
**Thời gian thực hiện Sprint 1:** 2 tuần (Từ 20/10/2026 đến 02/11/2026)  
**Phiên bản kế hoạch:** 1.0 (Official Baseline Plan)  
**Tiêu chuẩn áp dụng:** Hướng dẫn Đồ án Công nghệ Phần mềm SoICT (Chương 8 & 9), Bất biến Nghiệp vụ `AGENTS.md`

---

## 1. THÔNG TIN TỔNG QUAN SPRINT 1 (SPRINT OVERVIEW)

### 1.1. Thông tin Cơ bản
- **Tên Sprint:** Sprint 1 — Khởi tạo Nền tảng Kiến trúc Spring Boot, Chuẩn hóa Database Schema & Xây dựng Phân hệ Quản lý Tài khoản BQT / Xác thực Đăng nhập.
- **Thời lượng:** 2 tuần (10 ngày làm việc chính thức, từ Thứ Ba 20/10/2026 đến Thứ Hai 02/11/2026).
- **Quy mô Nhân sự:** 5 thành viên (100% năng lực đóng góp, ước tính 32 giờ/thành viên/Sprint $\rightarrow$ Tổng quỹ thời gian khả dụng: **160 giờ làm việc**).
- **Tổng dung lượng Story Points cam kết (Sprint Capacity):** **40 Story Points (SP)**.

### 1.2. Mục tiêu Cốt lõi của Sprint (Sprint Goal)
> **Sprint Goal:**  
> Thiết lập hoàn chỉnh khung kiến trúc dự án **Spring Boot 3.4.5 (Java 25 LTS)** theo mô hình phân tầng thuần Web (Pure Web Architecture), kết nối và hoàn thiện kịch bản CSDL **PostgreSQL** (vá triệt để các lỗi thiếu `start_date`/`end_date` và `version`), hiện thực hóa thành công phân hệ **Xác thực Đăng nhập, Cơ chế khóa Brute-force, Quản lý Tài khoản Ban Quản trị & Phân quyền RBAC**, kết hợp phân hệ **Quản lý Căn hộ & Chủ hộ** nền tảng, đáp ứng 100% tiêu chí kiểm thử và chuẩn mực học thuật của Viện CNTT&TT (SoICT / HUST).

### 1.3. Căn cứ Quy chiếu & Phạm vi Nghiệp vụ Sprint 1
Để giải quyết dứt điểm mâu thuẫn định danh và làm rõ bản chất công việc, Sprint 1 quy chuẩn phạm vi thực thi bao gồm:
1. **Phân hệ Nghiệp vụ Căn hộ & Không gian Tòa nhà (Business Feature):**
   - **US-001 (thuộc 11 User Stories gốc của nhóm trong `05_chi_tiet_us_va_acceptance_criteria.md`):** *Khởi tạo và Quản lý Danh mục Căn hộ (30 tầng bao gồm Kiot tầng 1, Thương mại tầng 2–5, Căn hộ tầng 6–29, Penthouse tầng 30) và Thông tin Hồ sơ Chủ hộ đại diện.*
2. **Phân hệ Nền tảng Xác thực & Quản trị BQT (Core Workflow 1 theo Đề bài gốc & `AGENTS.md`):**
   *(Lưu ý đối soát: Trong 11 User Stories ban đầu của nhóm chỉ tập trung vào nghiệp vụ chung cư nên chưa có mã riêng cho phân hệ Đăng nhập/Tài khoản vốn là điều kiện tiên quyết ở Mục 4.1.5 của `DE_BAI_GOC.md`. Hai mã `US-SEC-01` và `US-SEC-02` được đặt tên trong báo cáo audit để phân rã kỹ thuật, bản chất chính là Luồng nghiệp vụ cốt lõi 1 của hệ thống):*
   - **Xác thực Đăng nhập & Khóa Brute-force (`TASK-SEC-AUTH` / tạm chiếu `US-SEC-02`):** Xác thực Người dùng, Đăng nhập hệ thống, Kiểm soát phiên Session, Khóa tài khoản sau 5 lần sai và Đổi mật khẩu chủ động.
   - **Quản lý Tài khoản Ban Quản trị & Phân quyền RBAC (`TASK-SEC-USER` / tạm chiếu `US-SEC-01`):** Quản trị viên cấp cao (Admin) khởi tạo, cập nhật, kích hoạt/vô hiệu hóa tài khoản cho các thành viên BQT, Kế toán và Thủ quỹ.
3. **Nhiệm vụ Kỹ thuật Nền tảng (Technical Enablers):**
   - **TASK-TECH-01:** Khởi tạo khung dự án Spring Boot 3.4.5, cấu hình Pure Web đa tầng và xử lý ngoại lệ tập trung.
   - **TASK-TECH-02:** Nạp kịch bản Migration CSDL PostgreSQL `V1.1__fix_schema_and_seed.sql` và khởi tạo JPA Entities/Repositories.

---

## 2. PHÂN CÔNG CÔNG VIỆC CỤ THỂ CHO 5 THÀNH VIÊN (WORK BREAKDOWN & ASSIGNMENT)

Nhóm 13 gồm 5 thành viên được phân công vai trò rõ ràng, cân đối khối lượng công việc theo năng lực chuyên môn và trách nhiệm giải trình:

### 2.1. Bảng Tổng hợp Phân bổ Nhiệm vụ Toàn đội

| Thành viên | Vai trò Sprint 1 | Trách nhiệm chính | Mã nhiệm vụ (Task IDs) | Story Points | Giờ ước lượng | Tỷ trọng |
|---|---|---|---|:---:|:---:|:---:|
| **TV1: Phạm Nam Khánh** | Project Manager (PM) & Lead BA | Điều phối Scrum, kiểm soát WBS, giám sát DoD, hoàn thiện AC & Kế hoạch UAT | `PM-01`, `PM-02`, `PM-03` | 7 SP | 30h | 18.75% |
| **TV2: Nguyễn Xuân Sơn** | Database Engineer & Backend Dev | Khắc phục Schema CSDL, Entity JPA, Backend CRUD Căn hộ & Chủ hộ (`US-001`) | `DB-01`, `BE-01`, `BE-02` | 9 SP | 34h | 21.25% |
| **TV3: Phạm Thái Hoà** | Technical Lead & Core Backend Dev | Kiến trúc hệ thống, Spring Security 6, API Đăng nhập (`US-SEC-02`), Quản lý User (`US-SEC-01`), PR Review & Gatekeeping | `ARCH-01`, `SEC-01`, `SEC-02`, `SEC-03`, `SEC-04` | 10 SP | 36h | 22.50% |
| **TV4: Nghiêm Đức Anh** | Frontend Developer & UI/UX Specialist | Dựng Master Layout Thymeleaf, Giao diện Login, UI Quản lý tài khoản, UI Căn hộ | `FE-01`, `FE-02`, `FE-03`, `FE-04` | 8 SP | 32h | 20.00% |
| **TV5: Nguyễn Hợp Huy** | QA Lead & Test Automation Engineer | Cấu hình Test Framework, lập trình Unit Test CFG Basis Path, MockMvc Integration Test, Decision Table | `QA-01`, `QA-02`, `QA-03`, `QA-04` | 6 SP | 28h | 17.50% |
| **TỔNG CỘNG** | | | **19 Tasks** | **40 SP** | **160h** | **100%** |

---

### 2.2. Chi tiết Nhiệm vụ & Tiêu chí Nghiệm thu Từng Thành viên

#### THÀNH VIÊN 1: PHẠM NAM KHÁNH — PROJECT MANAGER & LEAD BUSINESS ANALYST
- **Trách nhiệm trọng tâm:** Đảm bảo dự án đi đúng tiến độ, tuân thủ phương pháp luận Agile/Scrum, kiểm soát phạm vi và chất lượng hồ sơ tài liệu đặc tả.
- **Danh mục Nhiệm vụ Chi tiết:**

| Mã Task | Tên Nhiệm vụ | Mô tả Chi tiết Công việc | Ước lượng | Tiêu chí Hoàn thành (Acceptance Criteria) |
|---|---|---|:---:|---|
| **PM-01** | Điều phối Vận hành Sprint 1 & Quản trị Tiến độ | • Thiết lập bảng Kanban Sprint 1.<br>• Tổ chức Daily Standup 15 phút hàng ngày.<br>• Kiểm soát tiến độ theo Burndown Chart.<br>• Tổ chức Sprint Review và Retrospective. | 10 giờ (2 SP) | • 100% thành viên cập nhật task hàng ngày.<br>• Biên bản họp Sprint Review & Retrospective đầy đủ. |
| **PM-02** | Chuẩn hóa Đặc tả Yêu cầu cho Sprint 1 | • Chi tiết hóa kịch bản Given-When-Then cho `US-SEC-01`, `US-SEC-02`, `US-001`.<br>• Rà soát tính bất biến pháp lý (Luật Cư trú, TT 02/2016, NĐ 93/2021).<br>• Cập nhật Ma trận Truy vết Sprint 1. | 10 giờ (3 SP) | • Tài liệu AC chi tiết được chuyển giao cho Dev và QA.<br>• Ma trận truy vết khớp 100% mã RAW sang Task. |
| **PM-03** | Xây dựng Kế hoạch UAT & Nghiệm thu BQT | • Thiết kế kịch bản kiểm thử chấp nhận người dùng (User Acceptance Test).<br>• Soạn thảo biên bản bàn giao tính năng Sprint 1 cho đại diện Ban Quản trị. | 10 giờ (2 SP) | • Bộ kịch bản UAT gồm 12 tình huống thực tế tại chung cư Văn Phú.<br>• Biên bản bàn giao demo sẵn sàng. |

---

#### THÀNH VIÊN 2: NGUYỄN XUÂN SƠN — DATABASE ENGINEER & BACKEND DEVELOPER
- **Trách nhiệm trọng tâm:** Đảm bảo cơ sở dữ liệu PostgreSQL chuẩn mực, khắc phục toàn bộ các lỗi phát hiện tại báo cáo audit, và hiện thực hóa phân hệ Căn hộ (`US-001`).
- **Danh mục Nhiệm vụ Chi tiết:**

| Mã Task | Tên Nhiệm vụ | Mô tả Chi tiết Công việc | Ước lượng | Tiêu chí Hoàn thành (Acceptance Criteria) |
|---|---|---|:---:|---|
| **DB-01** | Soạn thảo Kịch bản Migration CSDL `V1.1__fix_schema_and_seed.sql` | • Bổ sung `start_date`, `end_date` vào `residents` kèm check constraint ngày.<br>• Bổ sung `version BIGINT NOT NULL DEFAULT 0` vào `invoices`.<br>• Thêm ràng buộc XOR cho bảng `receipts`.<br>• Chuyển `apartments.area_sqm` sang `DECIMAL(12,2)`.<br>• Đổi khóa `households.apartment_id` sang Partial Unique Index (`status = 'ACTIVE'`).<br>• Cập nhật hash BCrypt chuẩn và bổ sung seed data cho `TAM_TRU`, `TAM_VANG`. | 10 giờ (3 SP) | • Kịch bản SQL chạy trơn tru không lỗi trên PostgreSQL 15/16.<br>• Toàn bộ 7 lỗi DDL và DML được vá 100%. |
| **BE-01** | Xây dựng Tầng Entity JPA & Repositories | • Ánh xạ JPA Entity chuẩn cho `User`, `Apartment`, `Household`, `Resident`.<br>• Khai báo `@Version` trên `Invoice` entity.<br>• Viết các Repository Interface kế thừa `JpaRepository` kèm custom JPQL query tìm kiếm căn hộ theo tầng, mã căn. | 12 giờ (3 SP) | • Entity ánh xạ chính xác 100% bảng CSDL.<br>• Không dùng `float`/`double`, toàn bộ diện tích dùng `BigDecimal`. |
| **BE-02** | Hiện thực hóa Backend CRUD Căn hộ (`US-001`) | • Viết `ApartmentService` và `ApartmentController`.<br>• Kiểm tra ràng buộc diện tích thông thủy strictly positive ($m^2 > 0$).<br>• Xử lý thêm/sửa/xóa logic căn hộ (30 tầng) và gán hồ sơ chủ hộ đại diện.<br>• Ghi nhật ký kiểm toán vào `AuditLog`. | 12 giờ (3 SP) | • Thỏa mãn 100% AC của `US-001`.<br>• Chặn thành công diện tích $\le 0$ và mã căn trùng lặp.<br>• Lưu vết lịch sử chủ hộ cũ an toàn. |

---

#### THÀNH VIÊN 3: PHẠM THÁI HOÀ — TECHNICAL LEAD & CORE BACKEND DEVELOPER
- **Trách nhiệm trọng tâm:** Thiết lập nền tảng kiến trúc phần mềm, cấu hình bảo mật Spring Security 6, xây dựng phân hệ Đăng nhập (`US-SEC-02`), Quản lý Tài khoản BQT (`US-SEC-01`), và giữ vai trò Gatekeeper kiểm duyệt chất lượng mã nguồn (Pull Request Review & Architecture Governance).
- **Phân bổ công việc:** Khối lượng công việc của Hoà được phân rã chi tiết thành 5 nhiệm vụ độc lập: `ARCH-01` (6h, 2 SP), `SEC-01` (8h, 2 SP), `SEC-02` (10h, 3 SP), `SEC-03` (8h, 2 SP), và `SEC-04: Code Review, PR Review & Architecture Gatekeeping` (4h, 1 SP). Tổng cộng: **36h, 10 SP**.
- **Danh mục Nhiệm vụ Chi tiết:**

| Mã Task | Tên Nhiệm vụ | Mô tả Chi tiết Công việc | Ước lượng | Tiêu chí Hoàn thành (Acceptance Criteria) |
|---|---|---|:---:|---|
| **ARCH-01** | Thiết lập Khung Kiến trúc Phân tầng & Cấu hình Hệ thống | • Chuẩn hóa cấu trúc Maven đa tầng theo quy chuẩn Pure Web (`controller`, `service`, `repository`, `model`, `dto`, `exception`).<br>• Thiết lập môi trường chạy trên **Java 25 LTS** và Spring Boot 3.4.5.<br>• Cấu hình kết nối HikariCP, JPA Hibernate trong `application.yml`.<br>• Viết Global Exception Handler (`@ControllerAdvice`). | 6 giờ (2 SP) | • Dự án biên dịch sạch sẽ (`mvn clean compile` không cảnh báo).<br>• Khởi động kết nối PostgreSQL thành công. |
| **SEC-01** | Cấu hình Spring Security 6 & Quản trị Phiên (Session) | • Thiết lập `SecurityFilterChain` quản lý phân quyền theo Role (`ROLE_ADMIN`, `ROLE_ACCOUNTANT`, `ROLE_CASHIER`, `ROLE_RESIDENT`).<br>• Cấu hình bảo mật Form Login, Logout, Cookie Session, bảo vệ CSRF và Header XSS Protection.<br>• Khai báo Bean `BCryptPasswordEncoder` (10 rounds). | 8 giờ (2 SP) | • Truy cập các route nội bộ bị chặn khi chưa đăng nhập.<br>• Phân quyền route chuẩn xác theo vai trò. |
| **SEC-02** | Hiện thực hóa Service Xác thực & Đăng nhập (`US-SEC-02`) | • Lập trình `CustomUserDetailsService` và `AuthService`.<br>• Hiện thực hóa cơ chế đếm `failed_attempts` và tự động khóa tài khoản 15 phút sau 5 lần nhập sai mật khẩu.<br>• Bổ sung kiểm tra cờ `is_active` ngăn tài khoản bị vô hiệu hóa đăng nhập; tự động reset biến đếm khi hết hạn khóa.<br>• Lập trình tính năng Đổi mật khẩu (kiểm tra mật khẩu cũ, mật khẩu mới $\ge 8$ ký tự có chữ hoa, số). | 10 giờ (3 SP) | • Đăng nhập thành công với tài khoản mẫu.<br>• Khóa tài khoản chính xác sau lần thứ 5 đăng nhập sai.<br>• Mật khẩu mới được băm BCrypt trước khi lưu. |
| **SEC-03** | Hiện thực hóa Quản lý Tài khoản BQT (`US-SEC-01`) | • Lập trình `UserService` và `AdminController`.<br>• API/Web Controller tạo tài khoản mới cho cán bộ BQT, kích hoạt/khóa tài khoản.<br>• Kiểm tra trùng lặp `username`, `email`.<br>• Bọc toàn bộ thao tác trong `@Transactional(rollbackFor = Exception.class)`. | 8 giờ (2 SP) | • Chỉ Admin mới có quyền tạo và phân quyền tài khoản.<br>• Không thể tạo 2 tài khoản trùng username/email. |
| **SEC-04** | Code Review, PR Review & Architecture Gatekeeping | • Thẩm định và duyệt 100% Pull Requests từ TV2 (DB/Entity), TV4 (Thymeleaf/UI), TV5 (Test harnesses).<br>• Kiểm soát tuân thủ kiến trúc Pure Web (không để lọt SQL/business logic vào Controller hay View).<br>• Gatekeeper chất lượng: Đảm bảo không merge code có mùi mã (Code smells) hoặc vi phạm quy chuẩn SoICT. | 4 giờ (1 SP) | • 100% PR được review và approve trước khi merge vào nhánh develop.<br>• Không xảy ra xung đột merge hoặc suy thoái kiến trúc. |

---

#### THÀNH VIÊN 4: NGHIÊM ĐỨC ANH — FRONTEND DEVELOPER & UI/UX SPECIALIST
- **Trách nhiệm trọng tâm:** Thiết kế và xây dựng giao diện người dùng Thymeleaf đồng bộ, trực quan, chuyên nghiệp, responsive theo thương hiệu BlueMoon.
- **Danh mục Nhiệm vụ Chi tiết:**

| Mã Task | Tên Nhiệm vụ | Mô tả Chi tiết Công việc | Ước lượng | Tiêu chí Hoàn thành (Acceptance Criteria) |
|---|---|---|:---:|---|
| **FE-01** | Xây dựng Master Layout Thymeleaf Chuẩn mực | • Thiết kế khung giao diện Master Layout (`templates/fragments/layout.html`) gồm: Header (thông tin user, nút đăng xuất), Sidebar điều hướng, Footer bản quyền.<br>• Tích hợp Bootstrap 5 / Tailwind CSS và JavaScript hỗ trợ modal, dropdown.<br>• Đảm bảo tính Responsive trên Desktop và Tablet. | 8 giờ (2 SP) | • Master layout tái sử dụng được cho toàn bộ các trang con.<br>• Giao diện hiển thị sắc nét, không vỡ khung. |
| **FE-02** | Xây dựng Giao diện Màn hình Đăng nhập | • Hiện thực hóa `templates/auth/login.html` với logo BlueMoon chuyên nghiệp.<br>• Xử lý hiển thị thông báo lỗi rõ ràng khi sai tài khoản, hiển thị cảnh báo số lần thử còn lại hoặc thông báo tài khoản bị khóa 15 phút. | 8 giờ (2 SP) | • Form đăng nhập tương thích đầy đủ với Spring Security.<br>• Thông báo lỗi hiển thị bằng tiếng Việt thân thiện. |
| **FE-03** | Xây dựng Giao diện Quản lý Tài khoản BQT | • Trang danh sách tài khoản BQT (`templates/admin/users.html`) có phân trang, bộ lọc vai trò.<br>• Modal/Form tạo tài khoản mới, gán vai trò (`ROLE_ADMIN`, `ACCOUNTANT`, `CASHIER`).<br>• Modal đổi mật khẩu an toàn. | 8 giờ (2 SP) | • Giao diện trực quan, xác thực form phía client.<br>• Hiển thị badge trạng thái hoạt động / bị khóa. |
| **FE-04** | Xây dựng Màn hình Dashboard & Danh mục Căn hộ | • Trang Dashboard tổng quan (`templates/admin/dashboard.html`) hiển thị thẻ thống kê sơ bộ.<br>• Trang Danh mục Căn hộ (`templates/admin/apartments.html`): Bảng danh sách 30 tầng căn hộ, modal thêm/sửa căn hộ với trường diện tích $m^2$. | 8 giờ (2 SP) | • Hiển thị đúng dữ liệu căn hộ từ Controller.<br>• Chặn người dùng nhập số âm cho diện tích trên UI. |

---

#### THÀNH VIÊN 5: NGUYỄN HỢP HUY — QA LEAD & TEST ENGINEER
- **Trách nhiệm trọng tâm:** Thiết kế kế hoạch kiểm thử, xây dựng bộ ca kiểm thử tự động đạt chuẩn học thuật SoICT (Chương 8 & 9), thẩm định chất lượng toàn diện.
- **Danh mục Nhiệm vụ Chi tiết:**

| Mã Task | Tên Nhiệm vụ | Mô tả Chi tiết Công việc | Ước lượng | Tiêu chí Hoàn thành (Acceptance Criteria) |
|---|---|---|:---:|---|
| **QA-01** | Thiết lập Khung & Môi trường Kiểm thử Tự động (Test Setup) | • Cấu hình môi trường kiểm thử độc lập `application-test.yml` (H2 in-memory DB / mocks) cho phép chạy test tự động không cần môi trường DB bên ngoài.<br>• Thiết lập thư viện JUnit 5, Mockito, MockMvc, AssertJ.<br>• Xây dựng các lớp cơ sở (Base Test Classes) và bộ dữ liệu fixture dùng chung cho nhóm. | 6 giờ (1 SP) | • Chạy lệnh `mvn test` sạch sẽ không báo lỗi môi trường.<br>• Khung kiểm thử tự động sẵn sàng cho tầng Service và Controller. |
| **QA-02** | Lập trình Ca Kiểm thử CFG Basis Path (AuthService) | • Chuyển hóa đồ thị dòng điều khiển CFG và 5 Basis Paths đã thiết kế trong Section 5 thành mã nguồn JUnit 5 trong `AuthServiceTest.java`.<br>• Bao phủ 100% 5 Basis Paths (`TC_LOGIN_BP_01` $\rightarrow$ `05`) và 2 trường hợp biên (tài khoản vô hiệu hóa, tự động giải phóng sau 15 phút). | 8 giờ (2 SP) | • Toàn bộ 7 ca kiểm thử luồng đăng nhập pass 100%.<br>• Báo cáo độ bao phủ nhánh rẽ đạt 100%. |
| **QA-03** | Lập trình Ca Kiểm thử Tham số hóa Decision Table (ApartmentService) | • Chuyển hóa Black-box Decision Table (6 Rules $R_1 \rightarrow R_6$ đã thiết kế trong Section 5) thành các ca kiểm thử tham số hóa `@ParameterizedTest` trong `ApartmentServiceTest.java`.<br>• Kiểm tra biên diện tích $\le 0$, số tầng ngoài dải $1 \le \text{floor} \le 30$, xung đột loại hình với số tầng. | 6 giờ (1 SP) | • Bắt chính xác 100% các ngoại lệ nghiệp vụ theo từng rule.<br>• Dữ liệu test tham số hóa bao phủ đầy đủ các hoán vị điều kiện biên. |
| **QA-04** | Lập trình Web Integration Test (MockMvc) & Đo lường Coverage | • Viết bộ ca kiểm thử tích hợp Controller với `MockMvc` cho `AuthController` và `AdminController`.<br>• Thực thi kiểm thử tự động, đo độ bao phủ mã nguồn (Line Coverage $\ge 85\%$).<br>• Đóng gói Báo cáo Tổng kết Đo lường Chất lượng Sprint 1 nộp SoICT. | 8 giờ (2 SP) | • 100% ca kiểm thử pass xanh (`BUILD SUCCESS`).<br>• Đo độ bao phủ đạt $\ge 85\%$ dòng lệnh cho tầng Service/Controller. |

---

### 2.3. Ma trận Phân định Trách nhiệm (RACI Matrix) Cho 5 Thành viên trong Sprint 1

Nhằm bảo đảm không xảy ra khoảng trống trách nhiệm (Accountability gaps) và tối ưu hóa phối hợp nhóm, ma trận RACI được thiết lập chuẩn hóa cho 5 Deliverables và các hoạt động trọng tâm của Sprint 1:
- **R (Responsible):** Người trực tiếp thực hiện nhiệm vụ kỹ thuật / tạo ra sản phẩm bàn giao.
- **A (Accountable):** Người chịu trách nhiệm cao nhất về kết quả cuối cùng và phê duyệt chất lượng (mỗi hạng mục có duy nhất 1 cá nhân Accountable).
- **C (Consulted):** Người được tham vấn chuyên môn, đóng góp ý kiến nghiệp vụ và kỹ thuật 2 chiều.
- **I (Informed):** Người được cập nhật thông tin tiến độ và kết quả bàn giao một chiều.

| Hạng mục Công việc & Deliverables Sprint 1 | TV1: Khánh (PM/BA) | TV2: Sơn (DB/BE) | TV3: Hoà (Tech Lead) | TV4: Đ.Anh (UI/UX) | TV5: Huy (QA Lead) |
|---|:---:|:---:|:---:|:---:|:---:|
| **D1: Khung Kiến trúc Spring Boot 3.4.5 & Pure Web** | I | C | **A / R** | C | I |
| **D2: Kịch bản Migration CSDL `V1.1` & Seed Data** | C | **R** | **A** | I | C |
| **D3a: Backend Xác thực, Khóa Brute-force & Quản lý User** | C | I | **A / R** | C | C |
| **D3b: Backend CRUD Danh mục Căn hộ & Chủ hộ (`US-001`)** | C | **R** | **A** | C | C |
| **D4: Bộ Giao diện Thymeleaf (Layout, Login, Admin)** | **A** | I | C | **R** | I |
| **D5: Bộ Ca Kiểm thử Tự động CFG/BVA & Test Report SoICT** | C | I | C | I | **A / R** |
| **Điều phối Scrum, Burndown, DoD & Nghiệm thu UAT** | **A / R** | I | C | I | C |
| **Code Review, PR Review & Architecture Gatekeeping** | I | C | **A / R** | C | I |

#### Bảng Tổng hợp RACI Chi tiết theo Nhóm Tác vụ (Task-Level RACI Summary):

| Nhóm Tác vụ Kỹ thuật | TV1: Khánh (PM/BA) | TV2: Sơn (DB/BE) | TV3: Hoà (Tech Lead) | TV4: Đ.Anh (UI/UX) | TV5: Huy (QA Lead) |
|---|:---:|:---:|:---:|:---:|:---:|
| **Quản trị Tiến độ, AC & UAT (`PM-01..03`)** | **A / R** | I | C | I | C |
| **CSDL Migration & Entity JPA (`DB-01`, `BE-01`)** | C | **R** | **A** | I | C |
| **Kiến trúc Spring Boot & Bảo mật (`ARCH-01`, `SEC-01..03`)** | I | C | **A / R** | C | C |
| **Gatekeeping & PR Review (`SEC-04`)** | I | C | **A / R** | C | I |
| **Nghiệp vụ Backend Căn hộ 30 tầng (`BE-02`)** | C | **R** | **A** | C | C |
| **Giao diện Thymeleaf, Layout & Form (`FE-01..04`)** | **A** | I | C | **R** | I |
| **Kiểm thử CFG Basis Path & Decision Table (`QA-01..04`)** | C | I | C | I | **A / R** |

---

## 3. ĐỊNH NGHĨA HOÀN THÀNH (DEFINITION OF DONE - DoD) THEO CHUẨN IT3180 / SoICT

Một User Story hoặc Nhiệm vụ Kỹ thuật chỉ được xem là **HOÀN THÀNH (DONE)** và sẵn sàng bàn giao khi thỏa mãn đầy đủ 6 nhóm tiêu chí khắt khe sau đây:

### 3.1. Tiêu chuẩn Mã nguồn & Lập trình Sạch (Clean Code & Conventions)
1. **Quy chuẩn Định dạng (Coding Conventions):**
   - Thụt lề chuẩn 4 dấu cách (4 spaces indentation), tuyệt đối không dùng tab lẫn lộn.
   - Đặt tên theo chuẩn Java: `camelCase` cho biến và phương thức, `PascalCase` cho tên Lớp và Interface, `UPPER_SNAKE_CASE` cho hằng số (`public static final`).
   - Tên phương thức phải mang tính hành động rõ ràng (`findByRoomNumber`, `authenticateUser`, `lockAccountAfterMaxAttempts`).
2. **Tài liệu hóa Javadoc Bắt buộc:**
   - 100% các lớp (Class), Interface và các phương thức `public` của tầng Service và Controller phải có Javadoc đầy đủ mô tả chức năng.
   - Phải có đầy đủ các thẻ quy chuẩn: `@param` (mô tả từng tham số đầu vào), `@return` (mô tả dữ liệu trả về), `@throws` (mô tả chi tiết các ngoại lệ nghiệp vụ có thể phát sinh).
3. **Loại bỏ Hoàn toàn Mùi Mã (Code Smells Prevention):**
   - **Không Shotgun Surgery:** Mỗi thay đổi nghiệp vụ chỉ được chỉnh sửa cục bộ tại một tầng trách nhiệm duy nhất.
   - **Không Duplicate Code:** Áp dụng triệt để nguyên lý DRY (Don't Repeat Yourself); các đoạn logic xử lý ngày tháng, chuỗi ký tự phải được đóng gói vào `util` dùng chung.
   - **Không Data Clump:** Đóng gói các nhóm trường dữ liệu liên quan thành DTO hoặc Value Object (ví dụ: `LoginRequestDTO`, `CreateApartmentDTO`).

### 3.2. Tiêu chuẩn Kiến trúc & Toàn vẹn Giao dịch
1. **Tuân thủ Tuyệt đối Phân tầng Thuần Web (Pure Web Architecture):**
   - Controller chỉ xử lý điều hướng, đón nhận request, kiểm tra hợp lệ DTO (`@Valid`) và trả về View/DTO. **Tuyệt đối CẤM** viết câu lệnh SQL/JPA hoặc logic tính toán tiền bạc trong Controller.
   - Toàn bộ logic nghiệp vụ phải đóng gói 100% trong Service.
   - Không chạy câu lệnh truy vấn CSDL bên trong Thymeleaf View.
2. **Bảo đảm Tính toàn vẹn Dữ liệu (ACID Transactions):**
   - Mọi phương thức Service có thao tác ghi dữ liệu (Thêm, Sửa, Xóa, Đổi trạng thái) bắt buộc phải được gắn annotation `@Transactional(rollbackFor = Exception.class)`.

### 3.3. Tiêu chuẩn An toàn & Bảo mật Thông tin
1. **Bảo vệ Mật khẩu:**
   - Tuyệt đối **CẤM** lưu trữ mật khẩu dưới dạng văn bản thô (plain text) ở bất kỳ đâu (CSDL, log, biến tạm).
   - Bắt buộc mã hóa mật khẩu bằng thuật toán **BCrypt** với độ phức tạp tối thiểu 10 rounds (`BCryptPasswordEncoder`).
2. **Phòng chống Tấn công Brute-Force:**
   - Hệ thống phải tự động đếm số lần đăng nhập sai liên tiếp.
   - Sau đúng **5 lần đăng nhập sai**, tài khoản phải bị tự động khóa tạm thời trong **15 phút** (`locked_until = now() + 15m`).
3. **Phòng chống Lỗ hổng Web Phổ biến:**
   - Tầng Repository bắt buộc dùng JPQL có tham số hoặc Spring Data Query Methods để chống triệt để **SQL Injection**.
   - Bật cờ bảo vệ chống **CSRF** trên Spring Security cho toàn bộ các Form POST/PUT.
   - Mã hóa dữ liệu đầu ra trên Thymeleaf (mặc định của `th:text`) để chống tấn công **XSS**.

### 3.4. Tiêu chuẩn Kiểm thử & Đo lường Chất lượng (SoICT Chapter 8 & 9)
1. **Độ bao phủ Kiểm thử (Test Coverage):**
   - Bộ ca kiểm thử tự động (Unit Test & Integration Test) phải đạt độ bao phủ dòng lệnh (Line Coverage) $\ge 85\%$ đối với tầng Service và Controller.
   - 100% các ca kiểm thử trong bộ test suite phải thực thi thành công (Pass xanh), không có bất kỳ lỗi fail hay error nào.
2. **Kiểm thử Luồng Điều khiển (Control Flow Testing - Chapter 8):**
   - Đối với phương thức lõi `authenticateUser()`, phải lập biểu đồ CFG, tính toán $V(G)$ và thiết kế ca kiểm thử bao phủ toàn bộ tập đường dẫn cơ sở (Basis Path Coverage).
3. **Kiểm thử Bảng Quyết định (Decision Table Testing - Chapter 9):**
   - Chức năng kiểm soát khóa tài khoản và kiểm tra tính hợp lệ căn hộ phải có Bảng quyết định kiểm thử hộp đen bao phủ toàn bộ các hoán vị điều kiện biên.

### 3.5. Tiêu chuẩn Đánh giá Tĩnh & Tích hợp (Static Code Analysis)
- Chạy công cụ kiểm tra tĩnh (SonarLint / Maven Checkstyle): **0 lỗi Blocker, 0 lỗi Critical, 0 lỗ hổng bảo mật (Vulnerabilities)**.
- Mã nguồn được commit lên Git nhánh `sprint-1-release` với thông điệp chuẩn mực (Conventional Commits: `feat:`, `fix:`, `test:`, `docs:`), đã giải quyết xong merge conflict và được Pull Request Review bởi Tech Lead (Hoà).

---

## 4. DANH MỤC DELIVERABLES CỦA SPRINT 1 (SPRINT 1 DELIVERABLES)

Sprint 1 cam kết bàn giao đầy đủ 5 sản phẩm bàn giao (Deliverables) chuẩn mực:

```
DELIVERABLES SPRINT 1
├── D1: Mã nguồn Khung Kiến trúc Phân tầng Spring Boot 3.4.5 (Java 25 LTS)
├── D2: Bộ Kịch bản CSDL PostgreSQL Hoàn thiện (Migration V1.1 Script)
├── D3: Phân hệ Backend API/Service/Controller Xác thực & Quản lý Tài khoản BQT
├── D4: Bộ Giao diện Người dùng Thymeleaf Hoàn chỉnh (Layout, Login, Dashboard, Users, Apartments)
└── D5: Bộ Ca Kiểm thử Tự động & Báo cáo Đo lường Chất lượng Chuẩn SoICT
```

### Deliverable D1: Mã nguồn Khung Kiến trúc Phân tầng Chuẩn mực
- **Vị trí lưu trữ:** `src/main/java/com/bluemoon/bfms/`
- **Mô tả thành phần:**
  - `BlueMoonBfmsApplication.java`: Điểm khởi chạy ứng dụng Spring Boot 3.4.5 trên nền tảng Java 25 LTS.
  - Cấu trúc các package phân tầng rõ rệt: `controller`, `service`, `repository`, `model`, `dto`, `exception`, `config`, `util`.
  - `config/SecurityConfig.java`: Cấu hình Spring Security 6 với `SecurityFilterChain`, `BCryptPasswordEncoder`, cấu hình Session Management và phân quyền route.
  - `exception/GlobalExceptionHandler.java`: Xử lý ngoại lệ tập trung, bắt ngoại lệ validation `@Valid`, ngoại lệ bảo mật và ngoại lệ khóa lạc quan `ObjectOptimisticLockingFailureException`.
  - `pom.xml`: Khai báo phụ thuộc chuẩn mực, không thừa thư viện bên ngoài.

### Deliverable D2: Bộ Kịch bản Migration CSDL PostgreSQL Hoàn thiện
- **Vị trí lưu trữ:** `database/migrations/V1.1__fix_schema_and_seed.sql`
- **Nội dung sửa đổi cốt lõi:**
  1. Thiết lập `ALTER TABLE residents ALTER COLUMN residence_status SET NOT NULL;` (triệt tiêu lỗ hổng SQL 3-valued logic bypass check constraints), đồng thời thêm 2 cột `start_date DATE` và `end_date DATE` kèm ràng buộc kiểm tra hợp lệ thời hạn tạm trú/tạm vắng theo Luật Cư trú 2020.
  2. Bổ sung cột `version BIGINT NOT NULL DEFAULT 0` vào bảng `invoices` để kích hoạt cơ chế khóa lạc quan `@Version` (khẳng định chuẩn kiểu `BIGINT` khớp 1:1 với kiểu `Long` của Hibernate/JPA, ngăn ngừa ngoại lệ `SchemaManagementException`).
  3. Bổ sung ràng buộc XOR độc lập vào bảng `receipts` để chống in gộp biên lai thu phí với biên lai quỹ.
  4. Đổi kiểu dữ liệu `apartments.area_sqm` sang `DECIMAL(12,2)`.
  5. Chuyển ràng buộc `households.apartment_id UNIQUE` sang Partial Unique Index (`status = 'ACTIVE'`) để bảo toàn lịch sử chủ hộ cũ.
  6. Thay thế toàn bộ mật khẩu băm giả lập trong `seed_data.sql` bằng mã băm BCrypt chuẩn đã được kiểm chứng thực nghiệm độc lập của `'Password@123'` (`$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK`).
  7. Nạp đầy đủ dữ liệu mẫu cho các trường hợp `TAM_TRU`, `TAM_VANG` và các dòng chi tiết hóa đơn.

### Deliverable D3: Backend API / Service / Controller Xác thực & Tài khoản BQT
- **Vị trí lưu trữ:** `src/main/java/com/bluemoon/bfms/service/`, `controller/`
- **Mô tả chức năng:**
  - `AuthService` & `AuthController`:
    - Tiếp nhận thông tin đăng nhập từ form web, xác thực BCrypt.
    - Xử lý tăng biến `failed_attempts`; kích hoạt tự động khóa tài khoản trong 15 phút khi nhập sai liên tiếp lần thứ 5.
    - Xử lý tính năng Đổi mật khẩu chủ động, kiểm tra mật khẩu cũ và băm mật khẩu mới.
  - `UserService` & `AdminController`:
    - Chức năng quản trị tài khoản BQT: Xem danh sách cán bộ, tạo tài khoản mới, phân vai trò (`ROLE_ADMIN`, `ROLE_ACCOUNTANT`, `ROLE_CASHIER`), kích hoạt/khóa tài khoản.
  - `ApartmentService` & `ApartmentController` (`US-001`):
    - Quản lý danh mục căn hộ tòa nhà 30 tầng (Kiot, Thương mại, Căn hộ ở, Penthouse).
    - Kiểm tra hợp lệ diện tích thông thủy strictly positive ($m^2 > 0$).
    - Gắn kết hồ sơ chủ hộ đại diện (Họ tên, SĐT 10 số, CCCD 12 số, Email).

### Deliverable D4: Bộ Giao diện Người dùng Thymeleaf Hoàn chỉnh
- **Vị trí lưu trữ:** `src/main/resources/templates/`, `src/main/resources/static/`
- **Mô tả màn hình:**
  - `templates/fragments/layout.html`: Master Layout chuẩn responsive với thanh tiêu đề Header hiển thị thông tin cán bộ đăng nhập, Sidebar điều hướng đa phân hệ và Footer tòa nhà.
  - `templates/auth/login.html`: Màn hình Đăng nhập hiện đại với thông báo lỗi tiếng Việt trực quan, cảnh báo số lần nhập sai còn lại hoặc thông báo thời gian tài khoản bị khóa.
  - `templates/admin/dashboard.html`: Màn hình Trang chủ quản trị hiển thị các thẻ tổng quan về số căn hộ, số tài khoản BQT.
  - `templates/admin/users.html`: Màn hình Quản lý tài khoản BQT với bảng danh sách, bộ lọc vai trò, nút kích hoạt/khóa và modal tạo tài khoản mới.
  - `templates/admin/apartments.html`: Màn hình Quản lý Căn hộ với bảng phân loại 30 tầng và form modal khai báo diện tích $m^2$.

### Deliverable D5: Bộ Ca Kiểm thử Tự động & Báo cáo Đo lường Chuẩn SoICT
- **Vị trí lưu trữ:** `src/test/java/com/bluemoon/bfms/`, `docs/reports/SPRINT_1_TEST_REPORT.md`
- **Mô tả kiểm thử:**
  - `AuthServiceTest.java`: Bộ ca kiểm thử đơn vị cho luồng đăng nhập, áp dụng phân tích đồ thị dòng điều khiển CFG (Control Flow Graph), xác định Cyclomatic Complexity và bao phủ 100% Basis Paths (Đường dẫn cơ sở 1: User không tồn tại $\rightarrow$ Đường dẫn 2: Sai pass lần $<5$ $\rightarrow$ Đường dẫn 3: Sai pass chạm mốc 5 bị khóa $\rightarrow$ Đường dẫn 4: Tài khoản đang trong thời gian bị khóa $\rightarrow$ Đường dẫn 5: Đăng nhập thành công, reset attempt).
  - `ApartmentServiceTest.java`: Bộ ca kiểm thử cho CRUD căn hộ, kiểm tra biên diện tích $m^2 \le 0$, diện tích hợp lệ, trùng mã căn hộ theo Black-box Decision Table.
  - `AuthControllerIntegrationTest.java`: Kiểm thử tích hợp với `@SpringBootTest` và `MockMvc` kiểm tra đăng nhập form, chuyển hướng sau đăng nhập thành công và chặn các route yêu cầu quyền Admin.
  - Báo cáo đo lường độ bao phủ (Coverage Report) đạt $\ge 85\%$ kèm đồ thị CFG và Bảng quyết định.

---

## 5. THIẾT KẾ KIỂM THỬ MẪU THEO CHUẨN SOICT (CHAPTER 8 & 9)

Để đảm bảo tính chuẩn mực tuyệt đối theo yêu cầu của Bộ môn Công nghệ Phần mềm (SoICT / HUST), dưới đây là thiết kế kiểm thử chi tiết cho chức năng cốt lõi của Sprint 1:

### 5.1. Kiểm thử Hộp trắng: Đồ thị Dòng Điều khiển CFG cho Chức năng Đăng nhập (`US-SEC-02`)

#### Đoạn mã nguồn nghiệp vụ xác thực (`AuthService.java`):
```java
public LoginResult authenticate(String username, String password) {
    // Nút 1: Bắt đầu, tìm kiếm người dùng theo username
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null) { // Nút 2: Rẽ nhánh kiểm tra user tồn tại
        return LoginResult.USER_NOT_FOUND; // Nút 3: Thoát - Không tìm thấy
    }

    // Kiểm tra trạng thái hoạt động (Chống cán bộ bị đình chỉ/vô hiệu hóa truy cập trái phép)
    if (!user.getIsActive()) throw new DisabledException("Tài khoản đã bị vô hiệu hóa");

    // Xử lý tự động giải phóng khóa khi hết thời hạn (Reset counter on lockout expiry)
    ZonedDateTime now = ZonedDateTime.now();
    if (user.getLockedUntil() != null && user.getLockedUntil().isBefore(now)) {
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
    }
    
    // Nút 4: Kiểm tra tài khoản có đang trong thời gian bị khóa hay không
    if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
        return LoginResult.ACCOUNT_LOCKED; // Nút 5: Thoát - Tài khoản bị khóa
    }
    
    // Nút 6: So khớp mật khẩu băm BCrypt
    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
        // Nút 7: Mật khẩu sai, tăng biến đếm
        int attempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);
        if (attempts >= 5) { // Nút 8: Rẽ nhánh kiểm tra mốc 5 lần sai
            user.setLockedUntil(now.plusMinutes(15)); // Nút 9: Khóa 15 phút
            userRepository.save(user);
            return LoginResult.LOCKED_NOW; // Nút 10: Thoát - Bị khóa ngay lập tức
        } else {
            userRepository.save(user);
            return LoginResult.BAD_CREDENTIALS; // Nút 11: Thoát - Sai mật khẩu
        }
    }
    
    // Nút 12: Mật khẩu đúng, đặt lại số lần sai
    user.setFailedAttempts(0);
    user.setLockedUntil(null);
    userRepository.save(user);
    return LoginResult.SUCCESS; // Nút 13: Thoát - Thành công
}
```

#### Vẽ Đồ thị Dòng Điều khiển (Control Flow Graph - CFG):
```
             (1)
              |
             (2) [user == null?]
            /   \
       True/     \False
          v       v
        (3)*     (4) [lockedUntil != null && lockedUntil > now?]
                /   \
           True/     \False
              v       v
            (5)*     (6) [!passwordEncoder.matches(...)?]
                    /   \
               True/     \False
                  v       v
                 (7)     (12)
                  |        |
                 (8)     (13)*
                /   \
           True/     \False
              v       v
             (9)     (11)*
              |
            (10)*
(* biểu thị nút kết thúc/return)
```

#### Tính toán Độ phức tạp Chu trình (Cyclomatic Complexity):
Đồ thị dòng điều khiển CFG của luồng xác thực và khóa tài khoản có $N = 13$ nút (Nút 1 đến Nút 13), $E = 12$ cạnh có hướng, $P = 4$ nút vị từ rẽ nhánh (Nút 2, Nút 4, Nút 6, Nút 8) và $e = 5$ nút thoát/return (Nút 3, Nút 5, Nút 10, Nút 11, Nút 13).
Độ phức tạp chu trình $V(G)$ được tính toán chuẩn xác theo các phương pháp lý thuyết đồ thị kiểm thử (SoICT Chapter 8):
* Công thức theo số nút vị từ: $V(G) = P + 1 = 4 + 1 = 5$ (với $P=4$ là 4 nút điều kiện rẽ nhánh: `user == null`, `lockedUntil != null && isAfter(...)`, `!passwordEncoder.matches(...)`, `attempts >= 5`).
* Hoặc theo công thức đồ thị đa điểm thoát: $V(G) = E - N + e + 1 = 12 - 13 + 5 + 1 = 5$ (với $E=12$ cạnh, $N=13$ nút, $e=5$ nút thoát).
* Bổ sung ghi nhận: Ma trận liên thuộc thực nghiệm đạt $\text{Rank} = 5$, bảo đảm 5 Basis Paths độc lập tuyến tính.
$\Rightarrow$ Có chính xác **5 Đường dẫn Cơ sở Độc lập (Basis Paths)** cần phải kiểm thử để đạt 100% Basis Path Coverage.

#### Tập 5 Đường dẫn Cơ sở (Independent Basis Paths) & Ca Kiểm thử Tương ứng:
1. **Path 1 (Không tìm thấy user):** `1 -> 2 -> 3`
   - *Test Case TC_LOGIN_BP_01:* Nhập `username = "unknown_user"`, `password = "any"`. Kỳ vọng: Trả về `USER_NOT_FOUND`.
2. **Path 2 (Tài khoản đang bị khóa tạm thời):** `1 -> 2 -> 4 -> 5`
   - *Test Case TC_LOGIN_BP_02:* Nhập `username = "locked_user"` (có `locked_until = now() + 10m`). Kỳ vọng: Trả về `ACCOUNT_LOCKED`.
3. **Path 3 (Sai mật khẩu lần thứ 5 $\rightarrow$ Khóa ngay lập tức):** `1 -> 2 -> 4 -> 6 -> 7 -> 8 -> 9 -> 10`
   - *Test Case TC_LOGIN_BP_03:* Nhập `username = "admin"`, sai mật khẩu khi `failedAttempts = 4`. Kỳ vọng: `failedAttempts` tăng lên 5, gán `locked_until`, trả về `LOCKED_NOW`.
4. **Path 4 (Sai mật khẩu dưới 5 lần):** `1 -> 2 -> 4 -> 6 -> 7 -> 8 -> 11`
   - *Test Case TC_LOGIN_BP_04:* Nhập `username = "admin"`, sai mật khẩu khi `failedAttempts = 2`. Kỳ vọng: `failedAttempts` tăng lên 3, `locked_until` vẫn NULL, trả về `BAD_CREDENTIALS`.
5. **Path 5 (Đăng nhập thành công):** `1 -> 2 -> 4 -> 6 -> 12 -> 13`
   - *Test Case TC_LOGIN_BP_05:* Nhập `username = "admin"`, đúng mật khẩu `'Password@123'`. Kỳ vọng: `failedAttempts` reset về 0, `locked_until = null`, trả về `SUCCESS`.

#### Các Ca Kiểm thử Trường hợp Biên & Ngoại lệ Bổ trợ (Boundary & Security Edge Cases):
6. **TC_LOGIN_EDGE_01 (Tài khoản bị vô hiệu hóa / cán bộ nghỉ việc):**
   - *Kịch bản:* Cán bộ BQT có `is_active = FALSE` cố gắng đăng nhập hệ thống với đúng mật khẩu.
   - *Kỳ vọng:* Hệ thống chặn ngay lập tức, ném `DisabledException("Tài khoản đã bị vô hiệu hóa")`, không thực hiện băm BCrypt mật khẩu.
7. **TC_LOGIN_EDGE_02 (Tự động phục hồi khi hết hạn khóa 15 phút):**
   - *Kịch bản:* Tài khoản có `failedAttempts = 5` và `locked_until` ở quá khứ (`locked_until < now()`), người dùng nhập sai mật khẩu lần đầu tiên sau khi mở khóa.
   - *Kỳ vọng:* Hệ thống tự động reset `failedAttempts = 0`, sau đó ghi nhận lần sai mới là 1 (không tái khóa oan uổng người dùng ngay lần gõ sai đầu tiên).

---

### 5.2. Kiểm thử Hộp đen: Bảng Quyết định (Decision Table) cho Dữ liệu Căn hộ (`US-001`)

Áp dụng kỹ thuật kiểm thử Bảng quyết định (Decision Table Testing - Chapter 9 SoICT) kết hợp Phân tích Giá trị Biên (Boundary Value Analysis - BVA) để kiểm soát toàn diện quy tắc tạo mới căn hộ, tuân thủ nghiêm ngặt Bất biến Kiến trúc tòa nhà BlueMoon 30 tầng và Bất biến 2 (`AGENTS.md`):
- **Điều kiện đầu vào (Conditions):**
  - **C1 (Tính duy nhất):** Mã căn hộ (`room_number`) đã tồn tại trong CSDL chưa? (T / F)
  - **C2 (Biên số tầng tòa nhà):** Số tầng (`floor_number`) có nằm trong dải $1 \le \text{floor} \le 30$? (T / F)
    * Phân tích biên dưới không hợp lệ: `floor < 1` (ví dụ: tầng 0, tầng hầm âm).
    * Phân tích biên trên không hợp lệ: `floor > 30` (ví dụ: tầng 31, vượt chiều cao thiết kế tòa nhà).
  - **C3 (Biên diện tích thông thủy):** Diện tích `area_sqm` có strictly positive ($> 0$)? (T / F)
    * Phân tích biên vi phạm: `area_sqm <= 0` (ví dụ: 0 m², -50 m² bị chặn dứt điểm theo Bất biến 2).
  - **C4 (Danh mục loại hình hợp lệ):** Loại hình căn hộ (`apartment_type`) có thuộc enum (`KIOT`, `COMMERCIAL`, `RESIDENTIAL`, `PENTHOUSE`)? (T / F)
  - **C5 (Bất biến Kiến trúc Tương thích Tầng & Loại hình - BlueMoon Invariant):** Căn hộ có đặt đúng tầng theo thiết kế tòa nhà? (T / F)
    * `KIOT`: Tầng 1 (Kiot thương mại dịch vụ tầng trệt).
    * `COMMERCIAL`: Tầng 2–5 (Khối đế kinh doanh / dịch vụ / kỹ thuật).
    * `RESIDENTIAL`: Tầng 6–29 (24 tầng căn hộ sinh sống cư dân).
    * `PENTHOUSE`: Tầng 30 (Căn hộ cao cấp Penthouse đỉnh tháp).
- **Hành động hệ thống (Actions):**
  - **A1:** Lưu bản ghi căn hộ thành công vào CSDL.
  - **A2:** Báo lỗi "Mã phòng đã tồn tại".
  - **A3:** Báo lỗi "Số tầng không hợp lệ (phải từ tầng 1 đến tầng 30)".
  - **A4:** Báo lỗi "Diện tích thông thủy phải lớn hơn 0 m²".
  - **A5:** Báo lỗi "Loại hình căn hộ không hợp lệ".
  - **A6:** Báo lỗi "Loại hình căn hộ không tương thích với số tầng tòa nhà (Kiot: T1; Thương mại: T2–5; Căn hộ: T6–29; Penthouse: T30)".

#### Bảng Quyết định Kiểm thử Toàn diện (Comprehensive Decision Table):

| Quy tắc (Rule) | R1 | R2 | R3 | R4 | R5 | R6 |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| **Điều kiện C1 (Trùng mã phòng?)** | True | False | False | False | False | False |
| **Điều kiện C2 (Số tầng $1 \le \text{floor} \le 30$?)** | - | **False** (`< 1` hoặc `> 30`) | True | True | True | **True** |
| **Điều kiện C3 (Diện tích $m^2 > 0$?)** | - | - | **False** (`\le 0`) | True | True | **True** |
| **Điều kiện C4 (Loại hình hợp lệ?)** | - | - | - | **False** | True | **True** |
| **Điều kiện C5 (Tương thích Tầng & Loại hình?)** | - | - | - | - | **False** | **True** |
| **Hành động A1 (Lưu thành công CSDL)** | | | | | | **X** |
| **Hành động A2 (Báo lỗi Trùng mã căn)** | **X** | | | | | |
| **Hành động A3 (Báo lỗi Số tầng sai)** | | **X** | | | | |
| **Hành động A4 (Báo lỗi Diện tích $\le 0$)** | | | **X** | | | |
| **Hành động A5 (Báo lỗi Sai loại hình)** | | | | **X** | | |
| **Hành động A6 (Báo lỗi Xung đột Tầng & Loại hình)** | | | | | **X** | |

*Ghi chú các quy tắc kiểm thử quyết định:*
- **Quy tắc R1:** Căn hộ trùng mã phòng đã tồn tại trong CSDL $\rightarrow$ Kích hoạt Hành động A2 (Báo lỗi `DuplicateRoomException`).
- **Quy tắc R2:** Kiểm tra chặt biên tầng `< 1` hoặc `> 30` (ví dụ tầng 0 hoặc tầng 31) $\rightarrow$ Kích hoạt Hành động A3 (Báo lỗi "Số tầng không hợp lệ").
- **Quy tắc R3:** Kiểm tra chặt biên diện tích: `area_sqm <= 0` (ví dụ 0 m² hoặc -75.5 m²) $\rightarrow$ Kích hoạt Hành động A4 (Báo lỗi "Diện tích thông thủy phải lớn hơn 0 m²").
- **Quy tắc R4:** Loại hình căn hộ ngoài danh mục cho phép $\rightarrow$ Kích hoạt Hành động A5 (Báo lỗi "Loại hình căn hộ không hợp lệ").
- **Quy tắc R5:** Xung đột loại hình với số tầng thiết kế $\rightarrow$ Kích hoạt Hành động A6 (Báo lỗi "Loại hình căn hộ không tương thích với số tầng").
- **Quy tắc R6 (Rule R6):** Kiểm soát tương thích Loại hình Căn hộ và Số tầng (Kiot: Tầng 1; Thương mại: Tầng 2–5; Căn hộ: Tầng 6–29; Penthouse: Tầng 30), và kiểm tra chặt biên tầng `< 1` hoặc `> 30` -> Báo lỗi; `area_sqm <= 0` -> Báo lỗi. Khi và chỉ khi thỏa mãn đồng thời toàn bộ các điều kiện biên hợp lệ và tương thích tầng, hệ thống kích hoạt Hành động A1 (Lưu thành công vào CSDL).

#### Bộ Ca Kiểm thử Tham số hóa Tương ứng (`ApartmentServiceTest.java`):
1. **TC_APT_DT_01 (Trùng mã căn - R1):** Nhập `room_number = "P.601"` (đã tồn tại trong seed data). Kỳ vọng: Báo lỗi A2 `DuplicateRoomException`.
2. **TC_APT_DT_02a (Biên dưới tầng - R2):** Nhập `floor_number = 0`, `room = "P.001"`. Kỳ vọng: Báo lỗi A3 "Tầng không được nhỏ hơn 1".
3. **TC_APT_DT_02b (Biên trên tầng - R2):** Nhập `floor_number = 31`, `room = "P.3101"`. Kỳ vọng: Báo lỗi A3 "Tầng không được vượt quá 30".
4. **TC_APT_DT_03a (Biên diện tích bằng 0 - R3):** Nhập `area_sqm = 0.00`. Kỳ vọng: Báo lỗi A4 `InvalidAreaException` ($> 0$).
5. **TC_APT_DT_03b (Biên diện tích âm - R3):** Nhập `area_sqm = -75.50`. Kỳ vọng: Báo lỗi A4 `InvalidAreaException`.
6. **TC_APT_DT_04 (Sai danh mục loại hình - R4):** Nhập `apartment_type = "WAREHOUSE"`. Kỳ vọng: Báo lỗi A5 `InvalidApartmentTypeException`.
7. **TC_APT_DT_05 (Xung đột Tầng & Loại hình - R5):** Nhập `apartment_type = "PENTHOUSE"` tại `floor_number = 1`, hoặc `KIOT` tại `floor_number = 30`. Kỳ vọng: Báo lỗi A6 `FloorTypeMismatchException`.
8. **TC_APT_DT_06 (Rule R6: Kiểm soát tương thích Loại hình Căn hộ và Số tầng, Kiểm tra chặt biên):** Áp dụng Rule R6: Kiểm soát tương thích Loại hình Căn hộ và Số tầng (Kiot: Tầng 1; Thương mại: Tầng 2–5; Căn hộ: Tầng 6–29; Penthouse: Tầng 30), và kiểm tra chặt biên tầng `< 1` hoặc `> 30` -> Báo lỗi; `area_sqm <= 0` -> Báo lỗi. Dữ liệu thử nghiệm chuẩn: Nhập `room = "P.702"`, `floor = 7`, `area = 85.50`, `type = "RESIDENTIAL"`. Kỳ vọng: Thỏa mãn toàn bộ điều kiện, hệ thống lưu thành công A1, mã căn hộ trả về không null.

---

## 6. MA TRẬN TRUY VẾT SPRINT 1 (SPRINT 1 TRACEABILITY MATRIX)

Ma trận truy vết 5 tầng dưới đây bảo đảm tính liên kết hữu cơ không thể tách rời từ Yêu cầu thô của khách hàng (`RAW`) đến User Story, Nhiệm vụ kỹ thuật, Sản phẩm bàn giao (`Deliverables`), Ca kiểm thử và Tiêu chuẩn kiểm soát chất lượng SoICT:

| Mã RAW Ban đầu | User Story Quy chuẩn | Mã Nhiệm vụ Triển khai | Người phụ trách | Sản phẩm Bàn giao (Deliverable) | Mã Ca Kiểm thử (Test Cases) | Tiêu chuẩn Nghiệp vụ & Bất biến Áp dụng |
|:---:|---|:---:|:---:|:---:|---|---|
| **Nền tảng** | Khung kiến trúc Pure Web & Master Layout | `ARCH-01` (6h, 2 SP), `FE-01: Thiết kế Master Layout Thymeleaf` (8h, 2 SP), `SEC-04` (4h, 1 SP) | Hoà (TV3), Đ.Anh (TV4) | **Deliverable D1, D4** | `SmokeTest.java`, `LayoutRenderTest.java` | Kiến trúc Pure Web phân tầng, Master Layout Thymeleaf responsive, Gatekeeper PR review, Java 25 LTS, Spring Boot 3.4.5. |
| **RAW-001** | `US-001`: Danh mục Căn hộ 30 tầng | `BE-01`, `BE-02`, `FE-04` | Sơn (TV2), Đ.Anh (TV4) | **Deliverable D2, D3, D4** | `TC_APT_01`, `TC_APT_02`, `TC_APT_03` | Bất biến 2: Diện tích thông thủy strictly positive ($m^2 > 0$), `DECIMAL(12,2)`. |
| **RAW-002** | `US-001`: Thông tin Hồ sơ Chủ hộ | `BE-02`, `FE-04` | Sơn (TV2), Đ.Anh (TV4) | **Deliverable D3, D4** | `TC_OWNER_01`, `TC_OWNER_02` | CCCD 12 chữ số, SĐT 10 số, lưu vết lịch sử chủ hộ (`status = 'ACTIVE'`). |
| **RAW-006** *(Chuẩn bị)* | `US-003`: Trạng thái Tạm trú/vắng | `DB-01` | Sơn (TV2) | **Deliverable D2** (Schema migration) | `TC_DB_MIGRATE_01` | Bất biến 3: Thêm `start_date`, `end_date` vào `residents` (Luật Cư trú 2020). |
| **RAW-011..016** *(Chuẩn bị)* | Thu phí & Khóa lạc quan | `DB-01` | Sơn (TV2) | **Deliverable D2** (Schema migration) | `TC_DB_MIGRATE_02` | Concurrency Invariant: Thêm `version BIGINT` vào `invoices` chống gạch nợ đúp. |
| **RAW-022..025** *(Chuẩn bị)* | Tách biệt Đóng góp Tự nguyện | `DB-01` | Sơn (TV2) | **Deliverable D2** (Schema migration) | `TC_DB_MIGRATE_03` | Bất biến 1: Thêm XOR check trên `receipts` chống in gộp biên lai (NĐ 93/2021). |
| **NFR-02** | `US-SEC-01`: Quản lý Tài khoản BQT | `SEC-03`, `FE-03` | Hoà (TV3), Đ.Anh (TV4) | **Deliverable D3, D4** | `TC_USER_CRUD_01`, `TC_USER_CRUD_02` | Phân quyền RBAC (`ROLE_ADMIN`, `ACCOUNTANT`, `CASHIER`), kiểm tra tính duy nhất. |
| **NFR-02** | `US-SEC-02`: Đăng nhập & Khóa Brute-force | `SEC-01`, `SEC-02`, `FE-02` | Hoà (TV3), Đ.Anh (TV4) | **Deliverable D3, D4** | `TC_LOGIN_BP_01` $\rightarrow$ `TC_LOGIN_BP_05` | Khóa tài khoản sau 5 lần sai trong 15 phút, mã hóa BCrypt $\ge 10$ rounds. |
| **Toàn bộ** | Đảm bảo Chất lượng Kiểm thử | `QA-01..04`, `PM-01..03` | Huy (TV5), Khánh (TV1) | **Deliverable D5** | Toàn bộ Test Suite (Pass 100%) | SoICT Workbook Chương 8 (CFG Basis Path) & Chương 9 (Decision Table). |

---

## 7. KẾ HOẠCH LÀM VIỆC THEO NGÀY (DAILY SPRINT SCHEDULE)

| Ngày làm việc | Hoạt động Trọng tâm Toàn đội | Output Kỳ vọng Cuối ngày |
|---|---|---|
| **Ngày 1 (20/10)** | Họp Sprint Planning. Hoà thiết lập khung kiến trúc Spring Boot (ARCH-01). Sơn soạn thảo kịch bản migration CSDL (DB-01). | Mã nguồn khung biên dịch sạch; script migration `V1.1` sẵn sàng. |
| **Ngày 2 (21/10)** | Chạy migration CSDL, cập nhật hash BCrypt chuẩn. Hoà cấu hình Spring Security 6 (SEC-01). Sơn ánh xạ Entity JPA (BE-01). | Kết nối DB PostgreSQL thành công; Security filter hoạt động. |
| **Ngày 3 (22/10)** | Hoà lập trình `AuthService` và xử lý khóa Brute-force 5 lần (SEC-02). Đức Anh dựng Master Layout Thymeleaf (FE-01). | Logic xác thực hoạt động qua Unit Test ban đầu; layout chuẩn. |
| **Ngày 4 (23/10)** | Đức Anh xây dựng giao diện Login (FE-02). Hoà hoàn thiện `AuthController` và tích hợp Form Login với Spring Security. | Đăng nhập giao diện thành công với tài khoản seed mẫu. |
| **Ngày 5 (24/10)** | Huy vẽ CFG và viết trọn bộ 5 ca kiểm thử Basis Path cho Login (QA-02). Hoà bắt đầu viết `UserService` (SEC-03). | 100% ca kiểm thử Basis Path đăng nhập pass xanh. |
| **Ngày 6 (27/10)** | Sơn hiện thực hóa `ApartmentService` (BE-02). Đức Anh thiết kế giao diện Quản lý tài khoản BQT (FE-03). | Backend Căn hộ hoàn thành; UI Quản lý tài khoản sẵn sàng. |
| **Ngày 7 (28/10)** | Đức Anh hoàn thiện màn hình Quản lý Căn hộ (FE-04). Sơn kết nối Controller với View Căn hộ. | Thao tác Thêm/Sửa Căn hộ hoạt động mượt mà trên giao diện. |
| **Ngày 8 (29/10)** | Huy thiết kế Bảng quyết định và viết ca kiểm thử cho Căn hộ (QA-03). Khánh rà soát AC và kiểm tra Definition of Done. | Bộ ca kiểm thử tự động đạt 30+ tests, bao phủ $\ge 85\%$. |
| **Ngày 9 (30/10)** | Kiểm thử tích hợp toàn diện bằng `MockMvc` (QA-04). Tối ưu hóa giao diện và fix bug phát hiện. | Báo cáo kiểm định chất lượng hoàn thiện; 0 bug mở. |
| **Ngày 10 (02/11)** | Họp Sprint Review, demo tính năng cho Ban Quản trị. Họp Sprint Retrospective đánh giá cải tiến. Đóng gói Deliverables D1–D5. | Biên bản nghiệm thu Sprint 1 có chữ ký; sẵn sàng cho Sprint 2. |

---

## 8. QUẢN LÝ RỦI RO SPRINT 1 (RISK MITIGATION)

| Mã Rủi ro | Nguy cơ Rủi ro Tiềm ẩn | Mức độ | Kế hoạch Phòng ngừa & Xử lý |
|:---:|---|:---:|---|
| **RSK-S1-01** | Lỗi không đăng nhập được do sai thuật toán băm BCrypt trong CSDL. | **CAO** | Đã phát hiện trong audit; bắt buộc nạp lại hash chuẩn `$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK` ngay tại Ngày 1 trước khi test. |
| **RSK-S1-02** | Xung đột phiên bản Java giữa máy các thành viên (Java 17 vs Java 25). | **TRUNG BÌNH** | Chốt cứng Java 25 LTS trong `pom.xml` và tài liệu; yêu cầu cài đặt bản JDK Temurin 25 đồng nhất. |
| **RSK-S1-03** | Khóa tài khoản sau 5 lần sai không tự động mở sau 15 phút. | **TRUNG BÌNH** | So sánh thời gian dựa trên `ZonedDateTime.now()` với múi giờ chuẩn UTC/GMT+7, viết Unit Test bao phủ mốc thời gian. |
| **RSK-S1-04** | Trùng lặp mã căn hộ khi nhập đồng thời danh mục căn hộ 30 tầng. | **THẤP** | Cài đặt Unique constraint tại CSDL và kiểm tra `existsByRoomNumber` tại Service trước khi lưu. |

---

## 9. KẾT LUẬN & CAM KẾT HOÀN THÀNH

Kế hoạch chi tiết Sprint 1 của Nhóm 13 đã thiết lập một lộ trình kỹ thuật chặt chẽ, cân đối tuyệt đối giữa 5 thành viên và bám sát toàn diện các quy chuẩn học thuật của Viện CNTT&TT (SoICT / HUST). Toàn bộ 5 thành viên cam kết tuân thủ nghiêm ngặt Definition of Done, 4 Bất biến nghiệp vụ trong `AGENTS.md` và hoàn thành 100% các Deliverables cam kết đúng hạn vào ngày **02/11/2026**.
