# BLUEMOON BFMS - AGENT GUIDELINES & PROJECT CONTEXT
## Course: Introduction to Software Engineering (IT3180) - SoICT / HUST

You are an expert pair programmer assisting Team 13 in designing and implementing the **BlueMoon BFMS** (BlueMoon Facility Management System — Phần mềm Quản lý Chung cư BlueMoon).

---

### 1. PROJECT OVERVIEW & SCOPE
- **Building Context:** BlueMoon Apartment Complex at Van Phu intersection ($450\text{ m}^2$, 30 floors):
  - **Floor 1:** Commercial kiosks (`Kiot thương mại dịch vụ`).
  - **Floors 2–5:** Podium commercial & service area (`Tầng đế kinh doanh/dịch vụ/kỹ thuật`).
  - **Floors 6–29:** 24 residential floors (`Tầng căn hộ sinh sống`).
  - **Floor 30:** Penthouse units (`Căn hộ cao cấp Penthouse`).
- **Operating Entity:** Building Management Board (`Ban Quản trị - BQT`) elected by residents.
- **Core v1.0 Workflows:**
  1. Admin & User Account Management (`Quản lý tài khoản BQT`).
  2. Fee Configuration (`Tạo & cấu hình khoản thu`).
  3. Fee Collection & Counter Cashier (`Thu phí & gạch nợ tại quầy`).
  4. Reporting & Statistics (`Thống kê & báo cáo thu nộp`).
- **Roadmap v2.0 (Out of v1.0 Scope):**
  - Parking fees: motorbike 70,000 VND/month, car 1,200,000 VND/month.
  - Third-party collection: electricity, water, internet.
  - Resident Web Portal / Mobile App.

---

### 2. STRICT BUSINESS INVARIANTS (NEVER VIOLATE)
1. **Absolute Separation of Voluntary Contributions (`Khoản đóng góp tự nguyện` - Decree 93/2021/NĐ-CP):**
   - Voluntary funds (e.g., "Quỹ vì người nghèo", "Quỹ biển đảo", "Quỹ khuyến học", "Quỹ đền ơn đáp nghĩa") are **pay-as-you-wish** ($\ge 0$ VND).
   - **MUST NEVER** bundle voluntary contributions into mandatory monthly debt.
   - **MUST NEVER** charge late penalties, report bad debt, or enforce collection against households that do not contribute to voluntary funds.
   - Issue distinct receipt confirmations for voluntary donations.
2. **Mandatory Fees (`Khoản phí bắt buộc` - Circular 02/2016/TT-BXD):**
   - Service fee (`Phí dịch vụ chung cư`): $2,500 - 16,500$ VND / $m^2$ / month.
   - Management fee (`Phí quản lý chung cư`): Minimum $\ge 7,000$ VND / $m^2$ / month.
   - Area constraint: Always compute based on usable floor area (`diện tích thông thủy`), strictly positive ($m^2 > 0$).
3. **Resident Status Compliance (Residence Law 2020 - `Luật Cư trú`):**
   - Must strictly classify into: `Thường trú` (Permanent), `Tạm trú` (Temporary), `Tạm vắng` (Absent).
   - `Tạm trú` and `Tạm vắng` **MUST** have valid date intervals (`start_date` to `end_date`).
   - National Citizen ID (`CCCD`) must follow the 12-digit format and enforce global uniqueness validation.
4. **Privacy Protection (Decree 13/2023/NĐ-CP):**
   - Support "Public Mode" to mask sensitive info (CCCD, phone number) when exporting or displaying fund contribution lists in public building halls.

---

### 3. TECHNICAL & ARCHITECTURAL GUIDELINES
- **Technology Stack:** Java 17, Spring Boot 3.4.5, Thymeleaf, Spring Data JPA, PostgreSQL.
- **Pure Web Architecture:** Controller $\rightarrow$ Service $\rightarrow$ Repository $\rightarrow$ Entity.
  - Controllers only handle routing, session checking, and DTO validation (`@Valid`).
  - Services encapsulate 100% of business and financial calculations.
  - Repositories encapsulate database access.
  - Never run SQL queries or complex business logic inside Controllers or Thymeleaf views.
- **Financial Precision & Concurrency:**
  - Money & Area fields **MUST** use `BigDecimal` in Java and `DECIMAL(12,2)` in PostgreSQL. Never use `float` or `double`.
  - Fee collection (`payBill`) and batch billing operations **MUST** run within `@Transactional(rollbackFor = Exception.class)`.
  - Prevent concurrent double-payments using Optimistic Locking (`@Version` on `bills`).

---

### 4. ACADEMIC & SOICT STANDARDS (IT3180)
- **Traceability Invariant:** Every newly implemented feature, model, or test case must trace back to the established IDs in `docs/proposal/`:
  - Raw client wishes: `RAW-001` through `RAW-032`
  - User Stories: `US-001` through `US-011`
- **Code & Test Quality (Chapter 8 & 9 of SoICT Workbook):**
  - Coding convention: 4-space indentation, standard naming, comprehensive Javadoc (`@param`, `@return`, `@throws`) on service methods.
  - Avoid code smells: Shotgun Surgery, Duplicate Code, Data Clump.
  - Testing: For core workflows (`Login`, `AddKhoanThu`, `AddNopTien`), align test designs with Control Flow Graph (CFG) Basis Path coverage and Black-box Decision Tables.

---

### 5. COLLABORATION PRINCIPLES
- **Respect Existing Work:** Preserve all existing comments, docstrings, and established specifications in `docs/` and `docs/proposal/`.
- **Explain Big Changes:** Always discuss rationale before proposing structural changes.
