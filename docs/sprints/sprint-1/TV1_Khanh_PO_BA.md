# 📋 CẨM NANG HÀNH ĐỘNG SPRINT 1 — THÀNH VIÊN 1: PHẠM NAM KHÁNH
## Vai trò: Lead Business Analyst (BA) & UAT
### Dự án: BlueMoon BFMS (IT3180 - Team 13)

---

## 🎯 1. TỔNG QUAN VAI TRÒ & SẢN PHẨM BÀN GIAO (DELIVERABLES)

- **Vai trò chính:** Phân tích yêu cầu, hỗ trợ Sơn cập nhật bảng Kanban, trực tiếp soạn thảo kịch bản kiểm thử chấp nhận người dùng (UAT) và thực thi UAT tính năng trên giao diện web do Đức Anh xây dựng.
- **Tổng thời gian cam kết:** **16 giờ ước lượng** (4 Story Points).
- **Sản phẩm đóng góp cụ thể trên Git (Minh chứng chấm điểm cá nhân IT3180):**
  1. Tệp kịch bản kiểm thử: `docs/sprints/sprint-1/UAT_SCENARIOS_SPRINT_1.md` (sản phẩm dự kiến) (12 kịch bản nền, bổ sung kịch bản chủ hộ BA-03).
  2. Tệp biên bản nghiệm thu: `docs/sprints/sprint-1/UAT_REPORT_SPRINT_1.md` (sản phẩm dự kiến) (Nhật ký lỗi và biên bản ký xác nhận nghiệm thu giao diện web).
  3. Bảng Kanban GitHub Projects cập nhật trạng thái thẻ hàng ngày.

---

## 📌 2. DANH MỤC NHIỆM VỤ CHI TIẾT (TASK BREAKDOWN)

### Điều phối PM-01

PM-01 (6h/2 SP) do Sơn chịu trách nhiệm; Khánh hỗ trợ Kanban với vai trò BA. Khánh giữ PM-02 và bổ sung BA-03 (2h/1 SP) cho UAT chủ hộ.

### Thẻ Issue 2: `[Sprint 1][BA] Xây dựng 12 Kịch bản UAT & Báo cáo Nghiệm thu Web` (`PM-02`)
- **Khối lượng:** 14 giờ (3 SP).
- **Mã nhánh Git:** `feature/issue-02-uat-scenarios-and-testing`
- **Công việc cụ thể:**
  1. Soạn thảo tệp `UAT_SCENARIOS_SPRINT_1.md` với **12 kịch bản thực tế** tại chung cư BlueMoon Văn Phú:
     - *Kịch bản 1:* Đăng nhập thành công với tài khoản Admin (`admin` / `Password@123`).
     - *Kịch bản 2:* Đăng nhập thất bại do sai mật khẩu lần 1, 2, 3 (kiểm tra lỗi chung, bộ đếm phía backend).
     - *Kịch bản 3:* Đăng nhập sai liên tiếp lần thứ 5 (kiểm tra backend khóa tạm thời 15 phút; UI lỗi chung).
     - *Kịch bản 4:* Đăng nhập bằng tài khoản bị khóa trong thời gian 15 phút (bị chặn truy cập).
     - *Kịch bản 5:* Đăng nhập bằng tài khoản đã bị vô hiệu hóa (`is_active = false`).
     - *Kịch bản 6:* Đổi mật khẩu chủ động thành công và đăng nhập lại bằng mật khẩu mới.
     - *Kịch bản 7:* Thêm mới Kiot thương mại tại Tầng 1 (hợp lệ).
     - *Kịch bản 8:* Thêm mới Căn hộ cư dân tại Tầng 12 diện tích $75.5\text{ m}^2$ (hợp lệ).
     - *Kịch bản 9:* Thêm mới Căn hộ Penthouse tại Tầng 30 diện tích $250\text{ m}^2$ (hợp lệ).
     - *Kịch bản 10 (Lỗi biên):* Thử thêm căn hộ với diện tích âm hoặc bằng 0 (hệ thống phải báo lỗi tiếng Việt).
     - *Kịch bản 11 (Lỗi biên):* Thử đặt Penthouse tại Tầng 1 hoặc Kiot tại Tầng 30 (hệ thống phải chặn vì xung đột thiết kế tòa nhà).
     - *Kịch bản 12 (Lỗi trùng):* Thử thêm phòng có mã phòng đã tồn tại (hệ thống báo lỗi trùng lặp).
  2. Khi Đức Anh hoàn thành giao diện web (`login.html`, `apartments.html`), Khánh mở trình duyệt (Chrome/Edge), trực tiếp bấm thao tác thử nghiệm theo 12 kịch bản trên.
  3. Lập nhật ký lỗi (Bug Log) chuyển cho Hoà/Sơn/Đức Anh fix.
  4. Tổng kết biên bản nghiệm thu đạt chuẩn `UAT_REPORT_SPRINT_1.md` và commit lên Git.

---

## 📅 3. CHECKLIST HÀNH ĐỘNG TỪNG NGÀY (DAILY CHECKLIST)

- [ ] **Ngày 1 (31/10):** Hỗ trợ Sơn cập nhật Kanban từ backlog; chuẩn bị BA-03. Bắt đầu phác thảo khung 12 kịch bản UAT.
- [ ] **Ngày 2 (01/11):** Thống nhất kịch bản UAT luồng Đăng nhập với Huy (QA Lead) để khớp với đồ thị kiểm thử CFG.
- [ ] **Ngày 3 (02/11):** Cập nhật tiến độ daily; hoàn thiện kịch bản UAT cho luồng Căn hộ 30 tầng.
- [ ] **Ngày 4 (03/11):** Mở web thử nghiệm chức năng Đăng nhập do Đức Anh & Hoà vừa ghép nối. Bắt lỗi giao diện (nếu có).
- [ ] **Ngày 5 (04/11):** Theo dõi tiến độ Backend Căn hộ của Sơn và UI của Đức Anh.
- [ ] **Ngày 6 (05/11):** Trực tiếp kiểm thử UAT thao tác Thêm/Sửa Căn hộ trên trình duyệt. Lập Bug Log nếu phát hiện lỗi.
- [ ] **Ngày 7 (06/11):** Xác nhận các bug đã được sửa xong; hoàn thiện biên bản nghiệm thu `UAT_REPORT_SPRINT_1.md`; hỗ trợ Sơn chủ trì buổi Demo Sprint Review & Retrospective.

---

## 🛠️ 4. HƯỚNG DẪN DÙNG GIT CHO KHÁNH

```bash
# 1. Kéo code mới nhất từ develop về máy
git checkout develop
git pull origin develop

# 2. Tạo nhánh làm việc cho kịch bản UAT
git checkout -b feature/issue-02-uat-scenarios

# 3. Sau khi viết xong file docs/sprints/sprint-1/UAT_SCENARIOS_SPRINT_1.md
git add docs/sprints/sprint-1/UAT_SCENARIOS_SPRINT_1.md
git commit -m "docs(#02): add 12 uat test scenarios for sprint 1"

# 4. Đẩy nhánh lên GitHub và tạo Pull Request
git push origin feature/issue-02-uat-scenarios
```

> Phân công/ước lượng hiện hành và task chủ hộ bổ sung: xem [quy chuẩn](../../PROJECT_CONVENTIONS.md) và phần “Điều chỉnh Sprint 1” trong [backlog](github_issues_backlog.md). Các task lớn trong cẩm nang là gói cha; giao các phần việc 2–4 giờ trong backlog.
