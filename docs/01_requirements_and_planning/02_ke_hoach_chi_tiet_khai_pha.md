# KẾ HOẠCH CHI TIẾT KHAI PHÁ YÊU CẦU
## Dự án: Phần mềm Quản lý Thu các loại phí tại Chung cư BlueMoon (BMS)
### Tài liệu nguồn: Tab 2 ("KẾ HOẠCH CHI TIẾT KHAI PHÁ YÊU CẦU") - Google Docs `CNPM_Proposal_Team13`

---

## I. TỔNG QUAN DỰ ÁN & MỤC TIÊU

### 1. Bối cảnh dự án
- **Thông tin tòa nhà:** Chung cư BlueMoon (khởi công năm 2021, hoàn thành và đưa vào vận hành từ năm 2023) tọa lạc tại vị trí đắc địa khu vực Ngã tư Văn Phú. Dự án xây dựng trên khuôn viên $450\text{ m}^2$ với quy mô 30 tầng nổi, bao gồm:
  - 1 tầng ki-ốt thương mại (Tầng 1).
  - 4 tầng đế dịch vụ (Tầng 2 – 5).
  - 24 tầng căn hộ ở (Tầng 6 – 29).
  - 1 tầng penthouse (Tầng 30).
- **Đơn vị vận hành:** Ban Quản trị (BQT) do Hội nghị cư dân bầu ra, đại diện quyền lợi và trực tiếp quản lý các hoạt động tài chính – vận hành.
- **Hiện trạng nghiệp vụ:** Việc quản lý, lập sổ sách và thu các khoản phí hiện vẫn thực hiện hoàn toàn thủ công kết hợp các file Excel riêng lẻ. Phương thức này gây ra nhiều bất cập: tốn thời gian tổng hợp, dễ phát sinh sai sót trong tính toán công nợ, khó tra cứu lịch sử nộp tiền và thiếu tính minh bạch với cư dân.

### 2. Bài toán thu phí
- **Phí dịch vụ chung cư:** Khoản phí bắt buộc hàng tháng, chi trả cho các hoạt động duy trì hạ tầng, vệ sinh cảnh quan, thu gom rác thải, an ninh bảo vệ. Đơn giá dao động từ **2.500 đến 16.500 VNĐ / $m^2$ / tháng** tùy theo loại hình mặt bằng.
- **Phí quản lý chung cư:** Khoản phí bắt buộc hàng tháng, phục vụ công tác điều hành và vận hành chung của tòa nhà. Tại BlueMoon, mức thu tối thiểu áp dụng từ **7.000 VNĐ / $m^2$ / tháng**.
- **Các khoản đóng góp tự nguyện / từ thiện:** Các đợt vận động ủng hộ do BQT phối hợp cùng Tổ dân phố triển khai (như Quỹ vì người nghèo, Quỹ biển đảo, Quỹ từ thiện...). Đây là khoản thu không bắt buộc, phụ thuộc vào tấm lòng tự nguyện của từng hộ gia đình.

### 3. Mục tiêu của Giai đoạn Khai phá Yêu cầu
- Khảo sát, làm rõ toàn bộ luồng nghiệp vụ thu phí hiện tại và các kỳ vọng chuyển đổi số từ Ban Quản trị.
- Thu thập, chuẩn hóa hệ thống Yêu cầu Chức năng (quản lý hộ gia đình, tính phí bắt buộc, thu quỹ vận động, gửi thông báo, đối soát thanh toán, xuất báo cáo) và Yêu cầu Phi chức năng (bảo mật dữ liệu, độ chính xác tài chính, tính dễ dùng).
- Xây dựng và thống nhất tài liệu Đặc tả Yêu cầu Phần mềm (SRS) chính thức, làm cơ sở ký duyệt (Sign-off) trước khi chuyển sang giai đoạn phát triển.

---

## II. PHÂN CÔNG NHÂN SỰ NHÓM (5 THÀNH VIÊN)

