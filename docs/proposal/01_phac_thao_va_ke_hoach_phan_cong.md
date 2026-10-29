# PHÁC THẢO CÔNG VIỆC VÀ KẾ HOẠCH PHÂN CÔNG
## Dự án: Phần mềm Quản lý Thu phí Chung cư BlueMoon (BMS)
### Tài liệu nguồn: Tab 0 ("Phác thảo công việc") & Tab 1 ("Kế hoạch phân công") - Google Docs `CNPM_Proposal_Team13`

---

## PHẦN 1: CÁC BƯỚC KHAI PHÁ YÊU CẦU VÀ KẾT QUẢ DỰ KIẾN (TAB 0)

### BƯỚC 1: XÁC ĐỊNH BỐI CẢNH & ĐỐI TƯỢNG KHAI PHÁ (PREPARATION)
#### 1. Các công việc cần làm:
- Nhận diện và lập ma trận phân loại Stakeholders (Ban quản trị, Kế toán/Thu ngân, Tổ dân phố, Cư dân 24 tầng, Chủ kiot & khối thương mại tầng 1-4).
- Phân tích bối cảnh tòa nhà BlueMoon (diện tích $450\text{ m}^2$, 30 tầng) và các loại phí: Phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$), Phí quản lý (từ $7.000\text{ đ}/m^2$), Các đợt đóng góp tự nguyện.
- Xác định phạm vi thu thập yêu cầu (In-scope vs Out-of-scope).
- Lập lịch trình và kịch bản khai phá cho từng đối tượng.

#### 2. Kết quả thu được (Deliverables):
- **Bảng ma trận Stakeholders (Stakeholder Matrix):** Xác định rõ ai cần phỏng vấn, ai tham gia workshop, ai làm khảo sát.
- **Tài liệu Tuyên bố Phạm vi (Scope Statement):** Chốt rõ phần mềm tập trung vào thu phí định kỳ, công nợ và báo cáo; không làm gạch nợ tự động qua ngân hàng ở Phase 1.
- **Kế hoạch Khai phá Yêu cầu (Elicitation Schedule):** Lịch làm việc chi tiết 2 tuần thu thập dữ liệu.

---

### BƯỚC 2: THỰC HIỆN THU THẬP & KHAI PHÁ THÔNG TIN (EXECUTION)
#### 1. Các công việc cần làm:
- **Phỏng vấn sâu (In-depth Interview):** Phỏng vấn Trưởng BQT và Kế toán về quy trình tính phí $m^2$, công thức Excel cũ, cách thu tiền mặt/chuyển khoản và quản lý nợ xấu.
- **Tổ chức Workshop (JAD Session):** Thảo luận với BQT và Tổ dân phố về cơ chế thu các quỹ vận động tự nguyện (Ủng hộ bão lụt, Quỹ vì người nghèo...) để tách biệt với phí bắt buộc.
- **Khảo sát qua bảng hỏi (Survey):** Phát Google Forms tới cư dân 24 tầng căn hộ & Penthouse về kênh nhận thông báo thu tiền và nhu cầu tra cứu.
- **Quan sát & Phân tích tài liệu (Observation & Document Analysis):** Quan sát Kế toán viết phiếu thu tiền mặt và phân tích các file Excel thu phí từ 2023-2026.

#### 2. Kết quả thu được (Deliverables):
- **Tập Biên bản phỏng vấn (Interview Transcripts/Notes):** Ghi nhận chi tiết công thức $\text{Tiền phí} = \text{Diện tích} \times \text{Đơn giá}$ và các bất cập của Excel cũ.
- **Tài liệu Thống nhất Quy trình Thu Quỹ Tự nguyện:** Thỏa thuận giữa BQT và Tổ dân phố về cách ghi nhận số tiền ủng hộ tùy tâm.
- **Báo cáo Kết quả Khảo sát Cư dân (Survey Analysis Report):** Số liệu % cư dân muốn nhận thông báo qua Zalo/Email thay vì giấy in.
- **Bộ mẫu file Excel cũ & Phiếu thu hiện tại:** Dữ liệu mẫu để phục vụ chuẩn hóa database.

