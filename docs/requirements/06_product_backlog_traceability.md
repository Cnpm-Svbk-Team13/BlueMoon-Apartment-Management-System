# PRODUCT BACKLOG & MA TRẬN TRUY VẾT YÊU CẦU (TRACEABILITY MATRIX)
## Dự án: Hệ thống Quản lý Thu phí Chung cư BlueMoon

---

## 1. Product Backlog (Sắp xếp theo MoSCoW & Kế hoạch Sprint)

Product Backlog được tổ chức theo tiêu chuẩn Scrum, phân loại ưu tiên theo khung **MoSCoW**:
- **Must-have (M):** Bắt buộc phải có trong phiên bản v1.0 để hệ thống có thể vận hành tối thiểu (MVP).
- **Should-have (S):** Quan trọng và nên có trong phiên bản v1.0 để gia tăng tính tiện ích, an toàn.
- **Could-have (C):** Tiện ích bổ sung nếu còn thời gian (báo cáo biểu đồ nâng cao, xuất PDF màu...).
- **Won't-have (W):** Tạm thời chưa triển khai trong v1.0, để dành cho phiên bản mở rộng v2.0 (phí gửi xe, thu hộ điện/nước).

| STT | ID (Epic / US) | Tên / Mô tả tóm tắt | Phân loại (MoSCoW) | Sprint dự kiến | Story Points | Trạng thái |
|:---:|:---:|---|:---:|:---:|:---:|:---:|
| 1 | **EPIC-05 / US-16** | Đăng nhập tài khoản BQT, mã hóa mật khẩu, phiên làm việc an toàn | **Must-have** | Sprint 1 | 3 | Ready for Dev |
| 2 | **EPIC-05 / US-17** | Đổi mật khẩu cá nhân và cập nhật thông tin tài khoản | **Must-have** | Sprint 1 | 2 | Ready for Dev |
| 3 | **EPIC-05 / US-18** | Phân quyền vai trò người dùng (Admin, Kế toán, Thủ quỹ) & Audit Log | **Should-have** | Sprint 1 | 5 | Ready for Dev |
| 4 | **EPIC-01 / US-01** | Quản lý căn hộ (ki-ốt, 24 tầng căn hộ, penthouse, diện tích $m^2$) | **Must-have** | Sprint 2 | 5 | Ready for Dev |
| 5 | **EPIC-01 / US-02** | Quản lý sổ hộ khẩu căn hộ, thông tin chủ hộ, số liên lạc | **Must-have** | Sprint 2 | 5 | Ready for Dev |
| 6 | **EPIC-01 / US-03** | Quản lý nhân khẩu thành viên trong hộ, số CCCD, ngày sinh, quan hệ | **Must-have** | Sprint 2 | 5 | Ready for Dev |
| 7 | **EPIC-01 / US-04** | Quản lý biến động nhân khẩu (thêm người, tách hộ, chuyển đi) | **Must-have** | Sprint 2 | 3 | Ready for Dev |
| 8 | **EPIC-01 / US-05** | Khai báo và quản lý tạm trú, tạm vắng (thời hạn, lý do) | **Must-have** | Sprint 2 | 3 | Ready for Dev |
| 9 | **EPIC-01 / US-06** | Xuất báo cáo danh sách nhân khẩu nộp Công an phường / Tổ dân phố | **Should-have** | Sprint 2 | 3 | Ready for Dev |
| 10 | **EPIC-02 / US-07** | Cấu hình biểu phí dịch vụ ($2.500 - 16.500\text{ đ}$) & quản lý ($\ge 7.000\text{ đ}$) | **Must-have** | Sprint 3 | 3 | Ready for Dev |
| 11 | **EPIC-02 / US-08** | Khởi tạo và quản lý các đợt phát động quỹ tự nguyện (vì người nghèo, biển đảo...) | **Must-have** | Sprint 3 | 3 | Ready for Dev |
| 12 | **EPIC-03 / US-09** | Tự động phát sinh bảng kê thu phí định kỳ theo diện tích từng phòng | **Must-have** | Sprint 3 | 5 | Ready for Dev |
| 13 | **EPIC-03 / US-10** | Quầy thu ngân: Tra cứu phòng, thu tiền định kỳ & đóng góp tự nguyện, gạch nợ | **Must-have** | Sprint 3 | 8 | Ready for Dev |
| 14 | **EPIC-03 / US-11** | Sinh mã biên lai và in/xuất biên lai thu tiền chi tiết cho cư dân | **Must-have** | Sprint 3 | 5 | Ready for Dev |
| 15 | **EPIC-04 / US-12** | Tra cứu nhanh tình trạng công nợ theo từng căn hộ | **Must-have** | Sprint 4 | 3 | Ready for Dev |
| 16 | **EPIC-04 / US-13** | Tra cứu và lọc lịch sử các giao dịch thu tiền theo khoảng thời gian | **Should-have** | Sprint 4 | 3 | Ready for Dev |
| 17 | **EPIC-04 / US-14** | Báo cáo thống kê tổng hợp số tiền thu phí định kỳ và tỷ lệ hoàn thành | **Must-have** | Sprint 4 | 5 | Ready for Dev |
| 18 | **EPIC-04 / US-15** | Thống kê danh sách các hộ đóng góp và tổng tiền các quỹ tự nguyện | **Must-have** | Sprint 4 | 3 | Ready for Dev |
| 19 | **EPIC-04 / US-Opt**| Biểu đồ trực quan so sánh tiến độ thu nộp giữa các tầng | **Could-have** | Sprint 4 | 3 | Backlog |
| 20 | **EPIC-06 / US-19** | Quản lý phương tiện và tính phí gửi xe (xe máy 70k, ô tô 1.200k) | **Won't-have** | Roadmap v2.0 | 8 | Future |
| 21 | **EPIC-06 / US-20** | Quản lý thu hộ tiền điện, nước, cước viễn thông/internet | **Won't-have** | Roadmap v2.0 | 8 | Future |