| Thành viên & Vai trò | Trách nhiệm cốt lõi | Phân công cụ thể giải quyết 6 sản phẩm bắt buộc (Slide Thầy) | Artefacts cam kết giao nộp |
|---|---|---|---|
| **Thành viên 1: Khánh**<br>*(BA Lead)* | • Quản lý dự án, kiểm soát tiến độ & chất lượng hồ sơ Elicitation.<br>• Thiết lập mục tiêu SMART & Ma trận bên liên quan.<br>• Đại diện làm việc với BQT Chung cư. | • Chủ trì Bảng mục tiêu SMART (OBJ-01 $\rightarrow$ OBJ-08) & Ma trận Stakeholders.<br>• Chủ trì lập Product Backlog (Ưu tiên theo MoSCoW & phân bổ Sprint dự kiến).<br>• Phụ trách mảng Quản trị hệ thống (Đăng nhập, phân quyền, đổi mật khẩu). | • Bảng SMART (OBJ-01 $\rightarrow$ 08)<br>• Ma trận Stakeholders<br>• Product Backlog (MoSCoW)<br>• Biên bản phỏng vấn BQT |
| **Thành viên 2: Hoà**<br>*(BA Nghiệp vụ 1: Hộ khẩu & Nhân khẩu)* | • Chuyên trách toàn bộ phân hệ Quản lý Căn hộ, Hộ gia đình và Nhân khẩu (giải quyết triệt để yêu cầu v1.0 bị thiếu). | • Lập Client's Wish List mảng Nhân khẩu (RAW-001 $\rightarrow$ RAW-010): hộ khẩu, nhân khẩu, CCCD, quan hệ chủ hộ.<br>• Viết As-Is Scenario quản lý cư dân (sổ sách thủ công) & chỉ ra Pain Points.<br>• Viết Visionary Scenario To-Be cho mảng quản lý cư dân số hóa.<br>• Phân rã Epic 1 (Nhân khẩu) & viết các User Stories chuẩn INVEST (US-001 $\rightarrow$ US-004). | • RAW-001 $\rightarrow$ RAW-010 (Nhân khẩu)<br>• As-Is Scenario (Cư dân & Pain Points)<br>• Visionary Scenario (Cư dân To-Be)<br>• User Stories (US-001 $\rightarrow$ US-004) |
| **Thành viên 3: Đức Anh**<br>*(BA Nghiệp vụ 2: Biểu phí & Thu phí)* | • Chuyên trách bài toán tính phí bắt buộc (theo $m^2$), lập bảng kê hàng tháng và quy trình gạch nợ thanh toán. | • Lập Client's Wish List mảng Thu phí (RAW-011 $\rightarrow$ RAW-020): phí dịch vụ (2.500-16.500đ/$m^2$), phí quản lý ($\ge 7.000$đ/$m^2$).<br>• Viết As-Is Scenario quy trình thu phí Excel/biên lai giấy & Pain Points sai lệch số liệu.<br>• Viết Visionary Scenario quy trình thu phí tự động, xuất phiếu PDF.<br>• Phân rã Epic 2 & 3 (Biểu phí, Thu nợ) & viết User Stories INVEST (US-005 $\rightarrow$ US-007). | • RAW-011 $\rightarrow$ RAW-020 (Thu phí)<br>• As-Is Scenario (Thu phí & Pain Points)<br>• Visionary Scenario (Thu phí To-Be)<br>• User Stories (US-005 $\rightarrow$ US-007) |
| **Thành viên 4: Sơn**<br>*(BA Nghiệp vụ 3: Quỹ tự nguyện & v2.0)* | • Chuyên trách quản lý các quỹ vận động xã hội (tùy tâm), hệ thống báo cáo thống kê và gợi mở Roadmap v2.0. | • Lập Client's Wish List mảng Quỹ tự nguyện & v2.0 (RAW-021 $\rightarrow$ RAW-032): từ thiện, bão lụt; gửi xe máy 70k, ô tô 1tr2, thu hộ điện nước.<br>• Viết As-Is & Visionary Scenario cho Quỹ đóng góp và Roadmap mở rộng v2.0.<br>• Soạn Conflict Resolution Note (tách rõ Mục A bắt buộc và Mục B tự nguyện trên thông báo thu tiền).<br>• Phân rã Epic 4 (Thống kê) & viết User Stories INVEST (US-008 $\rightarrow$ US-010). | • RAW-021 $\rightarrow$ RAW-032 (Quỹ & v2.0)<br>• Visionary Roadmap v2.0<br>• Conflict Resolution Note<br>• User Stories (US-008 $\rightarrow$ US-010) |
| **Thành viên 5: Huy**<br>*(BA Nghiệp vụ 4: QA, NFR & Traceability)* | • Chuyên trách bảo đảm chất lượng yêu cầu (QA), tiêu chuẩn kiểm thử được (Testability) và khả năng truy vết dữ liệu. | • Lập Bảng Yêu cầu Phi chức năng thô (NFR-01 $\rightarrow$ NFR-05: bảo mật tài khoản BQT, CSDL PostgreSQL, hiệu năng) & Risk Log Excel.<br>• Chủ trì viết toàn bộ Acceptance Criteria (AC) theo chuẩn Given - When - Then (GWT) cho 100% User Stories.<br>• Lập bảng Ma trận Truy xuất Nguồn gốc 5 tầng (Traceability Matrix: RAW ID $\rightarrow$ Epic $\rightarrow$ Feature $\rightarrow$ User Story $\rightarrow$ AC) đạt tỷ lệ truy vết $\ge 80\%$. | • Bảng NFR thô (NFR-01 $\rightarrow$ 005)<br>• Risk Log dữ liệu Excel<br>• Bộ Acceptance Criteria (chuẩn GWT)<br>• Traceability Matrix 5 tầng ($\ge 80\%$) |