#### 3. Bộ câu hỏi phỏng vấn mẫu (Interview Questionnaire):

##### A. Bộ câu hỏi phỏng vấn Ban Quản trị (BQT):
- **Tổng quan & Định hướng:**
  - Mục tiêu lớn nhất mà BQT kỳ vọng ở hệ thống quản lý thu phí mới này là gì?
  - Hiện tại BQT đang gặp khó khăn/điểm nghẽn (pain points) lớn nhất nào khi quản lý bằng Excel?
- **Quy định & Đơn giá các loại phí:**
  - Công thức và căn cứ tính Phí dịch vụ (hiện tại từ $2.500 - 16.500\text{ đ}/m^2$) và Phí quản lý (từ $7.000\text{ đ}/m^2$) đối với từng loại mặt bằng (Kiot tầng 1, 4 tầng đế, 24 tầng nhà ở, Penthouse) được quy định cụ thể như thế nào?
  - Đơn giá phí dịch vụ và phí quản lý có cố định không hay thay đổi theo quyết định của Hội nghị chung cư hàng năm?
- **Chính sách thu Quỹ đóng góp tự nguyện:**
  - Quy trình phối hợp với Tổ dân phố để phát động các đợt thu Quỹ từ thiện / Quỹ biển đảo / Quỹ vì người nghèo diễn ra như thế nào?
  - BQT muốn hiển thị khoản đóng góp tự nguyện này chung hay riêng trên Thông báo thu tiền của cư dân để đảm bảo minh bạch?
- **Quản lý Nợ & Giải quyết khiếu nại:**
  - Quy trình xử lý đối với các trường hợp nợ phí kéo dài (trên 3 tháng) hiện tại đang được thực hiện như thế nào?
  - BQT có cần tính phí phạt trả chậm đối với các hộ chậm đóng tiền không?

##### B. Bộ câu hỏi phỏng vấn Kế toán / Thu ngân:
- **Tác nghiệp nhập liệu & Chốt sổ hàng tháng:**
  - Hàng tháng, chị mất khoảng bao nhiêu thời gian để lập danh sách thông báo thu tiền cho toàn bộ 30 tầng chung cư trên file Excel?
  - Những lỗi sai sót hoặc nhầm lẫn nào thường gặp nhất khi thao tác trên file Excel cũ?
- **Thanh toán & Gạch nợ:**
  - Tỷ lệ cư dân thanh toán bằng Tiền mặt và Chuyển khoản ngân hàng hiện tại là bao nhiêu %?
  - Khi cư dân chuyển khoản (thường chuyển thiếu nội dung hoặc không ghi số căn hộ), chị đang đối soát và đóng vết công nợ thủ công như thế nào?
- **Mẫu thông báo & Biên nhận:**
  - Mẫu Thông báo thu tiền và Phiếu thu hiện tại có những trường thông tin bắt buộc nào?
  - Kế toán có nhu cầu xuất/in phiếu thu theo dạng PDF hàng loạt hay không?
- **Hệ thống Báo cáo:**
  - Cuối tháng / cuối quý, Kế toán phải lập những loại Báo cáo tài chính nào gửi cho BQT và Tổ dân phố?
  - Chị có cần chức năng tự động Import/Export dữ liệu danh mục căn hộ và công nợ ra/vào file Excel không?

---

