# TÀI LIỆU TUYÊN NGÔN DỰ ÁN (PROJECT CHARTER) - TEAM 13
## Dự án: Phần mềm Quản lý Chung cư BlueMoon (BlueMoon Management System - BMS)
### Tài liệu nguồn: Tab 5 ("Tuyên ngôn dự án") - Google Docs & File `CNPM_Team13_TuyenNgon.pdf`

---

## THÔNG TIN CHUNG
- **Tên dự án:** Phần mềm Quản lý Chung cư BlueMoon (BlueMoon Management System - BMS)
- **Mã dự án:** `PRJ-BLUEMOON-2026`
- **Đơn vị tài trợ:** Ban Quản trị Chung cư BlueMoon
- **Người lập:** Nhóm Phân tích Yêu cầu BA (Team 13)
- **Ngày lập:** 08/10/2026

---

## I. TỔNG QUAN BÀI TOÁN & PHẠM VI (SCOPE STATEMENT)

### 1.1. Bối cảnh dự án & Lý do đầu tư
Chung cư BlueMoon đi vào hoạt động từ năm 2023 với quy mô 30 tầng (1 tầng kiot, 4 tầng đế kinh doanh, 24 tầng nhà ở, 1 tầng penthouse). Toàn bộ công tác quản lý nhân khẩu và thu phí hiện tại được Ban quản trị (BQT) thực hiện thủ công thông qua sổ sách và phần mềm Excel.

Quy trình vận hành hiện tại đang gặp phải các vấn đề:
- Mất nhiều thời gian đối soát công nợ, dễ xảy ra sai sót khi tính toán các khoản phí phụ thuộc vào biến động diện tích hoặc nhân khẩu.
- Rủi ro mất mát dữ liệu do lưu trữ phân tán.
- Thiếu công cụ tương tác trực tuyến, gây ra rào cản thông tin và giảm tính minh bạch trong việc thu chi với cư dân.

### 1.2. Tuyên bố Phạm vi Dự án
- **Phạm vi thực hiện trong phiên bản v1.0 (In-Scope):**
  - Quản lý hộ gia đình & thông tin nhân khẩu (tạm trú, tạm vắng).
  - Quản lý danh mục các khoản phí bắt buộc và các quỹ đóng góp tự nguyện.
  - Quản lý tiến trình thu phí, ghi nhận thanh toán của từng hộ.
  - Tra cứu thông tin và thống kê báo cáo thu chi.
  - Quản lý tài khoản Ban quản trị (Đăng nhập, phân quyền, đổi mật khẩu).
- **Phạm vi hoãn/mở rộng trong phiên bản v2.0 (Out-of-Scope v1.0 / Visionary Roadmap):**
  - Quản lý phí gửi xe (ô tô, xe máy) theo định mức.
  - Quản lý thu hộ chi phí dịch vụ tiện ích (điện, nước, internet).

---

## II. PHÂN TÍCH BÊN LIÊN QUAN (STAKEHOLDER ANALYSIS)

### 2.1. Phân loại Nguồn thông tin (Identify Sources)
- **Nguồn con người:** Ban quản trị (BQT), Thủ quỹ/Thu ngân, Chủ hộ/Cư dân, Tổ trưởng dân phố/Chính quyền địa phương, Các nhà cung cấp dịch vụ.
- **Nguồn phi con người:** File Excel theo dõi sổ sách hiện tại, mẫu biên lai giấy, quy định vận hành chung cư, các văn bản pháp lý về quản lý nhân khẩu.

### 2.2. Ma trận Bên liên quan (Stakeholder Matrix / Register)

