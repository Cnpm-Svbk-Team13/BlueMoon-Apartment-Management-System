# KỊCH BẢN HIỆN TRẠNG (AS-IS) VÀ VIỄN CẢNH TƯƠNG LAI (VISIONARY / TO-BE)
## Dự án: Phần mềm Quản lý Thu phí Chung cư BlueMoon

---

## 1. Kịch bản Hiện trạng (As-Is Scenario)

### 1.1. Bối cảnh và Mô tả thực trạng
Hiện tại, toàn bộ công tác quản lý dữ liệu dân cư và thu nộp các khoản phí tại tòa chung cư BlueMoon (30 tầng) đang được Ban quản trị thực hiện theo **phương thức bán thủ công**:
- **Dữ liệu dân cư:** Được ghi chép trong sổ hộ khẩu giấy hoặc lưu trên một vài file Microsoft Excel riêng lẻ trên máy tính cá nhân của Trưởng ban quản trị. Khi có biến động (cư dân mới đến, sinh con, chuyển đi, khách thuê trọ tạm trú), việc cập nhật diễn ra không nhất quán, file Excel bị phân mảnh qua nhiều phiên bản (`Danh_sach_cu_dan_2023_v1.xlsx`, `v2_sua.xlsx`).
- **Thu phí định kỳ (Phí dịch vụ, Phí quản lý):** Hàng tháng, kế toán phải mở file Excel, tự gõ công thức nhân diện tích từng phòng với đơn giá, in thông báo giấy và bỏ vào hòm thư từng căn hộ.
- **Thu các khoản đóng góp tự nguyện (Quỹ vì người nghèo, biển đảo, đền ơn đáp nghĩa...):** Được thực hiện hoàn toàn trên giấy tờ viết tay (như mẫu chụp tại **Hình 1-1** trong đề bài). Người nộp ký tên và ghi số tiền trực tiếp vào sổ giấy.
- **Thanh toán và Gạch nợ:** Cư dân xuống nộp tiền mặt tại văn phòng BQT. Thủ quỹ kiểm đếm tiền, viết biên lai giấy xé từ cuốn hóa đơn bán lẻ, sau đó cuối ngày mới mở file Excel ra đánh dấu thủ công.

### 1.2. Bảng phân tích chi tiết quy trình hiện tại (As-Is Process Matrix)

| STT | Bước quy trình | Ai thực hiện | Công cụ hiện tại | Dữ liệu Đầu vào | Dữ liệu Đầu ra | Điểm đau & Rủi ro (Pain Points) |
|:---:|---|---|---|---|---|---|
| **P1** | Tiếp nhận & Đăng ký cư dân mới | Trưởng BQT / Thư ký | Sổ giấy + File Excel cá nhân | Hợp đồng mua bán / thuê nhà, CCCD bản photo | Dòng ghi chú mới trên file Excel | - Dữ liệu dễ mất mát nếu hỏng ổ cứng máy tính.<br>- Không có lịch sử biến động (ai sửa, sửa khi nào).<br>- Trùng lặp thông tin giữa các file. |
| **P2** | Khai báo tạm trú, tạm vắng | Cư dân & Ban quản trị | Giấy viết tay, liên hệ trực tiếp | Thông tin cá nhân người tạm trú/tạm vắng | Giấy xác nhận tạm trú viết tay | - Khó theo dõi ngày hết hạn tạm trú.<br>- Khi Công an khu vực kiểm tra đột xuất, mất hàng giờ để lục tìm hồ sơ. |
| **P3** | Tính toán bảng kê thu phí tháng | Kế toán BQT | Microsoft Excel thủ công | Diện tích căn hộ, đơn giá phí dịch vụ, phí quản lý | Bảng tính tổng tiền từng căn hộ | - Dễ sai lệch công thức Excel nếu vô tình kéo chuột nhầm dòng.<br>- Tốn nhiều giờ tính toán cho toàn bộ 30 tầng căn hộ. |
| **P4** | Phát động & Thu các quỹ đóng góp | Tổ dân phố & Thủ quỹ BQT | Sổ danh sách viết tay kẻ ô (như Hình 1-1) | Tên chủ hộ, địa chỉ phòng, số tiền ủng hộ tùy tâm | Dòng ghi chép viết tay + Chữ ký người nộp | - **Rất khó đọc chữ viết tay và số tiền** (như thực tế Hình 1-1).<br>- Nguy cơ rách, ướt, thất lạc sổ giấy thu quỹ.<br>- Tính tổng quỹ bằng máy tính bấm tay rất dễ nhầm lẫn. |
| **P5** | Thu tiền tại quầy và cấp biên lai | Thủ quỹ BQT | Tiền mặt, Cuống biên lai giấy xé lẻ | Số phòng, tiền mặt do cư dân nộp | Biên lai giấy giao cho khách, cuống lưu | - Phải lật tìm số phòng thủ công trên nhiều trang sổ.<br>- Tốc độ phục vụ chậm, cư dân phải xếp hàng giờ cao điểm.<br>- Viết tay biên lai tốn thời gian và dễ ghi nhầm số tiền. |
| **P6** | Gạch nợ & Đối soát cuối ngày | Kế toán + Thủ quỹ | Cuống biên lai giấy + File Excel | Cuống hóa đơn giấy thu trong ngày | Cột ghi chú "Đã đóng" trên Excel | - Nguy cơ lệch tiền giữa số tiền mặt thực tế và số tiền ghi trên sổ.<br>- Dễ bỏ sót việc gạch nợ khiến cư dân bị đòi nợ oan tháng sau. |
| **P7** | Tổng hợp báo cáo thu chi & Công nợ | Kế toán BQT | Excel, máy tính bỏ túi | Các cột thu trong tháng trên Excel | Bảng báo cáo thu nộp gửi BQT | - Mất từ 2 đến 3 ngày để cộng dồn số liệu và lập báo cáo.<br>- Không thể xem báo cáo tức thời theo thời gian thực (real-time). |