### BƯỚC 3: CẤU TRÚC HÓA & MÔ HÌNH HÓA YÊU CẦU (MODELING)
#### 1. Các công việc cần làm:
- Phân loại và cấu trúc thông tin thành Yêu cầu Chức năng (Functional) và Phi chức năng (Non-functional).
- Xây dựng sơ đồ Use Case phân quyền người dùng (Admin/BQT, Kế toán, Cư dân).
- Sơ đồ hóa luồng nghiệp vụ (Business Process Flowchart) từ lúc lập đợt thu $\rightarrow$ phát hành thông báo $\rightarrow$ nhận tiền $\rightarrow$ chốt công nợ $\rightarrow$ xuất báo cáo.
- Thiết kế phác thảo giao diện (Wireframes/Mockups) trên Figma.

#### 2. Kết quả thu được (Deliverables):
- **Danh mục Yêu cầu Chức năng (Functional Requirements List):**
  - Quản lý danh mục căn hộ & $m^2$ diện tích cho 30 tầng.
  - Tự động tính Phí dịch vụ & Phí quản lý theo diện tích.
  - Quản lý đợt thu quỹ vận động tự nguyện độc lập.
  - Lập và xuất Thông báo thu tiền định kỳ.
  - Cập nhật trạng thái thanh toán & quản lý công nợ.
  - Xuất báo cáo thu chi & danh sách hộ nợ phí.
- **Sơ đồ Use Case & Diagram luồng nghiệp vụ (BPMN/Flowchart).**
- **Bộ Wireframe/Prototype giao diện (Figma link & Export PDF):** Màn hình Dashboard, Màn hình Lập hóa đơn, Màn hình Sổ quỹ.

---