| Bên liên quan | Vai trò | Mức độ ảnh hưởng | Mức độ quan tâm | Kỳ vọng cốt lõi | Chiến lược tiếp cận & Kỹ thuật Khai phá |
|---|---|:---:|:---:|---|---|
| **Ban quản trị (BQT)** | Chủ đầu tư / Đơn vị quản lý dự án | **Cao** | **Cao** | Minh bạch thu chi, quản lý toàn bộ nhân khẩu và báo cáo số liệu chính xác. | **Quản lý chặt chẽ (Manage Closely):** Phỏng vấn bán cấu trúc, Workshop & Refinement Sessions. |
| **Thủ quỹ / Thu ngân** | Người vận hành trực tiếp hệ thống | **Trung bình** | **Cao** | Ghi nhận thanh toán nhanh chóng, chính xác, giảm thiểu thao tác thủ công và nhầm lẫn. | **Thỏa mãn nhu cầu:** Quan sát công việc, phân tích tài liệu sổ sách Excel/biên lai, phỏng vấn sâu luồng thao tác để lập tài liệu As-Is Scenario. |
| **Chủ hộ / Cư dân** | Người dùng cuối / Người đóng phí | **Thấp** | **Cao** | Tra cứu khoản phí rõ ràng, minh bạch thông tin đóng góp, quy trình khai báo đơn giản, bảo mật thông tin. | **Cung cấp thông tin:** Bảng hỏi / Khảo sát ngắn, phỏng vấn đại diện cư dân để xây dựng Client's Wish List. |
| **Tổ trưởng dân phố / Chính quyền địa phương** | Cơ quan giám sát quản lý hành chính | **Cao** | **Trung bình** | Theo dõi biến động nhân khẩu (tạm trú, tạm vắng) đầy đủ, tuân thủ quy định pháp luật. | **Tham vấn định kỳ:** Tra cứu quy định & văn bản pháp lý, Tham vấn yêu cầu báo cáo, thiết lập Acceptance Criteria (AC) & NFR pháp lý. |
| **Đội ngũ phát triển (Dev Team)** | Xây dựng & triển khai phần mềm | **Trung bình** | **Cao** | Hiểu rõ yêu cầu nghiệp vụ, tiêu chuẩn Acceptance Criteria rõ ràng để phát triển hệ thống v1.0 đúng hạn. | **Refinement Sessions:** Ánh xạ từ Client's Wish List $\rightarrow$ Epic $\rightarrow$ Feature $\rightarrow$ User Story (INVEST) $\rightarrow$ AC (GWT). |

### 2.3. Ma trận Quyền lực - Mức độ quan tâm (Power / Interest Grid)

```text
                           MỨC ĐỘ ẢNH HƯỞNG (POWER)
                 CAO                                    THẤP
       +------------------------------------+------------------------------------+
       | 1. QUẢN LÝ CHẶT CHẼ                | 2. GIỮ KẾT NỐI SÂU                 |
       |    (Manage Closely)                |    (Keep Informed)                 |
CAO    |                                    |                                    |
       | - Ban quản trị (BQT)               | - Thủ quỹ / Thu ngân               |
MỨC ĐỘ |                                    | - Chủ hộ & Cư dân                  |
QUAN   +------------------------------------+------------------------------------+
TÂM    | 3. GIỮ SỰ HÀI LÒNG                 | 4. GIÁM SÁT TỐI THIỂU              |
(INT)  |    (Keep Satisfied)                |    (Monitor)                       |
THẤP   |                                    |                                    |
       | - Tổ trưởng dân phố /              | - Các nhà cung cấp dịch vụ         |
       |   Chính quyền phường               |   (Điện/Nước)                      |
       +------------------------------------+------------------------------------+
```

- **Góc 1 (Power Cao - Interest Cao):** Ban quản trị. Đây là nhóm quan trọng nhất, cần gặp gỡ thường xuyên để chốt yêu cầu và duyệt Product Backlog.
- **Góc 2 (Power Thấp - Interest Cao):** Thủ quỹ & Cư dân. Nhóm này trực tiếp vận hành/thụ hưởng nên thông tin từ họ quyết định tính khả thi (Usability) của phần mềm.
- **Góc 3 (Power Cao - Interest Thấp):** Chính quyền địa phương. Không tương tác hằng ngày nhưng có quyền dừng dự án nếu vi phạm quy định quản lý nhân khẩu/bảo mật dữ liệu.
- **Góc 4 (Power Thấp - Interest Thấp):** Nhà cung cấp dịch vụ bên ngoài (Điện, Nước, Internet). Chỉ quan tâm ở phiên bản v2.0 khi có tính năng thu hộ.

