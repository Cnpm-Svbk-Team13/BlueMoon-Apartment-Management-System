# 🧪 CẨM NANG HÀNH ĐỘNG SPRINT 1 — THÀNH VIÊN 5: NGUYỄN HỢP HUY
## Vai trò: QA Lead & Test Automation Engineer
### Dự án: BlueMoon BFMS (IT3180 - Team 13)

---

## 🎯 1. TỔNG QUAN VAI TRÒ & SẢN PHẨM BÀN GIAO (DELIVERABLES)

- **Vai trò chính:** Thiết lập môi trường kiểm thử tự động, áp dụng phương pháp luận **Shift-Left Testing** (chuẩn bị khung test và dữ liệu giả lập ngay từ Ngày 1 song song với Dev), lập trình bộ ca kiểm thử đơn vị theo đồ thị CFG (các Basis Paths theo CFG đã chốt), ca kiểm thử tham số hóa theo Bảng quyết định (6 Rules), kiểm thử tích hợp `MockMvc` và đo lường độ bao phủ mã nguồn $\ge 85\%$ nộp cho Bộ môn CNPM (SoICT / HUST).
- **Tổng thời gian cam kết:** **20 giờ làm việc** (4 Story Points).
- **Quyền quản trị mã nguồn:**
  - `src/test/java/com/bluemoon/bfms/` (Toàn bộ các lớp kiểm thử JUnit 5 & Mockito).
  - `src/test/resources/application-test.yml` (Cấu hình môi trường test độc lập).
  - Báo cáo chất lượng: [`docs/sprints/sprint-1/SPRINT_1_TEST_REPORT.md`](./SPRINT_1_TEST_REPORT.md).

---

## 📌 2. DANH MỤC NHIỆM VỤ CHI TIẾT (TASK BREAKDOWN)

### Thẻ Issue 15: `[Sprint 1][QA] Thiết lập Môi trường Test & Dữ liệu Mẫu (Shift-Left)` (`QA-01`)
- **Khối lượng:** 4 giờ (1 SP) - Thực hiện ngay tại **Ngày 1**.
- **Mã nhánh Git:** `feature/issue-15-test-setup-shift-left`
- **Công việc cụ thể:**
  1. Cấu hình `src/test/resources/application-test.yml` chạy trên H2 in-memory CSDL hoặc mock repository để lệnh `mvn test` chạy độc lập, không phụ thuộc vào CSDL thật bên ngoài.
  2. Tạo lớp cơ sở `BaseTest.java` nạp sẵn cấu hình Spring Boot Test.
  3. Xây dựng bộ dữ liệu fixture mẫu (tài khoản admin test, căn hộ mẫu 30 tầng) dùng chung cho toàn bộ test suite.

---

### Thẻ Issue 16: `[Sprint 1][QA] Lập trình Ca Kiểm thử CFG Basis Path cho Đăng nhập` (`QA-02`)
- **Khối lượng:** 6 giờ (1 SP).
- **Mã nhánh Git:** `feature/issue-16-auth-cfg-basis-path-tests`
- **File cần tạo:** `src/test/java/com/bluemoon/bfms/service/AuthServiceTest.java`
- **Quy trình thực thi:**
  1. **Ngày 2:** Tạo trước khung kiểm thử (Test Stubs) chứa sẵn 5 hàm `@Test` rỗng tương ứng các Basis Paths theo CFG đã chốt đã chứng minh trong [`kiem_thu_hoc_thuat_cfg_decision_table.md`](./kiem_thu_hoc_thuat_cfg_decision_table.md):
     - `testPath1_UserNotFound()`
     - `testPath2_AccountLocked()`
     - `testPath3_WrongPassword5Times_LockedNow()`
     - `testPath4_WrongPasswordUnder5Times_BadCredentials()`
     - `testPath5_LoginSuccess_ResetAttempts()`
  2. **Ngày 3:** Ngay khi Hoà vừa push code `AuthService.java`, Huy ráp code vào khung test stubs và chạy kiểm thử ngay lập tức (phản hồi bug cho Hoà fix ngay trong ngày).
  3. Bổ sung 2 ca kiểm thử biên: Tài khoản bị vô hiệu hóa (`is_active = false`) và tự động giải phóng khóa khi hết 15 phút.

---

