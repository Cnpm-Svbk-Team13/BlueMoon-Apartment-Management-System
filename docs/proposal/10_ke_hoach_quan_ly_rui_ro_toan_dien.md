# KẾ HOẠCH QUẢN LÝ RỦI RO DỰ ÁN TOÀN DIỆN (RISK MANAGEMENT PLAN)
## HỆ THỐNG QUẢN LÝ THU PHÍ CHUNG CƯ BLUEMOON (BLUEMOON BMS)

> **Mã dự án:** `PRJ-BLUEMOON-2026`  
> **Học phần:** IT3180 - Nhập môn Công nghệ Phần mềm | Bộ môn CNPM - Trường CNTT&TT - ĐHBK Hà Nội  
> **Nhóm thực hiện:** Nhóm 13 (Team 13)  
> **Căn cứ tài liệu:** Bài 4.3 trong `Huong dan xay dung Ke hoach du an.pdf` & Mẫu `Mau_theo_doi_rui_ro_du_an.xlsx`.  
> **Phiên bản tài liệu:** v1.0 | Ngày ban hành: 25/10/2026

---

## 1. MỤC TIÊU VÀ QUY TRÌNH QUẢN LÝ RỦI RO

Mục tiêu của kế hoạch quản lý rủi ro là chủ động nhận diện, phân tích định lượng xác suất và mức độ tác động, phân hạng ưu tiên và thiết lập các biện pháp phòng ngừa (Prevention) cùng kế hoạch dự phòng (Contingency plan) cho toàn bộ các nguy cơ có thể ảnh hưởng đến tiến độ, ngân sách, chất lượng phần mềm và tính pháp lý của dự án chung cư BlueMoon.

Quy trình quản lý rủi ro tuân thủ 4 bước chuẩn của Bộ môn Công nghệ Phần mềm:
1. **Nhận diện rủi ro (Risk Identification):** Xác định các nguy cơ tiềm ẩn từ nguồn nhân lực, công nghệ, yêu cầu khách hàng, dữ liệu lịch sử và pháp lý.
2. **Định lượng xác suất & tác động (Risk Assessment):** Sử dụng thang đo chuẩn hóa 3 mức (Bảng 4-2 và Bảng 4-3).
3. **Phân hạng rủi ro bằng Ma trận $3 \times 3$ (Risk Prioritization):** Ánh xạ tọa độ vào ma trận để xác định cấp độ rủi ro từ Mức 1 đến Mức 5.
4. **Giám sát và thực thi ứng phó (Risk Monitoring & Response):** Thiết lập bảng theo dõi trạng thái nhiệm vụ (Risk Tracking Log) và hành động xử lý định kỳ trong các buổi Daily Scrum và Sprint Review.

---

## 2. THANG ĐO ĐÁNH GIÁ CHUẨN SOICT / HUST

### 2.1. Thang đo Đánh giá Xác suất Rủi ro (Bảng 4-2)

Độ khả dĩ xảy ra rủi ro được định lượng theo 3 mức độ chuẩn của bộ môn:

| Mức độ | Xác suất | Giải thích & Tần suất định lượng |
| :---: | :---: | :--- |
| **Thấp (1)** | $< 20\%$ | Rất hiếm khi xảy ra hoặc được cho là sẽ không xảy ra trong điều kiện vận hành bình thường. |
| **Vừa (2)** | $20\% - 50\%$ | Đã xảy ra thỉnh thoảng hoặc có lý do hợp lý nhưng không hoàn toàn cơ hội dự kiến xảy ra trong tương lai. |
| **Cao (3)** | $> 50\%$ | Cho thấy dấu hiệu rủi ro cao, đã xảy ra thường xuyên, có ảnh hưởng rất lớn cơ hội xảy ra trong tương lai. |

---

### 2.2. Thang đo Đánh giá Tác động Rủi ro (Bảng 4-3)

Mức độ thiệt hại khi rủi ro trở thành hiện thực được đánh giá độc lập trên 3 khía cạnh: **Phạm vi (Scope)**, **Chất lượng (Quality)** và **Chi phí / Nỗ lực (Cost/Effort)**:

| Tác động | Về phạm vi (Scope) | Về chất lượng (Quality) | Về chi phí / Nỗ lực (Cost/Effort) |
| :---: | :--- | :--- | :--- |
| **Thấp (1)** | $< 5\%$ quy mô dự án bị ảnh hưởng<br>*HOẶC* Các khu vực nhỏ trong phạm vi dự án bị ảnh hưởng | Hiệu suất chất lượng giảm $< 5\%$ so với Mục tiêu ban đầu | $< 5\%$ nỗ lực dự án phát sinh / tăng chi phí |
| **Vừa (2)** | $5\% - 10\%$ quy mô dự án bị ảnh hưởng | Giảm chất lượng yêu cầu sự chấp thuận của nhà tài trợ<br>*HOẶC* Hiệu suất chất lượng giảm từ $5\% - 10\%$ Mục tiêu | $5\% - 10\%$ nỗ lực dự án phát sinh / tăng chi phí |
| **Cao (3)** | $> 10\%$ quy mô dự án bị ảnh hưởng | Giảm chất lượng không thể chấp nhận để nhà tài trợ nghiệm thu<br>*HOẶC* Hiệu suất chất lượng giảm $> 10\%$ Mục tiêu | $> 10\%$ nỗ lực dự án phát sinh / tăng chi phí |

---

## 3. MA TRẬN ĐÁNH GIÁ MỨC ĐỘ RỦI RO $3 \times 3$ (RISK MATRIX)

Sự kết hợp giữa **Xác suất (Probability)** và **Tác động (Impact)** dẫn đến cấp độ nghiêm trọng của rủi ro được xếp thành 5 mức độ (từ Mức 1 đến Mức 5), trong đó **Mức 5 là nghiêm trọng nhất**:

| Xác suất \ Tác động | Thấp (1) | Vừa (2) | Cao (3) |
| :---: | :---: | :---: | :---: |
| **Cao (3)** | **Mức 3** *(Trung bình)* | **Mức 4** *(Nghiêm trọng)* | **Mức 5** *(Cực kỳ nguy cấp)* |
| **Vừa (2)** | **Mức 2** *(Thấp)* | **Mức 3** *(Trung bình)* | **Mức 4** *(Nghiêm trọng)* |
| **Thấp (1)** | **Mức 1** *(Không đáng kể)* | **Mức 2** *(Thấp)* | **Mức 3** *(Trung bình)* |

### Định nghĩa 5 mức độ ảnh hưởng của rủi ro:
- 🔴 **Mức 5 (Cực kỳ nguy cấp - Red):** Rủi ro chí tử, **không thể tiếp tục thực hiện dự án** nếu không có giải pháp khắc phục triệt để. Cần báo cáo khẩn cấp lên Nhà tài trợ (BQT) và GVHD.
- 🟠 **Mức 4 (Nghiêm trọng - Orange):** **Tác động đáng kể đến dự án**, làm đình trệ hoặc một số công việc của dự án không thực hiện được. Đòi hỏi hành động can thiệp tức thời.
- 🟡 **Mức 3 (Trung bình - Yellow):** **Tác động đến dự án, nhưng dự án vẫn thực hiện được**. Cần phân công nhân sự theo dõi sát sao và có kịch bản dự phòng.
- 🟢 **Mức 2 (Thấp - Light Green):** **Không ảnh hưởng đáng kể đến dự án**, có thể giải quyết trong các buổi họp kỹ thuật nội bộ thông thường.
- ⚪ **Mức 1 (Không đáng kể - Green):** **Hầu như không ảnh hưởng hoặc ảnh hưởng không rõ ràng** đến tiến độ và chất lượng sản phẩm.

---

## 4. BẢNG MÔ TẢ RỦI RO DỰ ÁN CHI TIẾT (BẢNG 4-1 MỞ RỘNG)

Bảng phân tích rủi ro dưới đây tích hợp trọn vẹn **9 rủi ro dự án mẫu chuẩn của Bộ môn CNPM** (STT 1 đến 9) cùng **4 rủi ro thực tế phát sinh từ nghiệp vụ quản lý chung cư BlueMoon của Team 13** (STT 10 đến 13):