---

## 2. Kịch bản Viễn cảnh Tương lai (Visionary / To-Be Scenario)

### 2.1. Bức tranh kỳ vọng toàn diện khi đưa Phần mềm vào vận hành
Hệ thống **BlueMoon BFMS** sẽ thay thế toàn bộ quy trình giấy tờ và bảng tính phân mảnh bằng một nền tảng quản trị tập trung, minh bạch và an toàn:
1. **Dữ liệu dân cư số hóa toàn diện:** Mọi căn hộ, hộ khẩu, nhân khẩu và biến động cư trú được lưu trữ trên cơ sở dữ liệu tập trung, truy xuất ngay lập tức khi cơ quan công an kiểm tra.
2. **Tự động hóa tính phí chỉ với một cú nhấp chuột:** Hệ thống tự động nhân diện tích căn hộ sở hữu với đơn giá phí dịch vụ và phí quản lý để tạo bảng công nợ hàng tháng cho toàn bộ cư dân trong vài giây.
3. **Quy trình thu phí một cửa tại quầy nhanh chóng:** Thủ quỹ chỉ cần nhập số phòng hoặc tên chủ hộ, toàn bộ các khoản nợ phí và các đợt quỹ tự nguyện sẽ hiển thị trực quan. Xác nhận thanh toán, in biên lai điện tử và tự động gạch nợ ngay lập tức.
4. **Báo cáo và Dashboard thời gian thực:** Trưởng ban quản trị và kế toán có thể mở biểu đồ thống kê để xem ngay tỷ lệ thu nộp của từng tầng, từng loại quỹ mà không cần chờ tính toán thủ công.
5. **Định hướng mở rộng v2.0 mượt mà:** Sẵn sàng tích hợp thêm các module mở rộng: tự động tính phí gửi xe máy/ô tô theo biển số đăng ký và nhập dữ liệu thu hộ tiền điện, nước, internet từ các nhà cung cấp.

### 2.2. Bảng mô tả dòng chảy nghiệp vụ tương lai (To-Be Workflow Matrix)