---

## III. MỤC TIÊU VÀ CÁC ARTEFACT ĐẦU RA

### 3.1. Bảng Mục tiêu SMART (OBJ-01 $\rightarrow$ OBJ-08)

| Mã | Mục tiêu cụ thể (SMART) | Chỉ số đo lường (KPI / Minh chứng) | Nguồn đo | Hạn chót | Artefact đầu ra tương ứng |
|:---:|---|---|---|:---:|---|
| **OBJ-01** | Thu thập $\ge 80\%$ yêu cầu chức năng cho BlueMoon v1.0 (quản lý cư dân, danh mục khoản thu, thu phí, tra cứu/thống kê, đăng nhập/đổi mật khẩu) | $\ge 30$ yêu cầu thô được ghi nhận | Biên bản phỏng vấn, khảo sát, phân tích Excel | **D+7** | **Client's Wish List** |
| **OBJ-02** | Xác định $\ge 5$ yêu cầu phi chức năng (bảo mật, phân quyền, sao lưu, hiệu năng, audit log...) | $\ge 5$ NFR trong danh sách | Checklist NFR | **D+7** | **Checklist NFR** |
| **OBJ-03** | Ghi nhận và mô tả hiện trạng thu phí, quản lý cư dân bằng Excel/biên lai | $\ge 1$ tài liệu As-Is Scenario hoàn chỉnh | Biên bản quan sát, phân tích tài liệu | **D+6** | **As-Is Scenario** |
| **OBJ-04** | Mô tả viễn cảnh kỳ vọng (Visionary Scenario) bao gồm gợi mở v2.0 (phí gửi xe, thu hộ điện/nước/internet) | $\ge 1$ tài liệu Visionary Scenario hoàn chỉnh | Workshop với Ban quản trị, log yêu cầu | **D+6** | **Visionary Scenario** |
| **OBJ-05** | Chuẩn hóa kết quả thô thành $\ge 5$ Epic và $\ge 10$ Feature | Danh sách Epic/Feature được xác nhận | Artefact chuẩn hóa | **D+8** | **Epic & Feature List** |
| **OBJ-06** | Xây dựng $\ge 10$ User Stories theo chuẩn INVEST, có Acceptance Criteria (GWT) | $10+$ User Story có Acceptance Criteria | Backlog draft | **D+8** | **User Story + Acceptance Criteria** |
| **OBJ-07** | Hoàn thiện Product Backlog ưu tiên theo mô hình MoSCoW | Backlog có phân loại Must/Should/Could | Product Backlog | **D+8** | **Product Backlog** |
| **OBJ-08** | Lập bảng ánh xạ Client's Wish List $\rightarrow$ Epic $\rightarrow$ Feature $\rightarrow$ User Story $\rightarrow$ AC | $\ge 80\%$ yêu cầu có truy vết (trace) | Traceability matrix | **D+8** | **Traceability Matrix** |

---

## IV. BỘ KỊCH BẢN PHỎNG VẤN CHI TIẾT

### 4.1. Kịch bản phỏng vấn Ban quản trị (BQT)
- **Câu hỏi mở (Open-ended):**
  1. Anh/Chị có thể chia sẻ chi tiết về quy trình quản lý dân cư và thu chi hiện tại của BQT chung cư BlueMoon không?
  2. Những khó khăn lớn nhất mà BQT gặp phải khi quản lý bằng file Excel và sổ sách thủ công hiện nay là gì?
  3. BQT kỳ vọng phần mềm mới sẽ giải quyết triệt để những vấn đề cốt lõi nào trong phiên bản v1.0?
  4. BQT mong muốn phân quyền tài khoản như thế nào giữa các thành viên BQT và nhân viên thu ngân/thủ quỹ?