| STT | Mã Rủi ro | Mô tả Rủi ro | Xác suất | Tác động | Mức độ rủi ro | Hành động ngăn ngừa & Kế hoạch ứng phó chi tiết |
| :---: | :---: | :--- | :---: | :---: | :---: | :--- |
| **1** | `RSK-01` | **Nhân sự phải tham gia nhiều môn học/dự án cùng lúc hoặc rời dự án** | Thấp (1) | Vừa (2) | **Mức 2** *(Minor)* | - Chia đều các công việc cốt lõi cho cả 5 thành viên, không để mã nguồn phụ thuộc vào 1 cá nhân (Pair programming).<br>- Chuẩn bị nhân sự dự phòng (Hoà và Sơn có thể hỗ trợ chéo DB/Backend, Đức Anh và Khánh hỗ trợ UI/BA).<br>- Đảm bảo mọi thành viên nắm rõ cam kết tiến độ cá nhân và nỗ lực tối thiểu 15h/tuần. |
| **2** | `RSK-02` | **Các bên liên quan (BQT/Kế toán) thay đổi yêu cầu hoặc bổ sung yêu cầu mới sau khi bắt đầu dự án** | Vừa (2) | Thấp (1) | **Mức 2** *(Minor)* | - Có biên bản ký duyệt Tuyên ngôn dự án và Wishlist yêu cầu v1.0 bằng văn bản rõ ràng.<br>- Xây dựng quy trình tiếp nhận thay đổi (Change Request): Mọi yêu cầu mới ngoài 11 User Stories đã duyệt sẽ được ghi nhận vào Backlog phiên bản v2.0.<br>- Đàm phán lại về thời gian và chi phí nếu BQT yêu cầu bắt buộc bổ sung trong v1.0. |
| **3** | `RSK-03` | **Các tài liệu kỹ thuật và tài liệu thiết kế kèm theo mã nguồn chưa hoàn thiện** | Thấp (1) | Cao (3) | **Mức 3** *(Vừa)* | - Thiết lập tiêu chuẩn Definition of Done (DoD): Code chỉ được merge khi có tài liệu API docs và cập nhật mô hình dữ liệu tương ứng.<br>- QA Lead (Huy) và Tech Lead (Hoà) định kỳ rà soát các tài liệu trung gian sau mỗi Sprint Review, xử lý kịp thời các thiếu sót. |
| **4** | `RSK-04` | **Thiếu sự tương tác, trao đổi đa chiều dẫn đến hiểu lầm, bất đồng quan điểm nghiệp vụ** | Vừa (2) | Vừa (2) | **Mức 3** *(Vừa)* | - Tổ chức họp giao ban định kỳ 2 tuần/lần với Trưởng ban quản trị và Kế toán để demo trực tiếp trên giao diện thử nghiệm.<br>- Xây dựng kịch bản sử dụng giả định (User Scenarios) cụ thể bằng hình ảnh minh họa cho khách hàng.<br>- Thiết lập nhóm Zalo/Telegram trao đổi tức thời giữa BQT và nhóm phát triển. |
| **5** | `RSK-05` | **Sự chậm trễ trong các giai đoạn dự án trước đó dồn áp lực sang giai đoạn sau** | Thấp (1) | Vừa (2) | **Mức 2** *(Minor)* | - Phân bổ công việc theo mức độ ưu tiên MoSCoW, hoàn thành dứt điểm các tính năng Must-have trong Sprint 1 và Sprint 2.<br>- Tổ chức họp Sprint Planning đầu tuần để công bố rõ mục tiêu WBS và thời hạn bàn giao từng task. |
| **6** | `RSK-06` | **Hành động pháp lý trì hoãn hoặc tạm dừng dự án (Tranh chấp BQT cũ - mới)** | Thấp (1) | Vừa (2) | **Mức 2** *(Minor)* | - Xác minh tư cách pháp nhân hợp pháp của Ban quản trị đương nhiệm căn cứ theo Quyết định công nhận của UBND phường sở tại trước khi triển khai.<br>- Kiểm tra kỹ tính hợp pháp của các quy chế thu phí nội bộ tòa nhà. |
| **7** | `RSK-07` | **Khách hàng từ chối nghiệm thu sản phẩm do giao diện khó dùng hoặc sai lệch số liệu** | Thấp (1) | Vừa (2) | **Mức 2** *(Minor)* | - Thỏa thuận bằng văn bản về Tiêu chí chấp nhận (Acceptance Criteria - AC) chi tiết cho từng User Story từ đầu dự án.<br>- Mời Kế toán trực tiếp nhập liệu thử nghiệm và gạch nợ trên dữ liệu thực tế tại buổi UAT trước khi bàn giao chính thức 1 tuần. |
| **8** | `RSK-08` | **Cắt giảm ngân sách dự án bất ngờ** | Thấp (1) | Thấp (1) | **Mức 1** *(Trivial)* | - Dự án sử dụng hoàn toàn các nền tảng công nghệ nguồn mở miễn phí (Java Spring Boot, PostgreSQL, Docker, GitHub, Thymeleaf) nên chi phí tài chính hầu như bằng 0.<br>- Dự trù quỹ dự phòng nhỏ từ nhóm cho chi phí in ấn tài liệu và đi lại khảo sát. |
| **9** | `RSK-09` | **Ước tính thời gian và chi phí quá lạc quan (Over-optimistic estimation)** | Vừa (2) | Vừa (2) | **Mức 3** *(Vừa)* | - Sử dụng kỹ thuật Planning Poker tập thể để ước lượng Story Points cho từng User Story.<br>- Dự phòng hệ số an toàn thời gian ($20\%$) cho các phân hệ phức tạp như tính toán công nợ tự động và giao dịch gạch nợ. |
| **10** | `RSK-10` | **Dữ liệu Excel lịch sử của BQT bị phân mảnh, sai lệch định dạng diện tích và số phòng khi import** | Cao (3) | Vừa (2) | **Mức 4** *(Nghiêm trọng)* | - Thiết kế module tiếp nhận dữ liệu có cơ chế Data Sanitizer & Validation nghiêm ngặt (kiểm tra định dạng số thực cho diện tích $m^2$, kiểm tra trùng mã căn hộ).<br>- Cung cấp file mẫu Excel chuẩn (`BlueMoon_Import_Template.xlsx`) và hỗ trợ kế toán chuẩn hóa dữ liệu trước khi nạp vào hệ thống. |
| **11** | `RSK-11` | **Xung đột pháp lý giữa khoản thu bắt buộc (theo $m^2$) và tự nguyện (đóng góp), kế toán gộp nhầm gây phản ứng từ cư dân** | Vừa (2) | Cao (3) | **Mức 4** *(Nghiêm trọng)* | - Thiết kế cơ sở dữ liệu tách biệt hoàn toàn 2 bảng: `mandatory_fees` (Phí bắt buộc có chế tài) và `voluntary_contributions` (Khoản thu tự nguyện).<br>- Giao diện in biên lai tách bạch 2 mục rõ ràng, tuân thủ nghiêm ngặt Điều 10 Thông tư 02/2016/TT-BXD và Nghị định 93/2021/NĐ-CP. |
| **12** | `RSK-12` | **Trùng lặp mã định danh CCCD/ĐDCN giữa các căn hộ khi một cư dân sở hữu hoặc thuê nhiều phòng** | Vừa (2) | Vừa (2) | **Mức 3** *(Vừa)* | - Thiết kế quan hệ Nhiều - Nhiều ($N - N$) giữa Cư dân (`Resident`) và Căn hộ (`Apartment`) qua bảng trung gian `Apartment_Resident_Relation`.<br>- Khóa chính định danh cư dân duy nhất bằng số CCCD, cho phép một cá nhân có thể là chủ hộ phòng này nhưng là người thuê tại phòng khác mà không gây lỗi ràng buộc toàn vẹn cơ sở dữ liệu. |
| **13** | `RSK-13` | **Lỗi giao dịch đồng thời (ACID Transaction Failure) khi nhiều kế toán cùng gạch nợ cho một căn hộ tại quầy** | Thấp (1) | Cao (3) | **Mức 3** *(Vừa)* | - Áp dụng cơ chế khóa bi quan/lạc quan (Pessimistic / Optimistic Locking với cột `@Version`) trên hóa đơn nợ.<br>- Đặt toàn bộ luồng gạch nợ trong Transaction mức cô lập `SERIALIZABLE` hoặc `REPEATABLE READ`, đảm bảo một hóa đơn chỉ được thanh toán một lần duy nhất. |