### Thẻ Issue 17: `[Sprint 1][QA] Lập trình Ca Kiểm thử Tham số hóa Bảng Quyết định Căn hộ` (`QA-03`)
- **Khối lượng:** 5 giờ (1 SP).
- **Mã nhánh Git:** `feature/issue-17-apartment-decision-table-tests`
- **File cần tạo:** `src/test/java/com/bluemoon/bfms/service/ApartmentServiceTest.java`
- **Quy trình thực thi:**
  - Áp dụng kiểm thử tham số hóa `@ParameterizedTest` với `@CsvSource` hoặc `@MethodSource` bao phủ 100% 6 Rules của Bảng quyết định (SoICT Chapter 9):
    * Rule R1: Trùng mã phòng $\rightarrow$ ném `DuplicateRoomException`.
    * Rule R2: Biên tầng $< 1$ hoặc $> 30$ $\rightarrow$ ném ngoại lệ tầng không hợp lệ.
    * Rule R3: Biên diện tích $\le 0$ ($0\text{ m}^2$, $-50\text{ m}^2$) $\rightarrow$ ném ngoại lệ diện tích.
    * Rule R4: Sai enum loại hình căn hộ.
    * Rule R5: Xung đột tầng và loại hình (ví dụ Penthouse tầng 1, hoặc Kiot tầng 30).
    * Rule R6: Dữ liệu hợp lệ $\rightarrow$ Lưu thành công vào CSDL.

---

### Thẻ Issue 18: `[Sprint 1][QA] Kiểm thử Tích hợp MockMvc & Báo cáo Đo lường Chất lượng` (`QA-04`)
- **Khối lượng:** 5 giờ (1 SP).
- **Mã nhánh Git:** `feature/issue-18-mockmvc-and-coverage-report`
- **File cần tạo:**
  - `src/test/java/com/bluemoon/bfms/controller/AuthControllerIntegrationTest.java`: Dùng `MockMvc` gửi request đăng nhập POST form, kiểm tra mã HTTP 302 chuyển hướng thành công.
  - Chạy đo lường độ bao phủ (Line Coverage $\ge 85\%$).
  - Đóng gói báo cáo đo lường chất lượng: `docs/sprints/sprint-1/SPRINT_1_TEST_REPORT.md`.

---

## 📅 3. CHECKLIST HÀNH ĐỘNG TỪNG NGÀY (DAILY CHECKLIST)

- [ ] **Ngày 1 (31/10):** Cấu hình `application-test.yml` và bộ dữ liệu fixture mẫu (Shift-Left khởi động).
- [ ] **Ngày 2 (01/11):** Vẽ đồ thị CFG và dựng trước khung Test Stubs rỗng cho `AuthServiceTest`.
- [ ] **Ngày 3 (02/11):** Ráp test thật vào `AuthService` của Hoà vừa push. Xác nhận các Basis Paths theo CFG đã chốt pass xanh.
- [ ] **Ngày 4 (03/11):** Thiết kế Bảng quyết định 6 Rules cho Căn hộ; viết trước khung `@ParameterizedTest`.
- [ ] **Ngày 5 (04/11):** Chạy bộ test Bảng quyết định trên `ApartmentService` của Sơn; bắt và báo các lỗi biên.
- [ ] **Ngày 6 (05/11):** Viết `MockMvc` Integration Test cho Controller; kiểm tra form đăng nhập và route bảo mật.
- [ ] **Ngày 7 (06/11):** Chạy đo độ bao phủ Line Coverage $\ge 85\%$; xuất báo cáo `SPRINT_1_TEST_REPORT.md` nộp bộ môn.

---

## 🛠️ 4. HƯỚNG DẪN DÙNG GIT CHO HUY

```bash
# 1. Kéo code develop mới nhất
git checkout develop
git pull origin develop

# 2. Tạo nhánh làm việc cho test setup
git checkout -b feature/issue-15-test-setup-shift-left

# 3. Commit và đẩy lên GitHub
git add src/test/
git commit -m "test(#15): setup independent test environment and mock fixtures"
git push origin feature/issue-15-test-setup-shift-left

# 4. Tạo Pull Request trên GitHub để Hoà duyệt
```

> Phân công/ước lượng hiện hành và task chủ hộ bổ sung: xem [quy chuẩn](../../PROJECT_CONVENTIONS.md) và phần “Điều chỉnh Sprint 1” trong [backlog](github_issues_backlog.md). Các task lớn trong cẩm nang là gói cha; giao các phần việc 2–4 giờ trong backlog.
