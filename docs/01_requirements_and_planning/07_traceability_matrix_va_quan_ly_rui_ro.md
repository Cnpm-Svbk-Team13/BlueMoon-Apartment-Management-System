# MA TRẬN TRUY VẾT YÊU CẦU, GIẢI QUYẾT XUNG ĐỘT & QUẢN LÝ RỦI RO
## Dự án: Phần mềm Quản lý Chung cư BlueMoon (BMS)
### Tài liệu nguồn: Tab 7 ("Quản lý rủi ro") - Google Docs & File `CNPM_Team13_TraceabilityMatrix.pdf` (Team 13)

---

## PHẦN 1: MA TRẬN TRUY VẾT YÊU CẦU (TRACEABILITY MATRIX - TEAM 13)

Bảng ma trận truy xuất nguồn gốc yêu cầu của Team 13 ánh xạ từ các yêu cầu thô (**RAW ID**) sang **Epic**, **Feature**, **User Story (US)** và **Acceptance Criteria (AC)**:

| RAW ID | Epic | Feature | User Story | Acceptance Criteria (AC) |
|---|:---:|---|:---:|---|
| **RAW-001, RAW-002, RAW-009, RAW-010** | **E1** | **1.** Thiết lập dữ liệu chuẩn xác về căn hộ và chủ hộ để làm căn cứ tính phí dịch vụ, quản lý tài chính và làm cơ sở gắn kết dữ liệu nhân khẩu thực tế.<br>**2.** Tra cứu nhanh dân cư và kết xuất báo cáo nhân khẩu học nộp cơ quan công an. | **US-001, US-004** | • AC US-001 (1-4)<br>• AC US-004 (1-4) |
| **RAW-003, RAW-004, RAW-005, RAW-006, RAW-007, RAW-008** | **E6** | **1.** Nắm bắt chính xác ai đang ở trong căn hộ nào.<br>**2.** Ngăn chặn tuyệt đối tình trạng một CCCD đăng ký ở nhiều nơi.<br>**3.** Tự động hóa việc lọc độ tuổi và kiểm soát chặt chẽ thời hạn lưu trú (thường trú, tạm trú, tạm vắng chuẩn Luật Cư trú 2020). | **US-002, US-003** | • AC US-002 (1-4)<br>• AC US-003 (1-3) |
| **RAW-011, RAW-012, RAW-013, RAW-014, RAW-015, RAW-016, RAW-020** | **E2** | **1.** BQT: Thiết lập & quản lý đơn giá các loại phí bắt buộc.<br>**2.** Kế toán: Tự động hóa công đoạn tính phí hàng tháng cho 24 tầng căn hộ & Penthouse.<br>**3.** Thủ quỹ: Ghi nhận thu tiền mặt tại văn phòng BQL, gạch nợ và in biên lai. | **US-005, US-006, US-007** | • AC US-005 (1-3)<br>• AC US-006 (1-3)<br>• AC US-007 (1-3) |
| **RAW-017, RAW-018, RAW-019** | **E3** | **1.** Theo dõi dòng tiền, lịch sử thanh toán từng căn hộ và báo cáo danh sách nợ đọng. | **US-011** | • AC US-011 (1-3) |
| **RAW-021, RAW-022, RAW-023, RAW-024, RAW-025, RAW-026, RAW-027, RAW-028** | **E4** | **1.** Khởi tạo và quản lý các đợt vận động ủng hộ độc lập, không ảnh hưởng đến phí nhà ở.<br>**2.** Tiếp nhận tiền đóng góp tùy tâm, tách bạch 100% khỏi công nợ bắt buộc, phát hành phiếu xác nhận riêng (NĐ 93/2021).<br>**3.** Xuất danh sách tổng kết và hỗ trợ Public Mode ẩn CCCD/SĐT công khai tại sảnh (NĐ 13/2023). | **US-008, US-009, US-010** | • AC US-008 (1-4)<br>• AC US-009 (1-5)<br>• AC US-010 (1-4) |
| **RAW-029, RAW-030, RAW-031, RAW-032** | **E5** | **1.** Tích hợp quản lý phương tiện và tự động hóa tính phí gửi xe theo căn hộ.<br>**2.** Hỗ trợ thu hộ tiện ích giúp cư dân không phải đóng tiền rải rác nhiều nơi.<br>**3.** Tự động hóa gạch nợ 100% qua mã VietQR động.<br>**4.** Cung cấp cổng tra cứu trực tuyến nâng cao trải nghiệm cư dân và minh bạch hóa thu chi. | *(Roadmap v2.0)* | *Phạm vi phát triển phiên bản mở rộng v2.0* |

---

## PHẦN 2: BIÊN BẢN GIẢI QUYẾT XUNG ĐỘT NGHIỆP VỤ (CONFLICT RESOLUTION NOTE)

