# 🎨 CẨM NANG HÀNH ĐỘNG SPRINT 1 — THÀNH VIÊN 4: NGHIÊM ĐỨC ANH

> Bắt đầu với [đồ thị luồng công việc](WORKFLOW_GUIDE.md): đầu vào, bàn giao và điều kiện kết thúc của từng người. Phân công hiện hành: Khánh điều phối/BA/UAT, Sơn DB/Backend và nghiệm thu; review PR Sơn–Hoà.
## Vai trò: Frontend Developer & UI/UX Specialist
### Dự án: BlueMoon BFMS (IT3180 - Team 13)

---

## 🎯 1. TỔNG QUAN VAI TRÒ & SẢN PHẨM BÀN GIAO (DELIVERABLES)

- **Vai trò chính:** Thiết kế và xây dựng toàn bộ giao diện người dùng Thymeleaf đồng bộ, hiện đại, responsive theo nhận diện thương hiệu BlueMoon, bao gồm Master Layout, màn hình Đăng nhập (tích hợp CSRF & thông báo lỗi), màn hình Quản lý Tài khoản BQT và màn hình Quản lý Căn hộ 30 tầng.
- **Tổng thời gian cam kết:** **28 giờ ước lượng** (7 Story Points).
- **Quyền quản trị mã nguồn:**
  - `src/main/resources/templates/` (Toàn bộ các file HTML Thymeleaf).
  - `src/main/resources/static/` (CSS, JavaScript, hình ảnh logo).
  - Tầng DTO/Form request: `src/main/java/com/bluemoon/bfms/dto/`.

---

## 📌 2. DANH MỤC NHIỆM VỤ CHI TIẾT (TASK BREAKDOWN)

### Thẻ Issue 11: `[Sprint 1][FE] Xây dựng Master Layout Thymeleaf Chuẩn mực` (`FE-01`)
- **Khối lượng:** 6 giờ (1.5 SP).
- **Mã nhánh Git:** `feature/issue-11-master-layout-thymeleaf`
- **File cần tạo:**
  - `src/main/resources/templates/fragments/layout.html`:
    * Thanh Header: Hiển thị logo BlueMoon, họ tên và vai trò của cán bộ đang đăng nhập, nút Đăng xuất an toàn.
    * Thanh Sidebar điều hướng: Danh mục các phân hệ (Trang chủ/Dashboard, Quản lý Căn hộ, Quản lý Tài khoản BQT, Quản lý Thu phí, Báo cáo thống kê).
    * Khung Footer: Bản quyền Nhóm 13 - IT3180.
  - `src/main/resources/static/css/app.css` & `js/app.js`: Tích hợp Bootstrap 5 / Tailwind CSS, hỗ trợ modal popup và responsive mobile/desktop.

---

### Thẻ Issue 12: `[Sprint 1][FE] Xây dựng Giao diện Màn hình Đăng nhập` (`FE-02`)
- **Khối lượng:** 6 giờ (1.5 SP).
- **Mã nhánh Git:** `feature/issue-12-login-ui-screen`
- **File cần tạo:** `src/main/resources/templates/auth/login.html`
- **Yêu cầu kỹ thuật cốt lõi:**
  1. Tích hợp Form POST chuẩn mực với Spring Security: `th:action="@{/login}"`.
  2. Bắt buộc có token chống tấn công CSRF: `<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>`.
  3. Xử lý thông báo lỗi bằng tiếng Việt rõ ràng qua Thymeleaf:
     - Sai tài khoản hoặc mật khẩu: hiển thị lỗi chung "Tên đăng nhập hoặc mật khẩu không chính xác".
     - Tài khoản bị khóa: trả thông báo thất bại chung; backend vẫn khóa 15 phút sau lần sai thứ 5.
     - Tài khoản vô hiệu hóa: chặn đăng nhập và trả thông báo thất bại chung.

---

