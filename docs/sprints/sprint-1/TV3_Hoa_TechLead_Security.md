# 🛡️ CẨM NANG HÀNH ĐỘNG SPRINT 1 — THÀNH VIÊN 3: PHẠM THÁI HOÀ
## Vai trò: Technical Lead & Core Backend Developer
### Dự án: BlueMoon BFMS (IT3180 - Team 13)

---

## 🎯 1. TỔNG QUAN VAI TRÒ & SẢN PHẨM BÀN GIAO (DELIVERABLES)

- **Vai trò chính:** Thiết lập kiến trúc phần mềm Spring Boot 3.5.16 (Java 25 LTS), cấu hình bảo mật Spring Security 6, xây dựng phân hệ Đăng nhập/Khóa Brute-force (`US-SEC-02`), Quản lý tài khoản BQT (`US-SEC-01`), và giữ vai trò **reviewer kỹ thuật cùng Sơn**, duyệt chéo và không tự duyệt PR.
- **Tổng thời gian cam kết:** **28 giờ làm việc** (8 Story Points).
- **Quyền quản trị mã nguồn:**
  - Đồng quản trị file cấu hình `pom.xml` và `src/main/resources/application.yml` cùng Sơn.
  - Phân hệ Bảo mật & Kiến trúc: `src/main/java/com/bluemoon/bfms/config/`, `exception/`, `service/AuthService.java`, `service/UserService.java`, `controller/AuthController.java`.

---

## 📌 2. DANH MỤC NHIỆM VỤ CHI TIẾT (TASK BREAKDOWN)

### Thẻ Issue 6: `[Sprint 1][ARCH] Khởi tạo Khung Kiến trúc Spring Boot 3.5.16 & Pure Web` (`ARCH-01`)
- **Khối lượng:** 5 giờ (1 SP).
- **Mã nhánh Git:** `feature/issue-06-architecture-framework-setup`
- **Công việc cụ thể:**
  1. Kiểm tra cấu hình `pom.xml` bảo đảm chạy mượt mà trên **Java 25 LTS** và Spring Boot 3.5.16.
  2. Cấu hình kết nối HikariCP tới CSDL PostgreSQL trong `src/main/resources/application.yml`.
  3. Viết `exception/GlobalExceptionHandler.java` sử dụng `@ControllerAdvice`:
     - Bắt ngoại lệ `@Valid` Bean Validation và trả thông báo lỗi thân thiện.
     - Bắt ngoại lệ nghiệp vụ `BusinessRuleException` và `ResourceNotFoundException`.
     - Bắt ngoại lệ khóa lạc quan `ObjectOptimisticLockingFailureException`.

---

### Thẻ Issue 7: `[Sprint 1][SEC] Cấu hình Spring Security 6 & Quản trị Phiên` (`SEC-01`)
- **Khối lượng:** 6 giờ (2 SP).
- **Mã nhánh Git:** `feature/issue-07-spring-security-config`
- **Công việc cụ thể:**
  1. Tạo `config/SecurityConfig.java`:
     - Khai báo Bean `BCryptPasswordEncoder` (10 rounds).
     - Thiết lập `SecurityFilterChain`: Mở quyền truy cập cho `/login`, `/css/**`, `/js/**`, `/images/**`.
     - Phân quyền các route `/admin/**` bắt buộc phải có vai trò `ROLE_ADMIN` hoặc cán bộ BQT.
     - Cấu hình Form Login, đường dẫn đăng xuất `/logout` và cơ chế bảo vệ CSRF.

---

### Thẻ Issue 8: `[Sprint 1][SEC] Hiện thực hóa AuthService & Khóa 15 Phút sau 5 Lần Sai` (`SEC-02`)
- **Khối lượng:** 8 giờ (2 SP).
- **Mã nhánh Git:** `feature/issue-08-auth-service-and-lockout`
- **Logic bất biến của `AuthService.java` (Khớp 100% với Đồ thị CFG theo mã nguồn đã chốt):**
  1. Kiểm tra user có tồn tại không $\rightarrow$ Nếu không: Báo `USER_NOT_FOUND`.
  2. Kiểm tra `is_active == false` $\rightarrow$ Ném ngoại lệ `DisabledException` ngay lập tức.
  3. Kiểm tra nếu `locked_until` đã quá hạn so với hiện tại $\rightarrow$ Tự động reset `failed_attempts = 0` và xóa `locked_until = null`.
  4. Kiểm tra nếu tài khoản đang trong hạn khóa (`now < locked_until`) $\rightarrow$ Báo `ACCOUNT_LOCKED`.
  5. So khớp mật khẩu qua `passwordEncoder.matches(rawPassword, user.getPasswordHash())`:
     - *Nếu sai:* Tăng `failed_attempts`. Nếu chạm mốc $\ge 5$, gán `locked_until = now() + 15 phút` và báo `LOCKED_NOW`. Nếu $< 5$, báo `BAD_CREDENTIALS`.
     - *Nếu đúng:* Reset `failed_attempts = 0`, `locked_until = null`, lưu CSDL và trả về `SUCCESS`.
  6. Viết hàm Đổi mật khẩu chủ động `changePassword()`: Bắt buộc kiểm tra mật khẩu cũ và băm BCrypt mật khẩu mới.

