# 🗄️ CẨM NANG HÀNH ĐỘNG SPRINT 1 — THÀNH VIÊN 2: NGUYỄN XUÂN SƠN
## Vai trò: Database Engineer & Backend Developer
### Dự án: BlueMoon BFMS (IT3180 - Team 13)

---

## 🎯 1. TỔNG QUAN VAI TRÒ & SẢN PHẨM BÀN GIAO (DELIVERABLES)

- **Vai trò chính:** Quản trị cơ sở dữ liệu PostgreSQL, soạn thảo kịch bản migration CSDL `V1.1` khắc phục toàn bộ 7 lỗi DDL/DML đã phát hiện, ánh xạ tầng JPA Entity & Repository, và xây dựng toàn bộ backend CRUD Căn hộ 30 tầng (`US-001`).
- **Tổng thời gian cam kết:** **25 giờ làm việc** (7 Story Points).
- **Quyền quản trị mã nguồn:**
  - `database/migrations/V1.1__fix_schema_and_seed.sql`
  - Đồng quản trị file cấu hình `pom.xml` và `src/main/resources/application.yml` cùng Hoà (Tech Lead).
  - Tầng dữ liệu: `src/main/java/com/bluemoon/bfms/model/`, `repository/`, `service/ApartmentService.java`.

---

## 📌 2. DANH MỤC NHIỆM VỤ CHI TIẾT (TASK BREAKDOWN)

### Thẻ Issue 3: `[Sprint 1][DB] Soạn thảo Kịch bản Migration CSDL V1.1` (`DB-01`)
- **Khối lượng:** 7 giờ (2 SP).
- **Mã nhánh Git:** `feature/issue-03-db-migration-v1-1`
- **File tạo mới:** `database/migrations/V1.1__fix_schema_and_seed.sql`
- **7 Điểm cần vá trong CSDL PostgreSQL:**
  1. Thêm `residence_status NOT NULL` và 2 cột `start_date DATE`, `end_date DATE` vào bảng `residents` kèm ràng buộc: Nếu là `TAM_TRU` hoặc `TAM_VANG` thì bắt buộc `start_date` và `end_date` không được null, và `end_date >= start_date` (Tuân thủ Luật Cư trú 2020).
  2. Thêm cột `version BIGINT NOT NULL DEFAULT 0` vào bảng `invoices` để kích hoạt khóa lạc quan `@Version`.
  3. Thêm ràng buộc XOR vào bảng `receipts`: Một phiếu thu chỉ được gắn với hóa đơn bắt buộc HOẶC đóng góp tự nguyện, không được cả hai (Bất biến 1: NĐ 93/2021/NĐ-CP).
  4. Đổi kiểu dữ liệu `apartments.area_sqm` sang `DECIMAL(12,2)` và thêm kiểm tra `area_sqm > 0`.
  5. Đổi khóa duy nhất `households.apartment_id` sang Partial Unique Index (`WHERE status = 'ACTIVE'`) để bảo toàn lịch sử chủ hộ cũ.
  6. Thay toàn bộ mật khẩu giả lập trong seed data bằng mã băm BCrypt chuẩn cho `Password@123`:  
     `$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK`
  7. Nạp dữ liệu mẫu cho các căn hộ mẫu từ tầng 1 đến tầng 30 (Kiot, Thương mại, Căn hộ ở, Penthouse).

---

### Thẻ Issue 4: `[Sprint 1][BE] Xây dựng Tầng JPA Entities & Repositories` (`BE-01`)
- **Khối lượng:** 9 giờ (2 SP).
- **Mã nhánh Git:** `feature/issue-04-jpa-entities-and-repositories`
- **Các file cần tạo trong `src/main/java/com/bluemoon/bfms/`:**
  - `model/ApartmentType.java` (Enum: `KIOT`, `COMMERCIAL`, `RESIDENTIAL`, `PENTHOUSE`)
  - `model/ApartmentStatus.java` (Enum: `VACANT`, `OCCUPIED`, `RENOVATING`)
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
    - Hàm kiểm tra quy tắc phân tầng tòa nhà:
      * Tầng 1: Loại hình phải là `KIOT`.
      * Tầng 2–5: Loại hình phải là `COMMERCIAL`.
      * Tầng 6–29: Loại hình phải là `RESIDENTIAL`.
      * Tầng 30: Loại hình phải là `PENTHOUSE`.
    - Kiểm tra diện tích thông thủy: `areaSqm.compareTo(BigDecimal.ZERO) > 0`.
    - Chặn trùng mã phòng bằng `existsByRoomNumber`.
  - Viết controller điều hướng danh sách căn hộ và thêm mới căn hộ trong `AdminController.java`.

---

## 📅 3. CHECKLIST HÀNH ĐỘNG TỪNG NGÀY (DAILY CHECKLIST)

- [ ] **Ngày 1 (31/10):** Viết script SQL `database/migrations/V1.1__fix_schema_and_seed.sql`. Chạy thử nghiệm trên PostgreSQL local.
- [ ] **Ngày 2 (01/11):** Nạp migration CSDL thành công; tạo các file Enum và Entity JPA (`Apartment`, `Household`, `Resident`) với `BigDecimal`.
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

# 2. Tạo nhánh làm việc cho migration CSDL
git checkout -b feature/issue-03-db-migration-v1-1

# 3. Commit và đẩy lên GitHub
git add database/migrations/V1.1__fix_schema_and_seed.sql
git commit -m "feat(#03): add v1.1 database migration script with all constraint fixes"
git push origin feature/issue-03-db-migration-v1-1

# 4. Tạo Pull Request trên GitHub và nhờ Sơn hoặc Hoà duyệt theo quy tắc không tự duyệt
```

> Phân công/ước lượng hiện hành và task chủ hộ bổ sung: xem [quy chuẩn](../../PROJECT_CONVENTIONS.md) và phần “Điều chỉnh Sprint 1” trong [backlog](github_issues_backlog.md). Các task lớn trong cẩm nang là gói cha; giao các phần việc 2–4 giờ trong backlog.
