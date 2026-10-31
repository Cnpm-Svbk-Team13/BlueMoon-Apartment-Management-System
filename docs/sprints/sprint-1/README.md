# 🚀 SPRINT 1: HỆ THỐNG ĐIỀU HÀNH & PHÂN RÃ CÔNG VIỆC
## Dự án: BlueMoon Facility Management System (BlueMoon BFMS)
### Học phần: IT3180 - Nhập môn Công nghệ Phần mềm | Nhóm 13 (Team 13) - SoICT / HUST

---

## 📌 THÔNG TIN TỔNG QUAN SPRINT 1

- **Thời gian thực hiện:** Đúng **7 ngày liên tục** (Từ **31/10/2026** đến **06/11/2026**).
- **Dung lượng cam kết:** **30 Story Points (117 giờ làm việc)**.
- **Sprint Goal:**  
  > *Thiết lập hoàn chỉnh khung kiến trúc dự án **Spring Boot 3.5.16 (Java 25 LTS)** theo mô hình Pure Web, kết nối và nạp kịch bản CSDL **PostgreSQL** (vá triệt để các lỗi thiếu `start_date`/`end_date` và `version`), hiện thực hóa thành công phân hệ **Xác thực Đăng nhập, Cơ chế khóa Brute-force 15 phút sau 5 lần sai, Quản lý Tài khoản BQT & Phân quyền RBAC**, kết hợp phân hệ **Quản lý Căn hộ 30 tầng & Chủ hộ** nền tảng, đáp ứng 100% tiêu chí kiểm thử Shift-Left và chuẩn mực học thuật của Viện CNTT&TT (SoICT / HUST).*

---

## 🗺️ BẢN ĐỒ ĐIỀU HƯỚNG CẨM NANG THÀNH VIÊN

Mỗi thành viên chỉ cần mở đúng tệp tài liệu hướng dẫn riêng của mình để xem chi tiết danh mục công việc, checklist từng ngày và tiêu chuẩn nghiệm thu:

| Thành viên | Vai trò Sprint 1 | Trách nhiệm cốt lõi | Cẩm nang hành động riêng | Khối lượng |
|---|---|---|---|:---:|
| **TV1: Khánh** | Product Owner & Lead BA | Quản trị tiến độ Kanban, soạn 12 kịch bản UAT, trực tiếp nghiệm thu web | 📄 [**`TV1_Khanh_PO_BA.md`**](./TV1_Khanh_PO_BA.md) | 5 SP (~20h) |
| **TV2: Sơn** | Database & Backend Dev | Kịch bản DB `V1.1`, ánh xạ Entity JPA, CRUD Căn hộ 30 tầng (`US-001`) | 📄 [**`TV2_Son_Database_Backend.md`**](./TV2_Son_Database_Backend.md) | 7 SP (~25h) |
| **TV3: Hoà** | Tech Lead & Core Backend | Khung Spring Boot 3.5.16, Security 6, API Đăng nhập/Khóa 15 phút, Gatekeeper PR | 📄 [**`TV3_Hoa_TechLead_Security.md`**](./TV3_Hoa_TechLead_Security.md) | 8 SP (~28h) |
| **TV4: Đức Anh** | Frontend Dev & Data Eng | Master Layout Thymeleaf, Giao diện Login, UI Căn hộ & Quản lý User | 📄 [**`TV4_DucAnh_Frontend_UI.md`**](./TV4_DucAnh_Frontend_UI.md) | 6 SP (~24h) |
| **TV5: Huy** | QA Lead & Test Engineer | Shift-Left testing, Unit test CFG Basis Path, Decision Table, MockMvc | 📄 [**`TV5_Huy_QA_Automation.md`**](./TV5_Huy_QA_Automation.md) | 4 SP (~20h) |

> 📋 **Danh mục Thẻ Công việc Backlog:** [**`github_issues_backlog.md`**](./github_issues_backlog.md) (18 thẻ sẵn sàng đưa lên GitHub Kanban).  
> 📐 **Chứng minh Học thuật Kiểm thử:** [**`kiem_thu_hoc_thuat_cfg_decision_table.md`**](./kiem_thu_hoc_thuat_cfg_decision_table.md) (Đồ thị CFG theo mã nguồn đã chốt và Bảng quyết định 6 Rules).

---

## 🛡️ BẢN ĐỒ PHÂN QUYỀN MÃ NGUỒN (CODE OWNERSHIP MAP)

Để tránh tối đa xung đột mã nguồn (Merge Conflict) khi 5 người cùng đẩy mã lên repository, ranh giới file/folder được phân định rõ:

```
IT3180_BlueMoon-Management/
│
├── pom.xml                                    ───► [HOÀ HOẶC SƠN QUẢN TRỊ]
├── src/main/resources/application.yml         ───► [HOÀ HOẶC SƠN QUẢN TRỊ]
│
├── database/migrations/V1.1__*.sql            ───► [SƠN (TV2)]
│
├── src/main/java/com/bluemoon/bfms/
│   ├── config/SecurityConfig.java             ───► [HOÀ (TV3)]
│   ├── model/ (JPA Entities)                  ───► [SƠN (TV2)]
│   ├── repository/ (Apartment, Household)     ───► [SƠN (TV2)]
│   ├── repository/ (User)                     ───► [HOÀ (TV3)]
│   ├── service/ApartmentService.java          ───► [SƠN (TV2)]
│   ├── service/AuthService, UserService.java  ───► [HOÀ (TV3)]
│   ├── controller/AdminController (Apartment) ───► [SƠN (TV2)]
│   ├── controller/AuthController, AdminUser   ───► [HOÀ (TV3)]
│   ├── dto/                                   ───► [ĐỨC ANH (TV4) & SƠN / HOÀ]
│   └── exception/GlobalExceptionHandler.java  ───► [HOÀ (TV3)]
│
├── src/main/resources/
│   ├── templates/                             ───► [ĐỨC ANH (TV4)]
│   └── static/css/, static/js/                ───► [ĐỨC ANH (TV4)]
│
├── src/test/java/                             ───► [HUY (TV5)]
│
└── docs/sprints/sprint-1/                     ───► [KHÁNH (TV1) & TOÀN ĐỘI]
    ├── UAT_SCENARIOS_SPRINT_1.md              ───► [KHÁNH (TV1)]
    └── UAT_REPORT_SPRINT_1.md                 ───► [KHÁNH (TV1)]
```

---

## 🌿 QUY CHUẨN PHÂN NHÁNH GITFLOW (1 ISSUE = 1 BRANCH)

Mỗi khi thành viên nhận một thẻ task từ cột `To Do` trên bảng GitHub Kanban:

### 1. Tạo nhánh riêng từ `develop`
```bash
git checkout develop
git pull origin develop
git checkout -b feature/issue-<ID>-<mo-ta-ngan>
```
*Ví dụ:*
- Hoà: `git checkout -b feature/issue-03-spring-security`
- Sơn: `git checkout -b feature/issue-05-apartment-crud`
- Đức Anh: `git checkout -b feature/issue-08-login-ui`
- Huy: `git checkout -b feature/issue-11-auth-cfg-tests`
- Khánh: `git checkout -b feature/issue-02-uat-scenarios`

### 2. Cam kết mã nguồn (Commit Convention)
Cú pháp commit bắt buộc phải gắn mã thẻ Issue:
```bash
git commit -m "feat(#05): implement ApartmentService with 30 floors validation"
git commit -m "test(#11): add basis paths theo CFG đã chốt unit test for AuthService"
git commit -m "docs(#02): draft 12 uat test scenarios for sprint 1"
```

### 3. Đóng tác vụ qua Pull Request (Gatekeeper Protocol)
1. Đẩy nhánh lên GitHub: `git push origin feature/issue-<ID>-<mo-ta-ngan>`.
2. Mở Pull Request trỏ đích vào nhánh `develop`.
3. **Người duyệt (Reviewer):** Bắt buộc là **Hoà (Tech Lead)** kiểm tra tuân thủ kiến trúc Pure Web, chạy lệnh `mvn test` pass xanh rồi mới nhấn nút **Merge**.

---

## 📅 LỊCH TRÌNH 7 NGÀY SPRINT 1 (31/10 – 06/11/2026)

| Ngày | Hoạt động Toàn đội | Sản phẩm Cuối ngày |
|:---:|---|---|
| **Ngày 1 (31/10)** | Họp Planning 45p. Hoà làm khung App, Sơn làm SQL `V1.1`, Huy setup test, Khánh dựng Kanban & phác thảo UAT. | App biên dịch sạch, DB `V1.1` sẵn sàng, test setup xong. |
| **Ngày 2 (01/11)** | Sơn nạp DB & ánh xạ JPA Entity; Hoà cấu hình Security 6; Đức Anh làm Master Layout; Huy dựng khung test stubs. | Kết nối DB PostgreSQL thành công; Layout HTML chuẩn; Security filter chạy. |
| **Ngày 3 (02/11)** | Hoà viết `AuthService` khóa 15 phút sau 5 lần sai; Đức Anh làm UI Login; Huy ráp test các Basis Paths theo CFG đã chốt chạy thật. | Logic Đăng nhập pass 100% test; Màn hình Login HTML sẵn sàng. |
| **Ngày 4 (03/11)** | Đức Anh & Hoà ghép UI Login với Security; Khánh kiểm thử đăng nhập trên web; Sơn bắt đầu code `ApartmentService`. | Đăng nhập trên web thành công; Sơn bắt đầu logic Căn hộ. |
| **Ngày 5 (04/11)** | Sơn hoàn thành CRUD Căn hộ 30 tầng; Đức Anh làm UI Căn hộ; Huy chạy test Bảng quyết định bắt lỗi biên. | Backend Căn hộ hoàn tất; Giao diện Căn hộ sẵn sàng; Test biên pass 100%. |
| **Ngày 6 (05/11)** | Đức Anh ghép nối UI Căn hộ với Controller; Khánh test UAT trên web; Huy viết `MockMvc` Integration Test. | Thao tác Thêm/Sửa Căn hộ chạy mượt trên web; Integration test pass. |
| **Ngày 7 (06/11)** | **Code Freeze lúc 12:00 trưa**. Huy đo Coverage $\ge 85\%$. Khánh tổng kết biên bản UAT. Họp Demo Sprint Review & Retrospective. | 100% ca kiểm thử pass xanh; Biên bản nghiệm thu hoàn tất; Đóng gói Deliverables D1–D5. |
