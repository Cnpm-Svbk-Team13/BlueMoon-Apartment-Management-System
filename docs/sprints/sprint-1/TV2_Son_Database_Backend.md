# 🗄️ CẨM NANG HÀNH ĐỘNG SPRINT 1 — THÀNH VIÊN 2: NGUYỄN XUÂN SƠN

> Bắt đầu với [đồ thị luồng công việc](WORKFLOW_GUIDE.md): đầu vào, bàn giao và điều kiện kết thúc của từng người. Phân công hiện hành: Khánh điều phối/BA/UAT, Sơn DB/Backend và nghiệm thu; review PR Sơn–Hoà.

> Khi thiết kế dữ liệu/Entity, dùng [bảng tên DB → Java → DTO/form](DATA_NAMING_CONVENTIONS.md). Sơn chốt cột/FK thuộc phân hệ, phối hợp Hoà về `users` và Đức Anh về DTO; DB-02 cần xác nhận nguồn định danh và ánh xạ thật cho chủ hộ/lịch sử, không dùng bảng đề xuất như schema đã có.
## Vai trò: Trưởng nhóm / Database Engineer & Backend Developer
### Dự án: BlueMoon BFMS (IT3180 - Team 13)

---

## 🎯 1. TỔNG QUAN VAI TRÒ & SẢN PHẨM BÀN GIAO (DELIVERABLES)

- **Vai trò chính:** Quản trị cơ sở dữ liệu PostgreSQL, khởi tạo, kiểm tra và bàn giao schema/seed 7 bảng Sprint 1, ánh xạ tầng JPA Entity & Repository, và xây dựng toàn bộ backend CRUD Căn hộ 30 tầng (`US-001`).
- **Tổng thời gian cam kết:** **36 giờ ước lượng** (10 Story Points).
- **Quyền quản trị mã nguồn:**
  - `database/schema.sql`, `database/seed_data.sql`
  - Đồng quản trị file cấu hình `pom.xml` và `src/main/resources/application.yml` cùng Hoà (Tech Lead).
  - Tầng dữ liệu: `src/main/java/com/bluemoon/bfms/model/`, `repository/`, `service/ApartmentService.java`.

---

## 📌 2. DANH MỤC NHIỆM VỤ CHI TIẾT (TASK BREAKDOWN)

### Thẻ Issue 3: `[Sprint 1][DB] Khởi tạo, kiểm tra và bàn giao schema/seed Sprint 1` (`DB-01`)
- **Khối lượng:** 7 giờ (2 SP): review schema/seed và chuẩn bị bàn giao (3h), chạy PostgreSQL cô lập và ghi kết quả (4h).
- **Mã nhánh Git:** `feature/issue-03-sprint1-schema-seed`
- **File phụ trách:** `database/schema.sql`, `database/seed_data.sql`.
- **Checklist:**
  - [ ] `database/schema.sql` tạo đủ 7 bảng cùng FK/CHECK/UNIQUE/index/trigger trên PostgreSQL trống, không nạp vào database đã có cấu trúc.
  - [ ] `database/seed_data.sql` chỉ nạp khi cả 7 bảng rỗng; dữ liệu giả và tài khoản BCrypt khớp `database/README.md`.
  - [ ] Kiểm tra dữ liệu hợp lệ/sai: tầng và danh mục loại độc lập, diện tích, CCCD/SĐT, ngày cư trú, một hộ ACTIVE/căn, liên kết chủ hộ và bảo vệ lịch sử/audit; ghi kết quả PostgreSQL thực chạy.
  - [ ] Bàn giao commit/schema/seed, cách kết nối không lộ bí mật, dữ liệu mẫu, bằng chứng và giới hạn cho Hoà/Huy; không coi việc tạo bảng là đã hoàn thành nghiệp vụ.

Xem [hướng dẫn database](../../../database/README.md). Không chạy lại bootstrap hoặc seed trên database đã có dữ liệu; thay đổi cấu trúc đã triển khai cần phương án chuyển đổi riêng. Phần tài chính chưa có trong bootstrap Sprint 1.

---

### Thẻ Issue 4: `[Sprint 1][BE] Xây dựng Tầng JPA Entities & Repositories` (`BE-01`)
- **Khối lượng:** 9 giờ (2 SP).
- **Mã nhánh Git:** `feature/issue-04-jpa-entities-and-repositories`
- **Các file cần tạo trong `src/main/java/com/bluemoon/bfms/`:**
  - `model/ApartmentType.java` (Enum: `KIOT`, `COMMERCIAL`, `RESIDENTIAL`, `PENTHOUSE`)
  - `model/ApartmentStatus.java` (Enum: `VACANT`, `OCCUPIED`)
  - `model/Apartment.java` (Entity ánh xạ bảng `apartments`, diện tích bắt buộc dùng `BigDecimal`, `@Min(1)`, `@Max(30)`)
  - `model/Household.java` (Entity ánh xạ `households`)
  - `model/Resident.java` (Entity ánh xạ `residents`, có `LocalDate startDate`, `LocalDate endDate`)
  - `repository/ApartmentRepository.java` (kế thừa `JpaRepository<Apartment, Long>`, có `findByRoomNumber`, `existsByRoomNumber`, `findByFloorNumber`)
  - `repository/HouseholdRepository.java`

---