### BƯỚC 4: PHÂN TÍCH, THẨM ĐỊNH & GIẢI QUYẾT XUNG ĐỘT (ANALYSIS & CONFLICT RESOLUTION)
#### 1. Các công việc cần làm:
- Phân loại mức độ ưu tiên của yêu cầu bằng phương pháp MoSCoW (Must, Should, Could, Won't).
- Phát hiện và xử lý mâu thuẫn nghiệp vụ (ví dụ: gộp hay tách khoản bắt buộc và khoản đóng góp tự nguyện).
- Đánh giá tính khả thi kỹ thuật (Technical Feasibility) và rủi ro dữ liệu rác từ file Excel cũ.

#### 2. Kết quả thu được (Deliverables):
- **Ma trận Ưu tiên MoSCoW (Prioritized Requirements Matrix):**
  - *Must have:* Quản lý $m^2$ căn hộ, tính phí tự động theo $m^2$, quản lý đóng góp tự nguyện, chốt công nợ, báo cáo cơ bản.
  - *Should have:* In thông báo PDF, import/export Excel.
  - *Could have:* Tự động gửi thông báo qua Zalo OA.
  - *Won't have (Phase 1):* Gạch nợ ngân hàng tự động.
- **Biên bản Giải quyết Xung đột (Conflict Resolution Note):** Quyết định tách mẫu Thông báo thành Mục A (Phí bắt buộc theo $m^2$) và Mục B (Đóng góp tự nguyện tùy tâm).
- **Nhật ký Rủi ro & Biện pháp giảm thiểu (Risk Log):** Thiết kế bộ lọc Validate dữ liệu khi Import Excel để tránh sai lệch diện tích $m^2$.

---

### BƯỚC 5: TÀI LIỆU HÓA & PHÊ DUYỆT (DOCUMENTATION & SIGN-OFF)
#### 1. Các công việc cần làm:
- Tổng hợp toàn bộ kết quả vào Tài liệu Đặc tả Yêu cầu Phần mềm (SRS - Software Requirements Specification) chuẩn IEEE 830.
- Xây dựng Ma trận Truy xuất Nguồn gốc Yêu cầu (Traceability Matrix).
- Tổ chức buổi họp Walkthrough review tài liệu với Ban Quản trị và Kế toán để nghiệm thu.

#### 2. Kết quả thu được (Deliverables):
- **Tài liệu SRS (Software Requirements Specification) hoàn chỉnh.**
- **Ma trận Truy xuất Nguồn gốc (Requirements Traceability Matrix):** Ánh xạ từ yêu cầu người dùng $\rightarrow$ Use Case $\rightarrow$ Màn hình Figma $\rightarrow$ Test Case.
- **Biên bản Nghiệm thu Yêu cầu (Requirements Sign-off Document):** Văn bản có chữ ký xác nhận chính thức của Trưởng BQT Chung cư BlueMoon, cho phép bắt đầu giai đoạn Thiết kế & Lập trình.

---

### BẢNG TỔNG HỢP CÁC BƯỚC VÀ KẾT QUẢ ĐẦU RA

| Bước | Tên bước khai phá | Công việc cốt lõi | Kết quả đầu ra (Deliverables) |
|:---:|---|---|---|
| **Bước 1** | Xác định Bối cảnh & Chuẩn bị | Nhận diện Stakeholders, chốt phạm vi, lập lịch trình | Stakeholder Matrix, Scope Statement, Elicitation Schedule |
| **Bước 2** | Thực hiện Thu thập & Khai phá | Phỏng vấn BQT, Workshop Tổ dân phố, Survey Cư dân, Đọc Excel | Interview Notes, Biên bản Workshop, Báo cáo Survey, Data Excel mẫu |
| **Bước 3** | Cấu trúc hóa & Mô hình hóa | Phân loại yêu cầu, vẽ Use Case & Flowchart, dựng Figma Wireframe | Danh mục Yêu cầu (Functional/Non-functional), Diagram, Figma Prototype |
| **Bước 4** | Phân tích & Giải quyết xung đột | Phân hạng MoSCoW, giải quyết mâu thuẫn hóa đơn, đánh giá rủi ro | Ma trận MoSCoW, Biên bản giải quyết xung đột, Risk Log |
| **Bước 5** | Tài liệu hóa & Sign-off | Soạn SRS IEEE 830, lập Traceability Matrix, họp ký duyệt | Tài liệu SRS, Traceability Matrix, Biên bản Sign-off chính thức |

---

## PHẦN 2: BẢNG ÁNH XẠ 5 NHÓM TÀI LIỆU VÀO KẾ HOẠCH PHÂN CÔNG (TAB 1)
### Kế hoạch phân công 5 Thành viên trong 6 Ngày

| Nhóm tài liệu / giai đoạn | Tên tài liệu chi tiết | Thành viên phụ trách | Ngày hoàn thành | Sản phẩm dùng làm đầu vào cho |
|---|---|:---:|:---:|---|
| **1. Khởi động & Định hướng** | • Scope Statement (In-scope vs Out-of-scope v1.0/v2.0)<br>• Stakeholder Matrix / Register<br>• Requirements Elicitation Plan (Lịch trình & kịch bản)<br>• Bảng Mục tiêu SMART (OBJ-01 $\rightarrow$ OBJ-08) | **TV 1 (BA Lead)** | **Ngày 1** | Định hướng phạm vi toàn dự án; làm đầu vào cho phỏng vấn BQT, Kế toán và Tổ dân phố. |
| **2. Thu thập & Ghi nhận thô** | • **Client’s Wish List (RAW-001 $\rightarrow$ RAW-030+):**<br>&nbsp;&nbsp;- RAW Nhân khẩu & Cư dân (RAW-001 $\rightarrow$ 010): TV 2<br>&nbsp;&nbsp;- RAW Khoản thu & Thu phí (RAW-011 $\rightarrow$ 020): TV 3<br>&nbsp;&nbsp;- RAW Quỹ tự nguyện & v2.0 (RAW-021 $\rightarrow$ 032): TV 4<br>• **Bảng kịch bản hiện trạng As-Is Scenario (kèm Pain Points):**<br>&nbsp;&nbsp;- As-Is Quản lý Cư dân: TV 2<br>&nbsp;&nbsp;- As-Is Quy trình Thu phí & Quỹ: TV 3 & TV 4<br>• **Bảng kịch bản viễn cảnh Visionary Scenario (To-Be & v2.0):**<br>&nbsp;&nbsp;- Visionary v1.0 cốt lõi: TV 3<br>&nbsp;&nbsp;- Visionary gợi mở v2.0 (Gửi xe, Điện nước): TV 4<br>• **Bảng Yêu cầu Phi chức năng thô (NFR-001 $\rightarrow$ 005):** TV 5<br>• Biên bản phỏng vấn BQT, Survey Cư dân & File Excel mẫu: TV 3, TV 4 | **Tất cả TV** | **Ngày 2** | Cung cấp toàn bộ 30+ yêu cầu thô (RAW), As-Is và To-Be làm căn cứ phân rã Epic, Feature và User Stories. |
| **3. Cấu trúc hóa & Mô hình hóa** | • **Danh mục Epic & Feature ($\ge 5$ Epic, $\ge 10$ Feature):**<br>&nbsp;&nbsp;- Epic 1: Quản lý Căn hộ & Nhân khẩu (TV 2)<br>&nbsp;&nbsp;- Epic 2 & 3: Biểu phí & Thu phí định kỳ (TV 3)<br>&nbsp;&nbsp;- Epic 4: Quỹ tự nguyện & Báo cáo thống kê (TV 4)<br>&nbsp;&nbsp;- Epic 5: Quản trị hệ thống & Bảo mật (TV 1)<br>• **Bộ User Stories chuẩn INVEST (US-001 $\rightarrow$ US-010+):**<br>&nbsp;&nbsp;- US Quản lý Hộ khẩu & Nhân khẩu: TV 2<br>&nbsp;&nbsp;- US Biểu phí & Thu phí bắt buộc: TV 3<br>&nbsp;&nbsp;- US Quỹ tự nguyện & Thống kê: TV 4<br>• **Acceptance Criteria (GWT - Given/When/Then):**<br>&nbsp;&nbsp;- Viết và chuẩn hóa AC cho $\ge 80\%$ User Stories: TV 5 chủ trì<br>• *(Phụ trợ thiết kế):* Sơ đồ luồng BPMN & Phác thảo Figma sơ bộ: TV 2 & TV 3 | **Tất cả TV** | **Ngày 3** | TV 3 vẽ giao diện Figma dựa trên Use Case; TV 4 viết script SQL DDL dựa trên ERD. |
| **4. Thẩm định & Đánh giá** | • **Product Backlog (Ưu tiên theo MoSCoW & Sprint dự kiến):**<br>&nbsp;&nbsp;- Phân loại Must/Should/Could/Won't: TV 1 (BA Lead) chốt với BQT<br>• **Conflict Resolution Note:** Tách hóa đơn Mục A bắt buộc theo $m^2$ và Mục B ủng hộ tự nguyện: TV 4<br>• **Risk Log:** Quản lý rủi ro sai lệch dữ liệu Excel cũ: TV 5<br>• **Traceability Matrix 5 tầng:** Ánh xạ RAW ID $\rightarrow$ Epic $\rightarrow$ Feature $\rightarrow$ US $\rightarrow$ AC (đảm bảo trace $\ge 80\%$): TV 5 chủ trì, TV 1 kiểm duyệt | **TV 1, TV 4, TV 5** | **Ngày 4** | Đảm bảo 100% yêu cầu có khả năng truy vết và đo lường được trước khi hoàn thiện hồ sơ báo cáo. |
| **5. Chuẩn hóa & Nghiệm thu** | • Báo cáo Khai phá Yêu cầu (Requirement Elicitation Report) hoàn chỉnh nộp Giảng viên (khớp 100% Slide bài giảng)<br>• Requirements Sign-off Document (Biên bản nghiệm thu thống nhất với BQT) | **Cả 5 Thành viên** *(TV 1 & TV 5 chủ trì)* | **Ngày 5 - 6** | Bàn giao trọn bộ hồ sơ Elicitation cho Giảng viên hướng dẫn môn học CNPM và đại diện BQT Chung cư BlueMoon. |