---

## III. BƯỚC 1: CHUẨN BỊ & XÁC ĐỊNH BỐI CẢNH

### 1. Ma trận Stakeholders (Đại diện các bên liên quan)

| Nhóm Stakeholder | Đại diện | Vai trò & Mức độ ảnh hưởng | Kỳ vọng chính |
|---|---|---|---|
| **Ban Quản trị (BQT)** | Trưởng Ban Quản trị | Primary / High Influence | Quản lý minh bạch, giảm thất thoát, lập thông báo thu phí nhanh chóng. |
| **Bộ phận Kế toán / Thu ngân** | Kế toán trưởng BQT | Operational / High Impact | Tự động hóa tính toán, giảm thao tác thủ công, đối soát công nợ chính xác. |
| **Chính quyền & Tổ dân phố** | Tổ trưởng Tổ dân phố | Collaborator / Medium Impact | Công khai danh sách đóng góp tự nguyện, dễ thu thập và tổng hợp quỹ. |
| **Cư dân / Chủ căn hộ** | Đại diện cư dân (24 tầng) | End-User / High Impact | Nhận thông báo rõ ràng, tra cứu lịch sử đóng tiền minh bạch, chính xác. |
| **Chủ Kiot & Khối Thương mại** | Đại diện Kiot (Tầng 1 & Đế) | End-User / Medium Impact | Tính đúng đơn giá kinh doanh theo diện tích hợp đồng. |

### 2. Xác định Phạm vi Dự án (Project Scope)
- **Trong phạm vi (In-Scope - Phiên bản 1):**
  - Quản lý danh mục căn hộ, diện tích sử dụng ($m^2$) và thông tin chủ hộ của 30 tầng.
  - Tự động tính toán Phí dịch vụ và Phí quản lý theo diện tích $m^2$ và đơn giá cấu hình.
  - Quản lý các đợt thu quỹ xã hội/từ thiện tự nguyện (không áp đơn giá cố định).
  - Phát hành và lập Thông báo thu tiền định kỳ hàng tháng cho từng hộ.
  - Ghi nhận trạng thái thanh toán (chuyển khoản/tiền mặt) và đóng vết công nợ thủ công/bán tự động.
  - Hệ thống Báo cáo - Thống kê thu chi, danh sách nợ phí, danh sách ủng hộ.
- **Ngoài phạm vi (Out-of-Scope - Phiên bản 1 / Quy hoạch Phase 2):**
  - Tích hợp cổng thanh toán trực tuyến tự động (Payment Gateway).
  - Tích hợp hệ thống kiểm soát xe tự động tại hầm gửi xe (Parking Management System).

---