### Thẻ Issue 13: `[Sprint 1][FE] Xây dựng Màn hình Quản lý Tài khoản BQT` (`FE-03`)
- **Khối lượng:** 6 giờ (1.5 SP).
- **Mã nhánh Git:** `feature/issue-13-admin-users-ui`
- **File cần tạo:** `src/main/resources/templates/admin/users.html`
- **Yêu cầu giao diện:**
  - Bảng danh sách cán bộ BQT: Cột Tên đăng nhập, Họ và tên, Email, Vai trò (Badge màu khác nhau cho Admin, Kế toán, Thủ quỹ), Trạng thái (Đang hoạt động / Đã khóa).
  - Modal tạo mới tài khoản cán bộ: Form nhập thông tin, chọn vai trò và phân quyền.
  - Nút chuyển đổi trạng thái Kích hoạt / Vô hiệu hóa tài khoản.

---

### Thẻ Issue 14: `[Sprint 1][FE] Xây dựng Màn hình Dashboard & Danh mục Căn hộ` (`FE-04`)
- **Khối lượng:** 6 giờ (1.5 SP).
- **Mã nhánh Git:** `feature/issue-14-apartments-ui`
- **File cần tạo:**
  - `templates/admin/dashboard.html`: Thẻ thống kê tổng số căn hộ, số căn đang ở, số căn trống.
  - `templates/admin/apartments.html`:
    * Bảng danh sách căn hộ theo từng tầng (Tầng 1 Kiot, Tầng 2–5 Thương mại, Tầng 6–29 Căn hộ, Tầng 30 Penthouse).
    * Bộ lọc tìm kiếm nhanh theo mã phòng hoặc số tầng.
    * Modal Thêm mới / Cập nhật căn hộ: Nhập diện tích thông thủy $m^2$ (có validation phía trình duyệt: `min="0.01" step="0.01"` chặn nhập số âm).

---

## 📅 3. CHECKLIST HÀNH ĐỘNG TỪNG NGÀY (DAILY CHECKLIST)

- [ ] **Ngày 1 (31/10):** Thiết lập cấu trúc thư mục `templates/` và `static/`; tải và tích hợp Bootstrap 5 CSS/JS.
- [ ] **Ngày 2 (01/11):** Hoàn thành Master Layout Thymeleaf (`templates/fragments/layout.html`).
- [ ] **Ngày 3 (02/11):** Thiết kế hoàn chỉnh màn hình Đăng nhập `login.html` với đầy đủ các trạng thái thông báo lỗi.
- [ ] **Ngày 4 (03/11):** Phối hợp với Hoà ghép Form Login vào Spring Security; kiểm thử giao diện đăng nhập cùng Khánh.
- [ ] **Ngày 5 (04/11):** Thiết kế màn hình Quản lý tài khoản BQT (`users.html`) và Dashboard tổng quan.
- [ ] **Ngày 6 (05/11):** Hoàn thành màn hình Quản lý Căn hộ (`apartments.html`), phối hợp với Sơn ghép nối dữ liệu từ Controller.
- [ ] **Ngày 7 (06/11):** Tinh chỉnh CSS/JS, sửa các lỗi giao diện do Khánh phát hiện qua UAT; tham gia buổi Demo Sprint Review.

---

## 🛠️ 4. HƯỚNG DẪN DÙNG GIT CHO ĐỨC ANH

```bash
# 1. Kéo code develop mới nhất
git checkout develop
git pull origin develop

# 2. Tạo nhánh làm việc cho Master Layout
git checkout -b feature/issue-11-master-layout-thymeleaf

# 3. Commit và đẩy lên GitHub
git add src/main/resources/templates/fragments/layout.html src/main/resources/static/
git commit -m "feat(#11): create responsive thymeleaf master layout with sidebar and header"
git push origin feature/issue-11-master-layout-thymeleaf

# 4. Mở PR để Sơn hoặc Hoà duyệt; không tự duyệt
```

> Phân công/ước lượng hiện hành và task chủ hộ bổ sung: xem [quy chuẩn](../../PROJECT_CONVENTIONS.md) và phần “Điều chỉnh Sprint 1” trong [backlog](github_issues_backlog.md). Các task lớn trong cẩm nang là gói cha; giao các phần việc 2–4 giờ trong backlog.

### FE-05 — hồ sơ/lịch sử chủ hộ

Bổ sung 4h/1 SP ngày 04–05/11: form họ tên, CCCD, SĐT, email và danh sách lịch sử; hợp đồng dữ liệu theo BE-03, tiêu chí #21 trong backlog. Thông báo login trả lỗi chung, không hiển thị thông tin để dò username.