- **Câu hỏi đóng (Close-ended):**
  1. BQT có nhu cầu theo dõi danh mục quỹ đóng góp tự nguyện (như Quỹ người nghèo, Khuyến học) riêng biệt với các khoản phí dịch vụ bắt buộc không? *(Có / Không)*
  2. Tất cả các tài khoản BQT đều có quyền xem báo cáo thu chi, hay chỉ có Trưởng BQT mới có quyền này? *(Xem tất cả / Phân quyền từng cấp)*
  3. Ngoài việc xem trên màn hình, BQT có bắt buộc phải xuất báo cáo nợ phí ra file Excel/PDF để in ấn không? *(Có / Không)*
  4. Phạm vi phiên bản v1.0 sẽ tạm thời chưa làm phần Quản lý gửi xe và Thu hộ điện/nước đúng không? *(Đúng / Chưa đúng)*
- **Kết thúc:** *"Còn điều gì quan trọng về quy trình quản lý hay quy định riêng của chung cư mà chúng em chưa hỏi nhưng BQT thấy cần lưu ý không?"*

### 4.2. Kịch bản phỏng vấn Thủ quỹ / Thu ngân
- **Câu hỏi mở:**
  1. Chị hãy mô tả từng bước thao tác từ lúc cư dân đến báo số căn hộ nộp tiền cho đến khi chị trao biên lai/phiếu thu cho họ?
  2. Khi tra cứu thông tin căn hộ trong file Excel 500 hộ dân hiện tại, chị thường gặp những sự cố hay nhầm lẫn nào?
  3. Cuối tháng, chị mất bao lâu để tổng hợp danh sách những hộ chưa nộp phí và đối soát số tiền mặt đã thu?
  4. Chị mong muốn màn hình lập phiếu thu hiển thị những thông tin gì để thao tác nhanh và thuận tiện nhất?
- **Câu hỏi đóng:**
  1. Phí dịch vụ chung cư hiện đang được thu theo diện tích $m^2$ căn hộ đúng không? *(Đúng / Sai)*
  2. Hệ thống có cần tự động tính tổng tiền khi chị chọn mã căn hộ (ví dụ: 1204) không? *(Có / Không)*
  3. Chị có cần in phiếu thu ra giấy ngay sau khi bấm xác nhận thanh toán trên phần mềm không? *(Có / Không)*
  4. Chị có bao giờ phải sửa hoặc hủy một phiếu thu đã lập do gõ sai số tiền không? *(Có / Thỉnh thoảng / Không)*
- **Kết thúc:** *"Chị có góp ý thêm gì về thao tác bàn phím/chuột hay cách bố trí giao diện để công việc thu tiền hằng ngày bớt áp lực hơn không?"*

### 4.3. Kịch bản phỏng vấn Đại diện Cư dân / Chủ hộ
- **Câu hỏi mở:**
  1. Anh/Chị đánh giá thế nào về tính minh bạch trong việc thu nộp các khoản phí và đóng góp tự nguyện tại chung cư hiện nay?
  2. Anh/Chị mong muốn nhận thông báo nộp phí và xem lại lịch sử đóng góp của gia đình mình bằng hình thức nào?
- **Câu hỏi đóng:**
  1. Anh/Chị có muốn công khai danh sách các hộ gia đình đã đóng góp các quỹ tự nguyện (như Khuyến học, Vì người nghèo) không? *(Có / Không)*
  2. Anh/Chị có nhu cầu tự tra cứu thông tin diện tích và nhân khẩu đăng ký của căn hộ mình trên phần mềm không? *(Có / Không)*
- **Kết thúc:** *"Anh/Chị có mong muốn hay đề xuất gì thêm đối với Ban quản trị về việc quản lý thu phí và nhân khẩu trong chung cư không?"*