### 1. Vấn đề mâu thuẫn cốt lõi
- **Bên liên quan tham gia:** Ban Quản trị chung cư BlueMoon vs Toàn thể Cư dân (do Tổ dân phố đại diện).
- **Nguyên nhân mâu thuẫn:**
  - BQT và Kế toán muốn đưa toàn bộ khoản vận động đóng góp từ thiện / quỹ xã hội vào chung một phiếu thông báo thu tiền dịch vụ hàng tháng và cộng dồn vào tổng tiền cần nộp để tăng tỷ lệ thu và giảm công sức đi thu tiền mặt tận phòng.
  - Cư dân phản ứng gay gắt vì cho rằng đây là hình thức "thu ép buộc", mập mờ giữa trách nhiệm tài chính bắt buộc theo hợp đồng mua bán căn hộ và nghĩa vụ tự nguyện tùy tâm. Nhiều hộ từ chối đóng và gây khó khăn cho việc gạch nợ của kế toán.

### 2. Phương án dung hòa thống nhất (Chốt giải pháp kỹ thuật)
Nhóm phân tích nghiệp vụ Team 13 thống nhất giải pháp kiến trúc phần mềm như sau:
1. **Thiết kế mẫu Thông báo thu tiền chia thành 2 phần độc lập tuyệt đối:**
   - **Mục A (Khoản phí bắt buộc):** Tính chính xác theo công thức $\text{Diện tích } (m^2) \times (\text{Đơn giá Dịch vụ} + \text{Đơn giá Quản lý})$. Cột công nợ lũy kế và nợ đọng chỉ tính toán trên Mục A.
   - **Mục B (Khoản đóng góp tự nguyện):** Hiển thị tên đợt vận động ủng hộ. Ô số tiền để trống cho cư dân tự điền hoặc hiển thị số tiền cư dân đã đăng ký ủng hộ tùy tâm.
2. **Quy tắc gạch nợ và kế toán:**
   - Trường hợp hộ gia đình không nộp tiền Mục B thì **tuyệt đối không bị ghi nhận nợ xấu** hay tính phạt trả chậm.
   - Khi cư dân đóng tiền ủng hộ quỹ, hệ thống phát hành **Phiếu xác nhận ủng hộ tự nguyện độc lập** có in lời cảm ơn của BQT và Tổ dân phố.

---

## PHẦN 3: NHẬT KÝ QUẢN LÝ RỦI RO (RISK LOG)

| Mã Rủi ro | Mô tả Rủi ro phát sinh | Đánh giá Tác động | Biện pháp Giảm thiểu / Giải pháp Kỹ thuật | Trách nhiệm |
|:---:|---|:---:|---|:---:|
| **RSK-01** | **Rủi ro sai lệch dữ liệu từ file Excel cũ:** File Excel thu phí từ 2023-2026 bị phân mảnh qua nhiều phiên bản, lệch format số diện tích $m^2$, thiếu số phòng hoặc sai chính tả họ tên. | **Cao** | Xây dựng module Parser có tính năng **Standardize & Validate dữ liệu đầu vào**. Hệ thống sẽ kiểm tra cú pháp, báo đỏ các dòng bị thiếu diện tích hoặc trùng lặp mã phòng trước khi Import vào CSDL. | TV 5 (QA) & TV 4 (Data) |
| **RSK-02** | **Rủi ro mất dữ liệu khi chuyển đổi:** Trong quá trình đưa dữ liệu vào phần mềm mới, dữ liệu lịch sử nộp tiền bị thất thoát hoặc ghi đè mất thông tin chủ hộ cũ. | **Cao** | Thiết kế bảng `AuditLog` và bảng `Apartment_Owner_History` trên CSDL. Cấm xóa cứng (Hard Delete) dữ liệu chủ hộ và giao dịch cũ; toàn bộ thay đổi đều được ghi vết vĩnh viễn. | TV 4 (Data Engineer) |
| **RSK-03** | **Rủi ro trùng lặp số CCCD giữa các căn hộ:** Người thuê nhà chuyển phòng hoặc khai báo nhầm dẫn đến 1 số CCCD xuất hiện ở nhiều hộ khác nhau. | **Trung bình** | Lập trình thuật toán **Global Validation** quét trường `identity_card_number` trên toàn bộ bảng nhân khẩu. Hiển thị popup cảnh báo đỏ và chặn lưu nếu phát hiện trùng lặp. | TV 2 (System Analysis) |
| **RSK-04** | **Rủi ro lệch tiền khi gạch nợ giao dịch:** Lỗi mạng hoặc sự cố hệ thống xảy ra giữa chừng khi thủ quỹ bấm gạch nợ khiến hóa đơn đổi trạng thái nhưng quỹ tiền mặt chưa ghi nhận. | **Rất cao** | Bắt buộc toàn bộ logic thanh toán và gạch nợ phải được thực thi trong một **Database Transaction (ACID)**. Nếu có bất kỳ lỗi nào xảy ra, hệ thống tự động `Rollback` toàn bộ về trạng thái trước đó. | TV 5 (QA Specialist) |