---

### Thẻ Issue 9: `[Sprint 1][SEC] Hiện thực hóa UserService & Quản lý Tài khoản BQT` (`SEC-03`)
- **Khối lượng:** 6 giờ (2 SP).
- **Mã nhánh Git:** `feature/issue-09-user-service-crud`
- **Công việc cụ thể:**
  - Lập trình `UserService.java` với các hàm CRUD tài khoản BQT: `createUser`, `updateUser`, `toggleUserStatus`, `getAllUsers`.
  - Kiểm tra tính duy nhất của `username` và `email`.
  - Bọc trong `@Transactional(rollbackFor = Exception.class)`.

---

### Thẻ Issue 10: `[Sprint 1][QA] Gatekeeping & Review chéo cùng Sơn` (`SEC-04`)
- **Khối lượng:** 3 giờ (1 SP) trải đều suốt 7 ngày.
- **Trách nhiệm:** Hoà duyệt PR Sơn; PR Hoà do Sơn duyệt; PR khác có ít nhất một trong hai duyệt.
- **Checklist Review khi duyệt PR:**
  - [ ] Không có câu lệnh SQL hoặc business logic nào bị viết trong Controller hoặc Thymeleaf View.
  - [ ] Không dùng kiểu `float`/`double` cho tiền tệ hoặc diện tích (bắt buộc dùng `BigDecimal`).
  - [ ] Kiểm tra `verify`, CI và test liên quan trước review; không tự duyệt/merge, nghiệm thu thêm profile sprint1-quality.

---

## 📅 3. CHECKLIST HÀNH ĐỘNG TỪNG NGÀY (DAILY CHECKLIST)

- [ ] **Ngày 1 (31/10):** Kiểm tra `pom.xml`, cấu hình `application.yml` kết nối PostgreSQL; viết `GlobalExceptionHandler.java`.
- [ ] **Ngày 2 (01/11):** Cấu hình `SecurityConfig.java` với Form Login và BCrypt; hỗ trợ Sơn chạy migration CSDL.
- [ ] **Ngày 3 (02/11):** Hoàn thành logic `AuthService.java` (khóa 15 phút sau 5 lần sai); push code lên nhánh để Huy ráp test CFG.
- [ ] **Ngày 4 (03/11):** Phối hợp với Đức Anh ghép Form Login với Spring Security. Lập trình `UserService.java`.
- [ ] **Ngày 5 (04/11):** Hoàn thiện các API Quản lý tài khoản BQT trong `AdminController.java`. Review PR Backend Căn hộ của Sơn.
- [ ] **Ngày 6 (05/11):** Hỗ trợ ghép nối toàn bộ các màn hình; kiểm tra rà soát lỗi bảo mật và transaction.
- [ ] **Ngày 7 (06/11):** Thực hiện Gatekeeper kiểm tra tổng thể mã nguồn; cùng nhóm tham gia buổi Demo Sprint Review.

---

## 🛠️ 4. HƯỚNG DẪN DÙNG GIT CHO HOÀ

```bash
# 1. Kéo code develop mới nhất
git checkout develop
git pull origin develop

# 2. Tạo nhánh làm việc cho AuthService
git checkout -b feature/issue-08-auth-service-and-lockout

# 3. Commit và đẩy lên GitHub
git add src/main/java/com/bluemoon/bfms/service/AuthService.java
git commit -m "feat(#08): implement AuthService with 5 attempts lockout logic"
git push origin feature/issue-08-auth-service-and-lockout

# 4. Khi review PR của thành viên khác:
# Kiểm tra code trên GitHub, nếu đạt chuẩn thì nhấn "Squash and merge" hoặc "Rebase and merge" vào develop.
```

> Phân công/ước lượng hiện hành và task chủ hộ bổ sung: xem [quy chuẩn](../../PROJECT_CONVENTIONS.md) và phần “Điều chỉnh Sprint 1” trong [backlog](github_issues_backlog.md). Các task lớn trong cẩm nang là gói cha; giao các phần việc 2–4 giờ trong backlog.

### Review và kiểm thử nền tảng

PR của Hoà do Sơn duyệt; Hoà duyệt PR Sơn. Kiểm tra CI và profile sprint1-quality khi nghiệm thu. Boot/JaCoCo/smoke test là nền tảng đã cấu hình, không thay cho test đăng nhập/RBAC. Mở khóa khi now >= locked_until, không chỉ khi lớn hơn.