## IV. BƯỚC 2: THỰC HIỆN THU THẬP & KHAI PHÁ YÊU CẦU

1. **Phỏng vấn sâu (In-depth Interview):**
   - *Đối tượng:* Trưởng Ban Quản trị & Kế toán BQT.
   - *Nội dung:* Khảo sát các công thức tính phí thực tế trên file Excel hiện tại; quy trình xử lý các trường hợp nợ phí kéo dài; quy trình chốt sổ kế toán vào ngày cuối tháng.
2. **Workshop làm việc nhóm (JAD Session):**
   - *Thành phần:* BQT chung cư, Đại diện Tổ dân phố, Nhóm BA.
   - *Nội dung:* Thống nhất phương án quản lý các khoản thu vận động tự nguyện, đảm bảo tách biệt hoàn toàn với phí bắt buộc và công khai minh bạch danh sách ủng hộ.
3. **Khảo sát qua bảng hỏi (Survey / Questionnaire):**
   - *Đối tượng:* Cư dân thuộc 24 tầng nhà ở và tầng Penthouse.
   - *Nội dung:* Thu thập ý kiến về kênh nhận thông báo thu phí ưa thích (SMS, Zalo, Giấy thông báo, Web/App) và đánh giá mức độ hài lòng về tính minh bạch thu chi hiện tại.
4. **Quan sát thực tế & Phân tích tài liệu (Observation & Document Analysis):**
   - *Quan sát:* Trực tiếp theo dõi ca làm việc của Kế toán khi viết phiếu thu tiền mặt và nhập liệu vào file Excel.
   - *Phân tích tài liệu:* Kiểm tra dữ liệu thu phí các năm 2023 - 2026 để xác định cấu trúc dữ liệu, các lỗi lệch dòng/sai công thức thường gặp trong file Excel cũ.

---

## V. BƯỚC 3: CẤU TRÚC HÓA & MÔ HÌNH HÓA YÊU CẦU

### 1. Nhóm Yêu cầu Chức năng (Functional Requirements)
- **Quản lý Căn hộ & Diện tích:** Đăng ký và quản lý danh mục mặt bằng của 30 tầng (Tầng 1 Kiot, 4 tầng đế commercial, 24 tầng ở, 1 tầng Penthouse). Lưu trữ chính xác diện tích $m^2$ đến chữ số thập phân.
- **Quản lý Phí Bắt buộc:** Cấu hình đơn giá Phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$) và Phí quản lý ($\ge 7.000\text{ đ}/m^2$). Tự động tính tiền theo công thức:
  $$\text{Tổng phí bắt buộc} = \text{Diện tích } (m^2) \times (\text{Đơn giá Dịch vụ} + \text{Đơn giá Quản lý})$$
- **Quản lý Quỹ Tự nguyện:** Tạo đợt vận động ủng hộ (Ủng hộ bão lụt, Vì người nghèo...). Ghi nhận số tiền đóng góp tùy tâm theo từng hộ (không bắt buộc và không áp công thức tính theo $m^2$).
- **Thông báo & Thanh toán:** Tổng hợp và xuất Thông báo thu tiền định kỳ; Cập nhật trạng thái công nợ (Đã thanh toán, Thanh toán một phần, Chưa thanh toán, Nợ đọng); In phiếu thu và xuất file PDF.
- **Báo cáo - Thống kê:** Báo cáo doanh thu thu phí theo tháng/quý/năm; Báo cáo danh sách các hộ nợ phí; Báo cáo tổng kết đợt vận động quỹ tự nguyện.

### 2. Mô hình hóa hệ thống
- **Use Case Diagram:** Xây dựng sơ đồ phân quyền các nhóm người dùng: BQT (Toàn quyền quản trị), Kế toán (Tác nghiệp thu chi, báo cáo), Cư dân (Tra cứu hóa đơn, lịch sử nộp tiền).
- **Sơ đồ luồng công việc (Process Flowchart):**
  $$\text{[BQT/Kế toán] Khởi tạo đợt thu phí} \longrightarrow \text{[Hệ thống] Tự động tính phí theo } m^2 \longrightarrow \text{[Hệ thống] Tạo Thông báo gửi Cư dân} \longrightarrow \text{[Cư dân] Thanh toán} \longrightarrow \text{[Kế toán] Đóng vết công nợ} \longrightarrow \text{[Hệ thống] Xuất Phiếu thu & Cập nhật Báo cáo}$$
