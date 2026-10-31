[🏠 Trang chủ Docs](../README.md) > [Giai đoạn 2: Thiết kế Hệ thống](./README.md) > **SRS.md**

---

# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## Hệ thống Quản lý Thu phí và Dân cư Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Giới thiệu tổng quan
Tài liệu Đặc tả Yêu cầu Phần mềm (SRS) này mô tả toàn diện các yêu cầu chức năng, yêu cầu phi chức năng, các tác nhân hệ thống và các điều kiện nghiệm thu cho dự án **BlueMoon BFMS**.

Bộ tài liệu yêu cầu chi tiết và kế hoạch dự án chính thức của nhóm được biên dịch và số hóa toàn bộ tại thư mục [`docs/01_requirements_and_planning/`](../01_requirements_and_planning/README.md) cùng văn bản đề bài gốc [`docs/02_design_and_architecture/DE_BAI_GOC.md`](./DE_BAI_GOC.md):
- [00. Tổng quan Hồ sơ Đề xuất](../01_requirements_and_planning/00_tong_quan_proposal.md)
- [01. Phác thảo và Kế hoạch Phân công Nhóm](../01_requirements_and_planning/01_phac_thao_va_ke_hoach_phan_cong.md)
- [02. Kế hoạch Chi tiết Khai phá Yêu cầu](../01_requirements_and_planning/02_ke_hoach_chi_tiet_khai_pha.md)
- [03. Tuyên ngôn Dự án (Project Charter sơ khởi)](../01_requirements_and_planning/03_tuyen_ngon_du_an.md)
- [04. Kế hoạch Dự án & Epic-Focused User Story Map](../01_requirements_and_planning/04_ke_hoach_du_an_va_user_story_map.md)
- [05. Chi tiết User Stories & Acceptance Criteria (US-001 -> US-011)](../01_requirements_and_planning/05_chi_tiet_us_va_acceptance_criteria.md)
- [06. Client Wish List, Kịch bản As-Is/To-Be & Yêu cầu Phi chức năng](../01_requirements_and_planning/06_client_wishlist_as_is_to_be_nfr.md)
- [07. Ma trận Truy vết Yêu cầu & Báo cáo Xử lý Xung đột](../01_requirements_and_planning/07_traceability_matrix_va_quan_ly_rui_ro.md)
- [08. Phụ lục Tuân thủ Pháp lý (Luật Cư trú, TT 02/2016, NĐ 93/2021)](../01_requirements_and_planning/08_phu_luc_tuan_thu_phap_ly.md)
- [09. Kế hoạch Dự án Chi tiết & WBS 2 dạng (Chuẩn SoICT)](../01_requirements_and_planning/09_ke_hoach_du_an_chi_tiet.md)
- [10. Kế hoạch Quản lý Rủi ro Toàn diện & Risk Log (Chuẩn SoICT)](../01_requirements_and_planning/10_ke_hoach_quan_ly_rui_ro_toan_dien.md)

---

## 2. Các Tác nhân Hệ thống (Actors)
1. **Quản trị viên Ban quản trị (BQT Admin):** Quản lý cấu hình toàn bộ tòa nhà, danh mục căn hộ (30 tầng), phân quyền tài khoản, giám sát bảo mật và kết xuất hồ sơ báo cáo cho Công an/Chính quyền.
2. **Kế toán Ban quản trị (Accountant):** Cấu hình định mức phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$), phí quản lý ($\ge 7.000\text{ đ}/m^2$), phát động chiến dịch quỹ tự nguyện, tạo bảng kê thu phí định kỳ hàng tháng và xem báo cáo tài chính.
3. **Thủ quỹ (Cashier):** Trực tiếp tiếp nhận nộp tiền tại quầy, tra cứu căn hộ, tích chọn gạch nợ, ghi nhận đóng góp tự nguyện và in biên lai thu tiền cho cư dân.
4. **Cư dân / Đại diện hộ gia đình (Resident):** Người nộp phí, nhận biên lai và tra cứu công nợ. *(Mở rộng v2.0: tương tác trực tiếp qua cổng Web Portal)*.
5. **Cơ quan chức năng (Công an phường / Tổ dân phố):** Bên tiếp nhận danh sách báo cáo nhân khẩu, tạm trú, tạm vắng và phối hợp vận động các quỹ xã hội.

---

## 3. Tóm tắt Yêu cầu Chức năng (Functional Requirements)

