[🏠 Trang chủ Docs](../README.md) > **Giai đoạn 1: Khai phá Yêu cầu & Kế hoạch Dự án**

---

# HỒ SƠ ĐỀ XUẤT VÀ KHAI PHÁ YÊU CẦU DỰ ÁN - TEAM 13
## Dự án: Phần mềm Quản lý Thu phí Chung cư BlueMoon (BlueMoon Management System - BMS)

---

## 1. Giới thiệu hồ sơ

Tài liệu này được biên dịch và số hóa toàn bộ từ tài liệu chính thức của **Team 13** cùng tài liệu hướng dẫn chuẩn của Bộ môn Công nghệ Phần mềm (SoICT / HUST), bao gồm:
- Toàn bộ các tabs trong tài liệu Google Docs: [CNPM_Proposal_Team13](https://docs.google.com/document/d/13JWVa9AGVlMYZVp1FaXzx0d7puReyfxI4C452kgso9o/edit?tab=t.gzj0xzndg9s3)
- Toàn bộ nội dung biểu đồ và text shapes trong Google Sheet: `CNPM_Team13_Epic-US` (Epic-Focused User Story Map)
- Bảng phân công nhiệm vụ và tiến độ: `IT3180_BTL_Team13`
- Bảng đặc tả chi tiết User Stories & Acceptance Criteria: `CNPM_Team13_US-AC.xlsx`
- Bộ hồ sơ báo cáo hiện trạng: `ClientWishList`, `AsIsScenario`, `RawNFR`, `TraceabilityMatrix`, `TuyenNgon`.
- Tài liệu hướng dẫn chính thức từ Bộ môn CNPM: `Huong dan xay dung Ke hoach du an.pdf` và các biểu mẫu Excel tham chiếu (`Mẫu WBS`, `Mẫu Ma trận Rủi ro`, `Mẫu Theo dõi Rủi ro`).

Mục tiêu của bộ tài liệu này là cung cấp nguồn tri thức chuẩn xác, đầy đủ và trực quan dưới dạng Markdown và sơ đồ (Mermaid) để các AI Agent và thành viên nhóm đọc hiểu và triển khai công việc một cách hiệu quả nhất.

---

## 2. Cấu trúc thư mục tài liệu

```text
docs/01_requirements_and_planning/
├── README.md                                      # Tổng quan và hướng dẫn điều hướng giai đoạn 1
├── 00_tong_quan_proposal.md                       # Tổng hợp tài liệu văn bản & khung báo cáo
├── 01_phac_thao_va_ke_hoach_phan_cong.md         # Tab 0 & Tab 1: Phác thảo 5 bước và bảng phân công 5 TV / 6 ngày
├── 02_ke_hoach_chi_tiet_khai_pha.md               # Tab 2: Kế hoạch chi tiết khai phá yêu cầu (Bối cảnh, MoSCoW, JAD...)
├── 03_tuyen_ngon_du_an.md                         # Tab 5: Tài liệu Tuyên ngôn dự án sơ khởi (Scope, Stakeholders, SMART)
├── 04_ke_hoach_du_an_va_user_story_map.md         # Tab 6: Kế hoạch dự án & User Story Map trích xuất từ Drawing Shapes
├── 05_chi_tiet_us_va_acceptance_criteria.md       # Bảng chi tiết US-001 -> US-011 (AC, DoD, Story Points)
├── 06_client_wishlist_as_is_to_be_nfr.md          # 32 RAW Wishlist, As-Is Map, To-Be Map, 5 NFRs định lượng
├── 07_traceability_matrix_va_quan_ly_rui_ro.md    # Tab 7: Traceability Matrix 5 tầng, Conflict Resolution & Risk Log ban đầu
├── 08_phu_luc_tuan_thu_phap_ly.md                 # Tab 3: Phụ lục Yêu cầu Tuân thủ và Pháp lý (Luật Cư trú, TT 02/2016)
├── 09_ke_hoach_du_an_chi_tiet.md                  # [CHUẨN SOICT - BÀI 4.2] Cấu trúc phân cấp công việc WBS 2 dạng (Sản phẩm & Scrum), RACI & Gantt Chart
└── 10_ke_hoach_quan_ly_rui_ro_toan_dien.md        # [CHUẨN SOICT - BÀI 4.3] Thang đo Bảng 4-2/4-3, Ma trận 3x3, 13 rủi ro chi tiết & Risk Tracking Log
```

---

## 3. Bản đồ ánh xạ các Tab Google Docs sang Markdown

| Tab trong Google Docs | Tiêu đề Tab | Tập tin Markdown tương ứng | Trọng tâm nội dung |
|---|---|---|---|
| **Đề bài gốc** | Đề bài BTL BlueMoon | [`../02_design_and_architecture/DE_BAI_GOC.md`](../02_design_and_architecture/DE_BAI_GOC.md) | Giới thiệu bài toán, quy mô tòa nhà 30 tầng, phân loại phí, đánh giá ảnh chụp phiếu thu viết tay Hình 1-1. |
| **Tab 0** (`t.0`) | Phác thảo công việc | [`01_phac_thao_va_ke_hoach_phan_cong.md`](./01_phac_thao_va_ke_hoach_phan_cong.md) | 5 bước khai phá yêu cầu (Preparation, Execution, Modeling, Analysis, Documentation) và kết quả dự kiến (Deliverables). |
| **Tab 1** (`t.8i9vt2uy4m8v`) | Kế hoạch phân công | [`01_phac_thao_va_ke_hoach_phan_cong.md`](./01_phac_thao_va_ke_hoach_phan_cong.md) | Bảng ánh xạ 5 nhóm tài liệu vào kế hoạch phân công cho 5 thành viên trong 6 ngày. |
| **Tab 2** (`t.8a2x2coysd5z`) | KẾ HOẠCH CHI TIẾT KHAI PHÁ YÊU CẦU | [`02_ke_hoach_chi_tiet_khai_pha.md`](./02_ke_hoach_chi_tiet_khai_pha.md) | Bối cảnh tòa nhà 30 tầng, bài toán thu phí m², phân công cụ thể 5 vai trò BA, ma trận MoSCoW, giải quyết xung đột, quản lý rủi ro và 9 deliverables. |
| **Tab 3** (`t.aka73ek3j2wa`) | Phụ lục | [`08_phu_luc_tuan_thu_phap_ly.md`](./08_phu_luc_tuan_thu_phap_ly.md) | 4 yêu cầu tuân thủ pháp lý: Luật Cư trú 2020, Thông tư 02/2016/TT-BXD, Nghị định 93/2021/NĐ-CP, Quy định hành chính quận Hà Đông. |
| **Tab 4** (`t.hivp13wc4fuc`) | Tổng hợp tài liệu văn bản | [`00_tong_quan_proposal.md`](./00_tong_quan_proposal.md) | Nơi tổng hợp và ghi nhận nhận xét báo cáo giảng viên. |
| **Tab 5** (`t.kiriuddfxg5o`) | Tuyên ngôn dự án | [`03_tuyen_ngon_du_an.md`](./03_tuyen_ngon_du_an.md) | Tài liệu Tuyên ngôn dự án sơ khởi: Mã PRJ-BLUEMOON-2026, Scope Statement, Stakeholder Matrix, Mục tiêu SMART, Kịch bản phỏng vấn. |
| **Tab 6** (`t.gzj0xzndg9s3`) | Kế hoạch dự án (Cơ sở) | [`04_ke_hoach_du_an_va_user_story_map.md`](./04_ke_hoach_du_an_va_user_story_map.md) | Gắn kết Google Sheet `IT3180_BTL_Team13` và toàn bộ **User Story Map** trích xuất từ hệ thống Shape/Drawing của Google Sheet `CNPM_Team13_Epic-US` (6 Epics lớn, Roles, Goals, Stories, Tasks, AC, Priority, Release Phase). |
| **Kế hoạch Dự án & WBS** | Bài 4.2 Chuẩn SoICT | [`09_ke_hoach_du_an_chi_tiet.md`](./09_ke_hoach_du_an_chi_tiet.md) | Cấu trúc phân cấp công việc WBS 2 dạng (theo sản phẩm bàn giao & theo quy trình Scrum 4 Sprints), Ma trận RACI 5 thành viên và Biểu đồ tiến độ Gantt Chart (Mermaid). |
| **Tab 7** (`t.sffelj7gkicr`) | Quản lý rủi ro (Cơ sở) | [`07_traceability_matrix_va_quan_ly_rui_ro.md`](./07_traceability_matrix_va_quan_ly_rui_ro.md) | Traceability Matrix 5 tầng kết nối Mục tiêu -> NFR -> Wishlist -> Epic -> User Story. |
| **Quản lý Rủi ro Toàn diện** | Bài 4.3 Chuẩn SoICT | [`10_ke_hoach_quan_ly_rui_ro_toan_dien.md`](./10_ke_hoach_quan_ly_rui_ro_toan_dien.md) | **Thang đo Bảng 4-2/4-3**, Ma trận Rủi ro $3 \times 3$ với 5 mức độ, Bảng 4-1 mở rộng 13 rủi ro chi tiết kèm hành động ngăn ngừa, Bảng theo dõi rủi ro (Risk Tracking Log). |