### Thẻ Issue 5: `[Sprint 1][BE] Hiện thực hóa Backend CRUD Căn hộ (US-001)` (`BE-02`)
- **Khối lượng:** 9 giờ (3 SP).
- **Mã nhánh Git:** `feature/issue-05-apartment-crud-service`
- **File cần tạo:**
  - `service/ApartmentService.java`:
    - `@Transactional(readOnly = true)` cho các hàm tìm kiếm.
    - `@Transactional(rollbackFor = Exception.class)` cho các hàm tạo mới, cập nhật.
    - Kiểm tra tầng 1–30 và loại trong KIOT/COMMERCIAL/RESIDENTIAL/PENTHOUSE độc lập nhau; không ràng buộc loại theo tầng.
    - Kiểm tra diện tích thông thủy: `areaSqm.compareTo(BigDecimal.ZERO) > 0`.
    - Chặn trùng mã phòng bằng `existsByRoomNumber`.
  - Viết controller điều hướng danh sách căn hộ và thêm mới căn hộ trong `AdminController.java`.

---

## 📅 3. CHECKLIST HÀNH ĐỘNG TỪNG NGÀY (DAILY CHECKLIST)

- [ ] **Ngày 1 (31/10):** Rà schema/seed 7 bảng, chạy thử trên PostgreSQL trống/cô lập và ghi bằng chứng.
- [ ] **Ngày 2 (01/11):** Kiểm tra và bàn giao schema/seed đã nạp; tạo các file Enum và Entity JPA (`Apartment`, `Household`, `Resident`) với `BigDecimal`.
- [ ] **Ngày 3 (02/11):** Viết các interface Repository (`ApartmentRepository`). Viết query tìm kiếm căn hộ theo tầng.
- [ ] **Ngày 4 (03/11):** Bắt đầu lập trình `ApartmentService`: viết hàm `validateApartmentBusinessRules()` và `createApartment()`.
- [ ] **Ngày 5 (04/11):** Hoàn thành toàn bộ `ApartmentService` (thêm, sửa, đổi trạng thái). Bàn giao cho Huy (QA) chạy bộ test Bảng quyết định.
- [ ] **Ngày 6 (05/11):** Phối hợp với Đức Anh ghép nối Controller với giao diện Thymeleaf Căn hộ. Sửa các lỗi nếu test của Huy phát hiện.
- [ ] **Ngày 7 (06/11):** Rà soát mã nguồn sạch sẽ, không có code smells; tham gia buổi Demo Sprint Review.

---

## 🛠️ 4. HƯỚNG DẪN DÙNG GIT CHO SƠN

```bash
# 1. Kéo code develop mới nhất
git checkout develop
git pull origin develop

# 2. Tạo nhánh làm việc cho schema/seed Sprint 1
git checkout -b feature/issue-03-sprint1-schema-seed

# 3. Commit và đẩy lên GitHub
git add database/schema.sql database/seed_data.sql
git commit -m "docs(#03): record Sprint 1 schema and seed verification"
git push origin feature/issue-03-sprint1-schema-seed

# 4. Tạo Pull Request trên GitHub và nhờ Hoà duyệt (PR của Sơn không tự duyệt)
```

> Phân công/ước lượng hiện hành và task chủ hộ bổ sung: xem [quy chuẩn](../../PROJECT_CONVENTIONS.md) và phần “Điều chỉnh Sprint 1” trong [backlog](github_issues_backlog.md). Các task lớn trong cẩm nang là gói cha; giao các phần việc 2–4 giờ trong backlog.

### Công việc bổ sung

PM-01 thuộc Khánh (6h/2 SP); Sơn tập trung dữ liệu/backend. PM-03 giữ Sơn (3h/1 SP): review PR Hoà, kiểm tra bằng chứng test của Huy và hồ sơ/UAT Khánh tổng hợp trước khi xác nhận nghiệm thu nội bộ; không làm lại báo cáo. Task thiếu bằng chứng vẫn mở. Sơn chốt thay đổi phạm vi/ưu tiên sau trao đổi nhóm; Khánh vận hành Kanban/daily, theo dõi bàn giao và chủ trì Review/Retro. DB-02: hồ sơ/lịch sử chủ hộ 4h/1 SP (01–02/11); BE-03: transaction thay chủ và lịch sử 4h/1 SP (03–04/11). Xem #19/#20/#24 trong backlog để có AC và phụ thuộc. Đây là ước lượng, cần đối chiếu thời gian thực tế có thể dành; không tự suy ra đủ năng lực.

## Đồng bộ dữ liệu Sprint 1 — quyết định 04/11/2026

US-001 / RAW-001, RAW-002; US-SEC-01/02 / NFR-02 → DB-01, DB-02, BE-01/02/03. Giữ phân công và ước lượng hiện hành. Thiết kế 7 bảng mới dùng persons làm nguồn hồ sơ chung, users.person_id duy nhất; chủ hộ không phải chủ sở hữu. Loại mặt bằng độc lập tầng; trạng thái căn chỉ VACANT/OCCUPIED. Hộ gắn cố định một căn; lịch sử household_head_history tham chiếu hộ, không lưu căn/event_type độc lập. Quan hệ cư trú HEAD/SPOUSE/CHILD/PARENT/TENANT/OTHER được kiểm tra cuối transaction. Mỗi người có tối đa một hồ sơ cư trú hiện tại; chưa nghiệm thu US-002/003.

Form tài khoản lấy/sửa hồ sơ qua Person; form căn vẫn nhập loại mặt bằng độc lập tầng. Các hướng dẫn tầng–loại cố định hoặc owner-history trong hồ sơ cũ được thay thế bằng quyết định này. Không thay lời khai phá RAW gốc. Đây là thay đổi dữ liệu/tài liệu, chưa triển khai Java, chưa áp dụng VM hoặc nghiệm thu Sprint. Handoff phải đối chiếu ERD hiện hành, bảng ánh xạ và báo cáo kiểm thử PostgreSQL 04/11.