| Phân hệ (Epic) | Chức năng chính | Trạng thái v1.0 |
|---|---|:---:|
| **Quản lý Dân cư & Căn hộ** | Quản lý 30 tầng căn hộ, kiot, penthouse; quản lý sổ hộ khẩu, nhân khẩu trong hộ; cập nhật biến động nhân khẩu (sinh, tử, chuyển đi); đăng ký tạm trú, tạm vắng; xuất danh sách nộp cơ quan công an. | **Must-have** |
| **Quản lý Danh mục Khoản thu** | Cấu hình định mức phí dịch vụ theo $m^2$, phí quản lý theo $m^2$; tạo chiến dịch phát động các quỹ từ thiện, phong trào tự nguyện (vì người nghèo, biển đảo, khuyến học...). | **Must-have** |
| **Quản lý Thu phí & Biên lai** | Tự động phát sinh bảng kê công nợ hàng tháng; quầy thu phí tra cứu theo số phòng; gạch nợ tự động; sinh mã biên lai và in phiếu thu chi tiết. | **Must-have** |
| **Tra cứu & Báo cáo Thống kê** | Tra cứu đa tiêu chí tình trạng nợ phí; tìm kiếm lịch sử giao dịch theo thời gian; báo cáo tổng kết doanh thu phí định kỳ và kết quả ủng hộ các quỹ tự nguyện. | **Must-have** |
| **Quản trị Hệ thống & Bảo mật** | Đăng nhập tài khoản BQT; đổi mật khẩu và cập nhật thông tin cá nhân; phân quyền theo vai trò (RBAC); ghi nhật ký kiểm toán (Audit Log). | **Must-have** |
| **Dịch vụ Mở rộng (v2.0)** | Quản lý phương tiện và tính phí gửi xe máy ($70.000\text{ đ}$), ô tô ($1.200.000\text{ đ}$); thu hộ tiền điện, nước, internet. | **Won't-have (v2.0)** |

---

## 4. Tóm tắt Yêu cầu Phi chức năng (Non-Functional Requirements)
- **Bảo mật:** Mật khẩu được mã hóa an toàn một chiều (BCrypt); áp dụng cơ chế xác thực và phân quyền RBAC; tự động khóa tài khoản sau 5 lần nhập sai.
- **Hiệu năng (NFR-01):** Tiêu chí đo theo [bảng NFR nguồn](../01_requirements_and_planning/06_client_wishlist_as_is_to_be_nfr.md): tra cứu/chuyển trang tối đa 3 giây, tạo kỳ thu toàn tòa nhà tối đa 30 giây, báo cáo 5 năm tối đa 10 giây. Không dùng mốc 1,5 giây của bản tóm tắt cũ làm tiêu chí khác.
- **Toàn vẹn dữ liệu:** Mọi giao dịch thu phí, gạch nợ đều tuân thủ nguyên tắc ACID Transaction, đảm bảo không lệch sổ quỹ.
- **Sao lưu & Phục hồi (NFR-05):** Theo bảng NFR nguồn: sao lưu hàng ngày, thử phục hồi tối đa 1 giờ, RPO tối đa 24 giờ. Đây là yêu cầu cần kiểm chứng, chưa phải khả năng đã triển khai.
- **Giao diện & Trải nghiệm:** Giao diện tiếng Việt rõ ràng, bố cục trực quan, tối ưu cho thao tác quầy thu ngân.

---

## 5. Tiêu chuẩn Nghiệm thu & Quản lý Truy vết
Toàn bộ chi tiết về kịch bản nghiệm thu **Given - When - Then**, tiêu chí Definition of Done và ma trận truy vết **Traceability Matrix** ánh xạ $100\%$ các yêu cầu từ Client's Wish List đến Acceptance Criteria được trình bày chi tiết tại:
- [Tài liệu Chi tiết US & Acceptance Criteria (US-001 -> US-011)](../01_requirements_and_planning/05_chi_tiet_us_va_acceptance_criteria.md)
- [Tài liệu Ma trận Truy vết & Quản lý Rủi ro](../01_requirements_and_planning/07_traceability_matrix_va_quan_ly_rui_ro.md)
- [Kế hoạch Dự án & Biểu đồ Gantt (Chuẩn SoICT)](../01_requirements_and_planning/09_ke_hoach_du_an_chi_tiet.md)
- [Kế hoạch Quản lý Rủi ro Toàn diện (Chuẩn SoICT)](../01_requirements_and_planning/10_ke_hoach_quan_ly_rui_ro_toan_dien.md)