- **Xây dựng Wireframe / Prototype:** Thiết kế bản phác thảo giao diện trên Figma bao gồm: Dashboard tổng quan dành cho BQT, Giao diện Lập phiếu thu dành cho Kế toán, và Giao diện Tra cứu công nợ dành cho Cư dân.

---

## VI. BƯỚC 4: PHÂN TÍCH, THẨM ĐỊNH & GIẢI QUYẾT MÂU THUẪN

### 1. Phân loại ưu tiên bằng phương pháp MoSCoW

```text
+---------------------------------------------------------------------------------+
|                                 MÔ HÌNH MOSCOW                                  |
+---------------------------------------------------------------------------------+
| MUST HAVE (Bắt buộc phải có)                                                    |
|  - Quản lý danh mục 30 tầng và diện tích m2 từng căn hộ.                       |
|  - Thuật toán tự động tính phí dịch vụ & quản lý theo m2.                        |
|  - Quản lý các đợt thu quỹ vận động tự nguyện.                                  |
|  - Quản lý trạng thái thanh toán và đóng vết công nợ.                           |
|  - Báo cáo thu chi và công nợ cơ bản.                                           |
+---------------------------------------------------------------------------------+
| SHOULD HAVE (Nên có)                                                            |
|  - Xuất thông báo thu tiền/phiếu thu dưới dạng PDF & hỗ trợ in ấn.              |
|  - Quản lý và theo dõi danh sách hộ nợ phí kéo dài.                             |
|  - Import / Export dữ liệu danh mục căn hộ từ file Excel.                       |
+---------------------------------------------------------------------------------+
| COULD HAVE (Có thể phát triển nếu đủ thời gian)                                 |
|  - Tích hợp Zalo OA / Email để gửi thông báo thu phí tự động.                   |
+---------------------------------------------------------------------------------+
| WON'T HAVE (Chưa thực hiện trong Phase 1)                                       |
|  - Tích hợp Cổng thanh toán trực tuyến (VNPay/Momo) để gạch nợ tự động.          |
+---------------------------------------------------------------------------------+
```

### 2. Giải quyết xung đột nghiệp vụ (Conflict Resolution)
- **Mâu thuẫn:** BQT muốn đưa toàn bộ khoản đóng góp từ thiện vào chung một phiếu báo thu tiền dịch vụ để tăng tỷ lệ thu; Cư dân phản đối vì lo ngại nhập nhằng giữa khoản thu bắt buộc và khoản đóng góp tự nguyện.
- **Giải pháp thống nhất:** Thiết kế mẫu Thông báo thu tiền chia rõ làm 2 phần độc lập:
  - **Mục A (Khoản phí bắt buộc):** Tính chính xác theo diện tích $m^2$ (Phí dịch vụ + Phí quản lý).
  - **Mục B (Khoản đóng góp tự nguyện):** Hiển thị tên đợt vận động, cư dân tùy chọn điền số tiền ủng hộ hoặc BQT ghi nhận số tiền tự nguyện riêng, không tính vào tổng nợ bắt buộc.

### 3. Đánh giá Khả thi & Quản lý Rủi ro (Risk Management)
- **Rủi ro:** Dữ liệu căn hộ và diện tích trên file Excel cũ bị lệch format, thiếu dữ liệu hoặc không đồng nhất.
- **Giải pháp:** Thiết kế tính năng Standardize & Validate dữ liệu đầu vào. Hệ thống sẽ cảnh báo các dòng sai dữ liệu/thiếu diện tích khi Kế toán Import file Excel vào phần mềm.

---

## VII. BƯỚC 5: TÀI LIỆU HÓA & XÁC NHẬN (SIGN-OFF)

