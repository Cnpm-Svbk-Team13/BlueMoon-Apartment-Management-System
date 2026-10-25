# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## Hệ thống Quản lý Thu phí và Dân cư Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Giới thiệu tổng quan
Tài liệu Đặc tả Yêu cầu Phần mềm (SRS) này mô tả toàn diện các yêu cầu chức năng, yêu cầu phi chức năng, các tác nhân hệ thống và các điều kiện nghiệm thu cho dự án **BlueMoon BFMS**.

Bộ tài liệu yêu cầu chi tiết của dự án được biên dịch từ đề bài BTL môn Công nghệ phần mềm (`CNPM_20261_YcBTL`) và lưu trữ tại thư mục [`docs/requirements/`](./requirements/README.md):
- [01. Đề bài và Giới thiệu bài toán](./requirements/01_de_bai_tong_quan.md)
- [02. Kế hoạch Khai phá Yêu cầu (SMART & Elicitation Plan)](./requirements/02_ke_hoach_khai_pha_yeu_cau.md)
- [03. Client's Wish List và Danh sách Yêu cầu thô](./requirements/03_client_wishlist_va_raw_findings.md)
- [04. Kịch bản Hiện trạng (As-Is) và Viễn cảnh Tương lai (To-Be)](./requirements/04_kich_ban_hien_trang_va_tuong_lai.md)
- [05. Bộ Scrum Artefacts (Epics, Features, User Stories, Acceptance Criteria)](./requirements/05_scrum_artefacts.md)
- [06. Product Backlog & Ma trận Truy vết Yêu cầu](./requirements/06_product_backlog_traceability.md)

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
- **Hiệu năng:** Thời gian phản hồi cho các truy vấn tra cứu căn hộ và tính toán hóa đơn $\le 1.5$ giây trong điều kiện tải bình thường.
- **Toàn vẹn dữ liệu:** Mọi giao dịch thu phí, gạch nợ đều tuân thủ nguyên tắc ACID Transaction, đảm bảo không lệch sổ quỹ.
- **Sao lưu & Phục hồi:** Hỗ trợ sao lưu CSDL định kỳ, thời gian khôi phục sự cố $< 30$ phút.
- **Giao diện & Trải nghiệm:** Giao diện tiếng Việt rõ ràng, bố cục trực quan, tối ưu cho thao tác quầy thu ngân.

---

## 5. Tiêu chuẩn Nghiệm thu & Quản lý Truy vết
Toàn bộ chi tiết về kịch bản nghiệm thu **Given - When - Then** và ma trận truy vết **Traceability Matrix** ánh xạ $100\%$ các yêu cầu từ Client's Wish List đến Acceptance Criteria được trình bày chi tiết tại:
- [Tài liệu Scrum Artefacts & AC](./requirements/05_scrum_artefacts.md)
- [Tài liệu Product Backlog & Traceability Matrix](./requirements/06_product_backlog_traceability.md)