---

## 2. Ma trận Truy vết Yêu cầu (Traceability Matrix)

Bảng ma trận này ánh xạ thông suốt từ **Nhu cầu thô ban đầu (RAW ID)** $\rightarrow$ **Epic** $\rightarrow$ **Feature** $\rightarrow$ **User Story** $\rightarrow$ **Acceptance Criteria (AC)**. 
Tỷ lệ truy vết đạt **100%** (32/32 yêu cầu thô được ánh xạ rõ ràng, vượt xa yêu cầu tối thiểu $\ge 80\%$ tại **OBJ-08**):

| RAW ID | Nguồn gốc | Epic ánh xạ | Feature ánh xạ | User Story | Tiêu chí nghiệm thu (AC) | Ghi chú kiểm thử / Triển khai |
|:---:|---|:---:|:---:|:---:|:---:|---|
| **RAW-001** | BQT | EPIC-01 | FEAT-01 | US-01 | US-01.Scenario 1, 2 | Kiểm tra lưu đủ 30 tầng, ki-ốt, penthouse. |
| **RAW-002** | Kế toán | EPIC-01 | FEAT-01 | US-01 | US-01.Scenario 1, 3 | Kiểm tra validate diện tích $m^2 > 0$. |
| **RAW-003** | BQT | EPIC-01 | FEAT-02 | US-02 | US-02.Scenario 1 | Sổ hộ khẩu gắn chặt với mã căn hộ. |
| **RAW-004** | BQT | EPIC-01 | FEAT-02 | US-03 | US-03.Scenario 2 | Danh sách nhân khẩu thành viên trong hộ. |
| **RAW-005** | BQT | EPIC-01 | FEAT-03 | US-04 | US-04.Scenario 1 | Lịch sử thêm, bớt, chuyển nhân khẩu. |
| **RAW-006** | TDP/CA | EPIC-01 | FEAT-03 | US-05 | US-05.Scenario 1, 2 | Kiểm tra trường ngày bắt đầu, kết thúc, lý do. |
| **RAW-007** | BQT | EPIC-01 | FEAT-04 | US-06 | US-06.Scenario 1 | Kết xuất PDF/Excel nộp Công an khu vực. |
| **RAW-008** | Kế toán | EPIC-02 | FEAT-05 | US-07 | US-07.Scenario 1 | Phân loại rõ khoản phí bắt buộc vs tự nguyện. |
| **RAW-009** | Kế toán | EPIC-02 | FEAT-05 | US-07 | US-07.Scenario 1, 2 | Kiểm tra dải định mức $2.500 - 16.500\text{ đ}/m^2$. |
| **RAW-010** | Kế toán | EPIC-02 | FEAT-05 | US-07 | US-07.Scenario 3 | Kiểm tra định mức tối thiểu $\ge 7.000\text{ đ}/m^2$. |
| **RAW-011** | TDP | EPIC-02 | FEAT-06 | US-08 | US-08.Scenario 1 | Tạo chiến dịch quỹ tự nguyện có thời hạn. |
| **RAW-012** | Thủ quỹ | EPIC-03 | FEAT-08 | US-10 | US-10.Scenario 1 | Thu tự nguyện cho phép nhập số tiền tùy ý. |
| **RAW-013** | Kế toán | EPIC-03 | FEAT-07 | US-09 | US-09.Scenario 1 | Công thức tự động: $(\text{Phí DV} + \text{Phí QL}) \times \text{Diện tích}$. |
| **RAW-014** | Thủ quỹ | EPIC-03 | FEAT-08 | US-10 | US-10.Scenario 1 | Màn hình quầy tra cứu nhanh theo số phòng. |
| **RAW-015** | Thủ quỹ | EPIC-03 | FEAT-08 | US-10 | US-10.Scenario 1 | Tích chọn đóng từng khoản hoặc đóng tất cả. |
| **RAW-016** | Cư dân | EPIC-03 | FEAT-09 | US-11 | US-11.Scenario 1 | Biên lai có đủ chi tiết khoản thu, người nộp. |
| **RAW-017** | Thủ quỹ | EPIC-03 | FEAT-08 | US-10 | US-10.Scenario 1 | Ghi nhận người nộp thực tế và timestamp thu. |
| **RAW-018** | BQT | EPIC-04 | FEAT-10 | US-12 | US-12.Scenario 1 | Kiểm tra trạng thái nợ của căn hộ. |
| **RAW-019** | Kế toán | EPIC-04 | FEAT-10 | US-13 | US-13.Scenario 1 | Lọc giao dịch thu tiền theo khoảng thời gian. |
| **RAW-020** | BQT | EPIC-04 | FEAT-11 | US-14 | US-14.Scenario 1 | Báo cáo doanh thu và tỷ lệ hoàn thành. |
| **RAW-021** | TDP/BQT | EPIC-04 | FEAT-11 | US-15 | US-15.Scenario 2 | Danh sách ủng hộ các quỹ thay thế sổ tay Hình 1-1. |
| **RAW-022** | BQT | EPIC-05 | FEAT-12 | US-16 | US-16.Scenario 1, 2 | Chặn truy cập chưa đăng nhập, mã hóa BCrypt. |
| **RAW-023** | Kế toán | EPIC-05 | FEAT-12 | US-17 | US-17.Scenario 1 | Người dùng tự đổi mật khẩu cá nhân. |
| **RAW-024** | BQT | EPIC-05 | FEAT-13 | US-18 | US-18.Scenario 3 | Kiểm tra phân quyền truy cập URL theo Role. |
| **RAW-025** | Kế toán | EPIC-05 | FEAT-13 | US-18 | NFR-005.Scenario 1 | Ghi log thao tác sửa hoặc xóa phiếu thu. |
| **RAW-026** | Cư dân | Cổng Cư dân | Resident Portal | Backlog v2.0 | N/A | Dành cho giai đoạn mở rộng v2.0. |
| **RAW-027** | BQT | EPIC-06 | FEAT-14 | US-19 | N/A (v2.0) | Thu phí xe máy 70k, ô tô 1.200k. |
| **RAW-028** | BQT | EPIC-06 | FEAT-15 | US-20 | N/A (v2.0) | Thu hộ tiền điện, nước, viễn thông. |
| **RAW-029** | Kế toán | Hạ tầng | Backup & Restore | DoD / Tech Story | NFR-007 | Kiểm thử sao lưu CSDL định kỳ. |
| **RAW-030** | Thủ quỹ | Giao diện | UI/UX Tiện dụng | Definition of Done | NFR-008 | Giao diện tiếng Việt chuẩn, rõ ràng, dễ nhìn. |
| **RAW-031** | TDP | EPIC-01 | FEAT-02 | US-02, US-03 | US-03.Scenario 3 | Validate số CCCD đủ 12 chữ số theo quy chuẩn. |
| **RAW-032** | Kế toán | EPIC-01, 04 | FEAT-04, 11 | US-06, US-14 | US-14.Scenario 1 | Xuất file Excel / in biểu mẫu trực tiếp. |