- **Soạn thảo Tài liệu Đặc tả SRS:** Biên soạn tài liệu SRS theo chuẩn IEEE 830 bao gồm: Tổng quan hệ thống, Mô tả chi tiết từng Use Case, Yêu cầu Phi chức năng (Bảo mật, Hiệu năng), Wireframe giao diện và các Ràng buộc nghiệp vụ.
- **Xây dựng Ma trận Truy xuất Nguồn gốc (Traceability Matrix):** Thiết lập bảng ánh xạ liên kết từ Yêu cầu ban đầu của BQT/Cư dân $\rightarrow$ Use Case tương ứng $\rightarrow$ Vị trí mô tả trong tài liệu SRS $\rightarrow$ Kế hoạch Test Case.
- **Quy trình Review & Chốt Yêu cầu (Sign-off):**
  - Tổ chức buổi họp Walkthrough trình bày chi tiết tài liệu SRS với Trưởng BQT và Kế toán.
  - Ghi nhận các phản hồi chỉnh sửa cuối cùng và hoàn thiện tài liệu.
  - Tiến hành ký Biên bản Xác nhận Yêu cầu (Requirement Sign-off Document) giữa Đại diện BQT BlueMoon và Nhóm thực hiện dự án.

---

## VIII. CÔNG CỤ SỬ DỤNG & SẢN PHẨM ĐẦU RA THEO TÁC VỤ

### 1. Công cụ hỗ trợ dự án
- **Quản lý công việc & Yêu cầu:** Jira Software, Confluence.
- **Vẽ sơ đồ & Luồng nghiệp vụ:** Draw.io, Lucidchart.
- **Thiết kế Wireframe / Prototype:** Figma.
- **Khảo sát & Soạn thảo:** Google Forms, MS Word / Google Docs.

### 2. Danh mục Sản phẩm Đầu ra (Deliverables)

| STT | Sản phẩm đầu ra (Deliverable) | Mô tả chi tiết theo chuẩn Slide Thầy | Nhân sự phụ trách |
|:---:|---|---|:---:|
| **1** | Mục tiêu SMART & Stakeholder Matrix | 8 mục tiêu đo lường được (OBJ-01 $\rightarrow$ OBJ-08) và ma trận phân tích các bên liên quan. | TV 1 (BA Lead) |
| **2** | Biên bản Phỏng vấn & Khảo sát cư dân | Ghi chép làm việc với BQT, phiếu khảo sát cư dân và bộ dữ liệu Excel cũ. | TV 1, TV 3 & TV 4 |
| **3** | Client's Wish List (RAW-001 $\rightarrow$ RAW-032) | Danh sách $\ge 30$ mong muốn thô bao quát cả Hộ khẩu, Nhân khẩu, Thu phí và Quỹ. | TV 2, TV 3 & TV 4 *(TV 1 tổng hợp)* |
| **4** | As-Is & Visionary Scenarios | Bảng quy trình hiện trạng (kèm Pain Points) và kịch bản tương lai (gợi mở v2.0 gửi xe, điện nước). | TV 2, TV 3 & TV 4 |
| **5** | Bảng NFR thô & Risk Log | $\ge 5$ yêu cầu phi chức năng (bảo mật, PostgreSQL, hiệu năng) và nhật ký quản lý rủi ro dữ liệu. | TV 5 (QA Specialist) |
| **6** | Danh mục Epic & Feature (Scrum) | Chuẩn hóa kết quả thô thành $\ge 5$ Epic lớn và $\ge 10$ Feature cụ thể. | TV 1 (Chủ trì) & Cả nhóm |
| **7** | Bộ User Stories (INVEST) & AC (GWT) | $\ge 10$ User Stories viết chuẩn INVEST, kèm Acceptance Criteria dạng Given-When-Then testable. | TV 2, 3, 4 (viết US), TV 5 (chuẩn hóa AC) |
| **8** | Product Backlog (MoSCoW) | Bảng tồn đọng sản phẩm phân hạng Must/Should/Could/Won't và phân bổ Sprint dự kiến. | TV 1 (BA Lead) |
| **9** | Traceability Matrix 5 Tầng | Ma trận truy xuất nguồn gốc: RAW ID $\rightarrow$ Epic $\rightarrow$ Feature $\rightarrow$ User Story $\rightarrow$ AC (đạt $\ge 80\%$). | TV 5 (Trace Lead) & TV 1 |