---

## 5. BẢNG THEO DÕI VÀ GIÁM SÁT RỦI RO DỰ ÁN (PROJECT RISK TRACKING LOG)

Bảng theo dõi tiến độ xử lý và kiểm soát rủi ro tuân thủ chính xác cấu trúc mẫu `Mau_theo_doi_rui_ro_du_an.xlsx` từ tài liệu của bộ môn:

| Mã Rủi ro (RISK) | Task ID | Nhiệm vụ / Hành động ứng phó cụ thể (TASK) | Người phụ trách (ASSIGNED TO) | Thời hạn (DEADLINE) | Trạng thái (STATUS) | Mức độ rủi ro (RISK LEVEL) | Ghi chú & Kế hoạch dự phòng (COMMENTS) |
| :---: | :---: | :--- | :---: | :---: | :---: | :---: | :--- |
| `RSK-10` | **T-10.1** | Xây dựng công cụ kiểm tra và chuẩn hóa dữ liệu Excel từ BQT | Sơn | 02/11/2026 | **In Progress** | **High** | Cung cấp file Excel mẫu; nếu file gốc lỗi nặng thì chuyển sang nhập tay trực tiếp từng tầng. |
| `RSK-11` | **T-11.1** | Tách biệt hoàn toàn bảng dữ liệu và logic tính toán phí bắt buộc vs tự nguyện | Hoà | 10/11/2026 | **Not Started** | **High** | Kế toán chỉ được phép in biên lai khi đã xác nhận rõ ràng loại phí thu; kiểm tra tuân thủ Thông tư 02/2016. |
| `RSK-13` | **T-13.1** | Cấu hình Transaction Isolation & Optimistic Locking cho chức năng gạch nợ | Hoà, Sơn | 22/11/2026 | **Not Started** | **Medium** | Viết kịch bản kiểm thử tải đồng thời 20 kế toán gạch nợ cùng 1 hóa đơn để bảo đảm không bị duplicate payment. |
| `RSK-12` | **T-12.1** | Thiết kế bảng quan hệ cư dân - căn hộ N:N với ràng buộc khóa duy nhất CCCD | Sơn | 31/10/2026 | **Complete** | **Medium** | Đã hoàn thiện thiết kế ERD logic, hỗ trợ 1 cư dân đăng ký nhiều phòng. |
| `RSK-04` | **T-04.1** | Tổ chức demo Sprint Review định kỳ với Kế toán và Trưởng BQT BlueMoon | Khánh | 16/11/2026 | **In Progress** | **Medium** | Đã thiết lập kênh Zalo; tổ chức họp offline tại sảnh tòa nhà sau Sprint 2. |
| `RSK-09` | **T-09.1** | Rà soát Story Points và áp dụng hệ số dự phòng 20% thời gian cho Sprint 3 | Khánh, Hoà | 15/11/2026 | **In Progress** | **Medium** | Nếu phân hệ gạch nợ phức tạp, ưu tiên chuyển tính năng xuất biểu đồ v2.0 về sau. |
| `RSK-01` | **T-01.1** | Phân chia công việc theo cặp (Pair work), lưu tài liệu thiết kế lên GitHub Wiki | Cả nhóm | 15/12/2026 | **In Progress** | **Low** | Duy trì Daily Standup 15 phút hàng ngày để kịp thời hỗ trợ chéo. |
| `RSK-02` | **T-02.1** | Chốt biên bản bàn giao danh mục 11 User Stories cố định cho phiên bản v1.0 | Khánh | 28/10/2026 | **Complete** | **Low** | BQT đã ký xác nhận danh mục phạm vi v1.0 trong Project Charter. |
| `RSK-03` | **T-03.1** | Định kỳ kiểm tra chất lượng tài liệu thiết kế và API docs sau mỗi Sprint | Huy | 05/12/2026 | **In Progress** | **Medium** | QA Lead từ chối nghiệm thu task nếu chưa đính kèm tài liệu kỹ thuật cập nhật. |
| `RSK-05` | **T-05.1** | Kiểm soát tiến độ theo biểu đồ Gantt Chart, xử lý dứt điểm nợ kỹ thuật tuần | Khánh | 08/12/2026 | **In Progress** | **Low** | Báo cáo tiến độ hàng tuần trong cuộc họp giao ban nội bộ. |
| `RSK-07` | **T-07.1** | Cùng Kế toán BQT thực hiện ca kiểm thử UAT trên dữ liệu mô phỏng trước bàn giao | Huy, Khánh | 10/12/2026 | **Not Started** | **Low** | Biên bản UAT có chữ ký chấp thuận của Kế toán trưởng trước lễ bảo vệ bài tập lớn. |
| `RSK-08` | **T-08.1** | Khai thác tối đa hạ tầng máy tính cá nhân và dịch vụ máy chủ đám mây miễn phí | Hoà | 15/12/2026 | **Complete** | **Low** | Triển khai trên Docker Container nội bộ, không phát sinh chi phí duy trì. |