| STT | Bước quy trình tương lai | Ai thực hiện | Hệ thống hỗ trợ gì (Phần mềm BlueMoon) | Lợi ích mong muốn đạt được |
|:---:|---|---|---|---|
| **F1** | Tiếp nhận & Quản lý thông tin cư dân | Ban quản trị | - Giao diện quản lý Hộ khẩu/Nhân khẩu trực quan.<br>- Kiểm tra trùng lặp số CCCD/định danh.<br>- Tự động gắn nhân khẩu vào đúng căn hộ và tầng lầu. | - Dữ liệu cư dân chính xác 100%, không bị phân mảnh.<br>- Cập nhật tức thời, dễ dàng tìm kiếm trong 1 giây. |
| **F2** | Khai báo & Theo dõi tạm trú / tạm vắng | Ban quản trị | - Form ghi nhận trạng thái: Thường trú, Tạm trú, Tạm vắng.<br>- Lưu trữ thời hạn (từ ngày - đến ngày), lý do và nơi chuyển đi/đến.<br>- Cảnh báo sắp hết hạn tạm trú. | - Tuân thủ nghiêm ngặt Luật Cư trú.<br>- Trích xuất danh sách báo cáo nộp Công an phường ngay lập tức khi có yêu cầu. |
| **F3** | Cấu hình & Phát sinh bảng phí hàng tháng | Kế toán BQT | - Cho phép thiết lập đơn giá phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$) và phí quản lý ($\ge 7.000\text{ đ}/m^2$).<br>- Nút bấm **"Tạo đợt thu phí tháng"** tự động tính số tiền cho toàn bộ căn hộ dựa trên diện tích đã đăng ký. | - Tiết kiệm $95\%$ thời gian tính toán của kế toán.<br>- Loại bỏ hoàn toàn lỗi tính nhầm số tiền hoặc bỏ sót căn hộ. |
| **F4** | Khởi tạo đợt quyên góp quỹ tự nguyện | Ban quản trị / Tổ dân phố | - Tạo chiến dịch vận động (Quỹ người nghèo, Quỹ biển đảo, Khuyến học...) với thời gian bắt đầu, kết thúc.<br>- Cho phép ghi nhận số tiền đóng góp linh hoạt theo từng hộ dân. | - Thay thế triệt để các tờ giấy thu quỹ viết tay thủ công (như Hình 1-1).<br>- Số tiền quyên góp được ghi nhận minh bạch, công khai. |
| **F5** | Thu tiền tại quầy & Gạch nợ tự động | Thủ quỹ BQT | - Màn hình thu ngân thông minh: Gõ số phòng $\rightarrow$ hiển thị đầy đủ công nợ phí dịch vụ, quản lý và các quỹ từ thiện.<br>- Tích chọn khoản thanh toán $\rightarrow$ bấm **"Xác nhận nộp tiền"**.<br>- Hệ thống tự động gạch nợ và cập nhật trạng thái "Đã thanh toán" tức thì. | - Thao tác thu tiền hoàn tất trong vòng dưới 30 giây.<br>- Không lo thất thoát tiền hay quên gạch nợ. |
| **F6** | In/Xuất biên lai thu phí | Thủ quỹ BQT | - Tự động tạo mã biên lai duy nhất, hiển thị chi tiết số tiền, người nộp, người thu, ngày giờ thu.<br>- Hỗ trợ in trực tiếp ra máy in hóa đơn hoặc xuất file PDF gửi cư dân. | - Cư dân nhận được chứng từ rõ ràng, chuyên nghiệp.<br>- Dễ dàng đối chiếu khi có thắc mắc. |
| **F7** | Tra cứu & Báo cáo thống kê quản trị | Trưởng BQT & Kế toán | - Bộ lọc đa chiều: lọc theo số phòng, theo tầng, theo trạng thái nợ, theo loại phí.<br>- Biểu đồ trực quan và bảng tổng hợp số tiền đã thu, còn nợ, tỷ lệ hoàn thành.<br>- Nút xuất báo cáo Excel / PDF chuẩn mẫu. | - Ban quản trị nắm bắt tình hình tài chính tòa nhà 24/7.<br>- Quyết định điều hành nhanh chóng, chính xác. |
| **F8** | Quản lý phương tiện & Thu phí gửi xe *(Gợi mở v2.0)* | Ban quản lý tòa nhà | - Quản lý số lượng và biển số xe máy, ô tô của từng căn hộ.<br>- Tự động cộng phí gửi xe hàng tháng (xe máy: $70.000\text{ đ}/xe$, ô tô: $1.200.000\text{ đ}/xe$) vào bảng thu phí tổng hợp. | - Quản lý chặt chẽ an ninh bãi đỗ xe và doanh thu trông giữ phương tiện. |
| **F9** | Thu hộ tiền điện, nước, viễn thông *(Gợi mở v2.0)* | Kế toán BQT | - Tiếp nhận hoặc import file số liệu sản lượng, tiền cước từ đơn vị cung cấp dịch vụ.<br>- Thu gộp một lần thuận tiện cho cư dân. | - Cư dân không phải đóng tiền lắt nhắt nhiều nơi; BQT chủ động đối soát với nhà cung cấp. |
| **F10** | Cổng thông tin Cư dân *(Gợi mở v2.0)* | Cư dân BlueMoon | - Cư dân đăng nhập bằng tài khoản căn hộ.<br>- Xem lịch sử nộp tiền, công nợ hiện tại, nhận thông báo họp và gửi phản ánh dịch vụ. | - Nâng cao chỉ số hài lòng của cư dân, hiện đại hóa chung cư BlueMoon thành chung cư thông minh. |
