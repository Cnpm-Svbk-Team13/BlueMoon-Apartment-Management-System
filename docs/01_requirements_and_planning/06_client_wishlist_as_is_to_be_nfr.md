# CLIENT'S WISH LIST, KỊCH BẢN HIỆN TRẠNG (AS-IS) / TƯƠNG LAI (TO-BE) VÀ YÊU CẦU PHI CHỨC NĂNG (NFR)
## Dự án: Phần mềm Quản lý Chung cư BlueMoon (BMS)
### Tài liệu nguồn: `CNPM_Team13_ClientWishList.pdf`, `CNPM_Team13_AsIsScenario.pdf`, `CNPM_Team13_RawNFR.pdf` (Team 13)

---

## PHẦN 1: CLIENT'S WISH LIST (32 YÊU CẦU THÔ NGUYÊN VĂN)

| Mã YC | Lời trích dẫn yêu cầu thô (Voice of Stakeholder) | Mức độ ưu tiên | Nguồn thu thập | Phân loại | Ghi chú & Định hướng phân tích |
|:---:|---|:---:|:---:|:---:|---|
| **RAW-001** | "Chị cần phần mềm lưu được thông tin cơ bản của căn hộ như mã căn, tầng, diện tích vuông và xem nó là kiot hay căn hộ ở, để sau này nhân đơn giá cho chuẩn." | **Cao** | Kế toán BQT | Quản lý Căn hộ | Cần quản lý danh mục căn hộ (mã căn, tầng, diện tích, loại căn hộ) làm cơ sở tính phí. |
| **RAW-002** | "Phải có chỗ khai báo hồ sơ chủ nhà, ghi rõ họ tên, số điện thoại, CCCD và email để BQT gọi khi có việc." | **Cao** | Trưởng BQT | Quản lý Căn hộ | Quản lý thông tin hồ sơ chủ hộ (Họ tên, SĐT, CCCD, Email). |
| **RAW-003** | "Mỗi căn hộ phải thêm được danh sách những người đang thực tế sinh sống ở trong nhà đó." | **Cao** | Trưởng BQT | Quản lý Nhân khẩu | Quản lý danh sách nhân khẩu thực tế sinh sống theo từng căn hộ. |
| **RAW-004** | "Trong danh sách người ở, nhớ cho chọn quan hệ với chủ hộ là vợ chồng, con cái hay chỉ là người thuê nhà." | **Cao** | Trưởng BQT | Quản lý Nhân khẩu | Ghi nhận mối quan hệ với chủ hộ (vợ/chồng, con, người thuê...). |
| **RAW-005** | "Phải có ô nhập số thẻ CCCD, ngày cấp, nơi cấp của những người đã đủ tuổi làm căn cước." | **Cao** | Trưởng BQT | Quản lý Nhân khẩu | Lưu trữ chi tiết CCCD/Định danh của cư dân đủ tuổi. |
| **RAW-006** | "Quan trọng nhất là theo dõi được người này là thường trú, tạm trú hay tạm vắng. Nếu tạm trú/vắng thì phải ghi rõ từ ngày nào đến ngày nào để công an kiểm tra." | **Cao** | Kế toán BQT | Quản lý Nhân khẩu | Quản lý trạng thái cư trú (thường trú, tạm trú, tạm vắng) và thời hạn. |
| **RAW-007** | "Làm sao nhập ngày sinh vào thì phần mềm tự lọc ra đâu là trẻ em, đâu là người cao tuổi để Tổ dân phố tiện rà soát tặng quà Trung thu, mùng 1/6." | **Trung bình** | Đại diện Tổ dân phố | Quản lý Nhân khẩu | Tự động phân loại độ tuổi cư dân (trẻ em, người cao tuổi) phục vụ công tác dân phố. |
| **RAW-008** | "Nếu ấn đính kèm được ảnh chụp CCCD hoặc hợp đồng thuê nhà thẳng vào hồ sơ người đó luôn thì quá tốt, đỡ phải tìm giấy tờ." | **Thấp** | Kế toán BQT | Quản lý Nhân khẩu | Đính kèm/Lưu trữ tệp tin hình ảnh giấy tờ (CCCD, hợp đồng thuê nhà). |
| **RAW-009** | "Cuối tháng chú làm sao xuất được danh sách cư dân theo từng tầng hoặc cả tòa nhà ra file Excel hay PDF để anh in đi nộp cho công an phường." | **Cao** | Trưởng BQT | Báo cáo & Thống kê | Xuất báo cáo danh sách cư dân (Excel/PDF) theo tầng/tòa nhà phục vụ báo cáo cơ quan chức năng. |
| **RAW-010** | "Chị muốn có một cái thanh tìm kiếm. Chị gõ tên hay số điện thoại phát là nó hiện ra ngay nhà đó ở căn nào cho nhanh, chứ tìm tay bằng Excel mờ cả mắt." | **Cao** | Kế toán BQT | Tra cứu & Tìm kiếm | Tìm kiếm nhanh cư dân theo tên, SĐT để tra cứu số căn hộ. |
| **RAW-011** | "Bên tôi cần có chỗ nhập và lưu các loại phí bắt buộc như Phí dịch vụ chung cư và Phí quản lý chung cư để hàng tháng tính tiền." | **Cao** | BQT (Thủ quỹ) | Cấu hình Phí | Quản lý danh mục các khoản phí dịch vụ và phí quản lý chung cư. |
| **RAW-012** | "Tiền phí dịch vụ với phí quản lý phải tự động lấy diện tích nhà nhân với đơn giá, chứ bắt ngồi nhân tay từng phòng như Excel bây giờ lâu lắm." | **Cao** | Thủ quỹ | Quản lý Thu phí | Tự động tính phí quản lý và dịch vụ dựa trên diện tích căn hộ và đơn giá. |
| **RAW-013** | "Phí dịch vụ chung cư có nhiều mức giá khác nhau tùy loại căn hộ, nên hệ thống phải cho phép BQL thiết lập đơn giá riêng cho từng đợt hoặc từng diện tích." | **Cao** | Ban quản trị | Cấu hình Phí | Linh hoạt thiết lập đơn giá phí dịch vụ theo loại căn hộ hoặc từng đợt áp dụng. |
| **RAW-014** | "Phí quản lý hiện áp dụng từ 7.000đ/m², nhưng sau này có thể điều chỉnh theo quyết định Hội nghị nhà chung cư nên hệ thống phải cho sửa linh hoạt đơn giá này." | **Cao** | Thủ quỹ | Cấu hình Phí | Cho phép thay đổi, điều chỉnh đơn giá phí quản lý chung cư linh hoạt theo thời gian. |
| **RAW-015** | "Đến đầu tháng phần mềm phải tự tính toán và lập danh sách tổng tiền phí dịch vụ và phí quản lý từng nhà cần nộp rồi hiển thị/in ra cho tôi biết đường thu." | **Cao** | Thủ quỹ | Quản lý Thu phí | Tự động tổng hợp và lập bảng kê thông báo phí hàng tháng cho từng căn hộ. |
| **RAW-016** | "Khi căn hộ lên nộp tiền phí dịch vụ/quản lý, tôi bấm thu tiền và đổi trạng thái thành 'Đã nộp' để còn biết nhà nào nộp rồi, nhà nào chưa nộp." | **Cao** | Thủ quỹ | Quản lý Thu phí | Ghi nhận thanh toán và cập nhật trạng thái nộp tiền phí của căn hộ. |
| **RAW-017** | "Nhiều khi cư dân cãi bảo nộp tiền tháng này rồi, tôi muốn gõ số phòng cái là tra ra ngay lịch sử nhà đấy đã nộp tiền phí dịch vụ/quản lý chưa, nộp ngày nào." | **Cao** | Ban quản trị | Tra cứu & Tìm kiếm | Tra cứu lịch sử đóng phí cố định chi tiết theo từng căn hộ. |
| **RAW-018** | "Cuối tháng BQL cần xuất báo cáo tổng số tiền phí dịch vụ và phí quản lý đã thu được thực tế để đối soát quỹ vận động tòa nhà." | **Trung bình** | Ban quản trị | Báo cáo & Thống kê | Xuất báo cáo doanh thu phí dịch vụ/quản lý thực tế thu được định kỳ. |
| **RAW-019** | "Cần xem được danh sách các hộ còn nợ phí dịch vụ và phí quản lý hàng tháng để BQL gửi giấy nhắc nợ hoặc xử lý." | **Cao** | Ban quản trị | Quản lý Công nợ | Tổng hợp và quản lý danh sách căn hộ nợ phí để phục vụ đôn đốc thu nợ. |
| **RAW-020** | "Thu tiền phí xong phải cho in được phiếu thu hoặc biên lai xác nhận nộp phí dịch vụ/quản lý đưa cho dân làm bằng chứng." | **Trung bình** | Thủ quỹ | Quản lý Thu phí | In phiếu thu / biên lai xác nhận nộp phí cố định đưa cho cư dân. |
| **RAW-021** | "Hệ thống cần cho phép tạo linh hoạt từng đợt vận động ủng hộ từ thiện mới (nhập tên quỹ, thời gian phát động, mục đích quyên góp) mà không ảnh hưởng đến cấu hình các loại phí cố định." | **Cao** | Tổ trưởng TDP & BQT (Phỏng vấn) | Quản lý Quyên góp | Quản lý tạo và thiết lập các đợt vận động quyên góp/từ thiện độc lập. |
| **RAW-022** | "Đóng góp từ thiện là tùy tâm của từng gia đình, hệ thống tuyệt đối không được tự động áp mức thu tối thiểu hay áp công thức nhân theo diện tích m² sàn như phí dịch vụ." | **Cao** | Cư dân (Khảo sát Google Forms) | Quy tắc Nghiệp vụ | Quy định khoản đóng góp quyên góp mang tính tự nguyện, không được gán định mức/công thức tự động. |
| **RAW-023** | "Khi cư dân nộp tiền ủng hộ quỹ, tôi muốn ghi nhận nhanh số tiền vào căn hộ đó, chọn hình thức tiền mặt hoặc chuyển khoản và in ngay Phiếu xác nhận/Phiếu thu ủng hộ để đưa cho họ." | **Cao** | Kế toán / Thu ngân BQT | Quản lý Quyên góp | Ghi nhận tiền đóng góp tự nguyện (tiền mặt/chuyển khoản) và in phiếu thu/xác nhận ủng hộ. |
| **RAW-024** | "Cần có giải pháp giải quyết tranh cãi giữa BQT và Cư dân: Phiếu thông báo thu tiền hàng tháng phải tách riêng Mục A (Phí bắt buộc tính theo m²) và Mục B (Ủng hộ tự nguyện), không được gộp vào tổng nợ bắt buộc." | **Cao** | Khảo sát đại diện cư dân & BQT | Quản lý Thu phí | Phân tách rõ ràng giữa Phí bắt buộc (Mục A) và Ủng hộ tự nguyện (Mục B) trên Thông báo thu tiền. |
| **RAW-025** | "Cư dân không nộp tiền quỹ tự nguyện thì không được coi là nợ xấu hay nợ đọng của tòa nhà, không được áp dụng các biện pháp nhắc nợ hay phạt trả chậm." | **Trung bình** | Cư dân các tầng & Tổ phó TDP | Quy tắc Nghiệp vụ | Không tính nợ xấu hay phạt đọng đối với khoản đóng góp quỹ tự nguyện chưa nộp. |
| **RAW-026** | "Mỗi khi kết thúc đợt quyên góp, tôi cần phần mềm xuất ra danh sách tổng kết toàn bộ các căn hộ đã đóng góp, số tiền ủng hộ từng hộ và tổng số tiền thu được để ký biên bản bàn giao và dán công khai ở sảnh." | **Cao** | Tổ trưởng Tổ dân phố | Báo cáo & Thống kê | Xuất danh sách tổng kết đợt quyên góp (chi tiết theo hộ và tổng tiền) phục vụ công khai và bàn giao. |
| **RAW-027** | "Hệ thống phải có bộ lọc thống kê đa chiều: xem tổng số tiền quỹ thu được theo từng tầng, theo từng đợt vận động hoặc theo khoảng thời gian để tiện đối soát tiền mặt với ngân quỹ." | **Trung bình** | Kế toán BQT | Báo cáo & Thống kê | Cung cấp bộ lọc thống kê thu quỹ theo tầng, đợt vận động, khoảng thời gian để đối soát quỹ. |
| **RAW-028** | "BQT cần công khai minh bạch số tiền quỹ thu được, có thể ẩn số CCCD hay thông tin cá nhân nhạy cảm trên danh sách công khai để đảm bảo quyền riêng tư nhưng vẫn kiểm tra được số tiền căn hộ mình đóng." | **Cao** | Cư dân (Khảo sát Google Forms) | Bảo mật & Quyền riêng tư | Hỗ trợ tính năng ẩn thông tin cá nhân nhạy cảm (CCCD) trên danh sách công khai. |
| **RAW-029** | "Chung cư cần quản lý danh sách phương tiện gửi tại tầng hầm theo từng căn hộ (xe máy 70.000 đ/tháng, ô tô 1.200.000 đ/tháng) và tính tự động vào tiền gửi xe hàng tháng." | **Thấp** | BQT & Đại diện cư dân (Khảo sát) | Quản lý Phương tiện | Quản lý phương tiện gửi và tự động tính phí gửi xe hàng tháng vào bảng kê. *(Roadmap v2.0)* |
| **RAW-030** | "Mong muốn tương lai phần mềm hỗ trợ thu hộ tiền Điện, Nước, Internet từng tháng theo file danh sách đối soát từ Công ty Điện lực/Nước sạch để cư dân không phải nộp nhiều nơi." | **Thấp** | Kế toán BQT (Phỏng vấn) | Tích hợp & Thu hộ | Tích hợp nhập/đối soát dữ liệu điện, nước, internet từ đối tác để thu hộ. *(Roadmap v2.0)* |
| **RAW-031** | "Mong muốn quét mã QR chuyển khoản tự động gạch nợ trên phần mềm thay vì kế toán phải kiểm tra sao kê ngân hàng rồi bấm gạch nợ bằng tay." | **Cao** | Cư dân trẻ & BQT (Khảo sát) | Thanh toán Số | Tích hợp thanh toán QR Code và tự động gạch nợ trên phần mềm. *(Roadmap v2.0)* |
| **RAW-032** | "Mong muốn có ứng dụng di động hoặc cổng tra cứu trực tuyến để cư dân tự xem chi tiết các khoản phí và số tiền quỹ mình đã đóng bất kỳ lúc nào." | **Trung bình** | Cư dân (Khảo sát Google Forms) | Kênh Cư dân | Phát triển ứng dụng Mobile / Portal cho cư dân tra cứu thông tin phí và quỹ. *(Roadmap v2.0)* |

---

## PHẦN 2: KỊCH BẢN HIỆN TRẠNG (AS-IS SCENARIO MAP)

| Nghiệp vụ | Bước thực hiện | Cách làm hiện tại | Pain Points / Rủi ro phát sinh |
|---|---|---|---|
| **1. Quản lý Nhân khẩu & Cư trú** | Tiếp nhận & Nhập liệu | Cư dân nộp giấy photo giờ HC. Nhân viên ghi sổ tay + mở Excel tìm thủ công (Ctrl+F) để nhập liệu. | Phụ thuộc giờ hành chính. Phải nhập 2 lần, dễ sai chính tả, không chuẩn hóa dữ liệu. |
| | Lưu trữ & Theo dõi | Lưu trên file Excel cục bộ. Khi chuyển đi thì xóa dòng. Theo dõi ngày tạm trú bằng mắt. | Xóa dòng làm mất trắng lịch sử (không Audit log). Không có cảnh báo hết hạn, dễ bị phạt. |
| | Báo cáo Công an | Lọc tay trên Excel, copy/paste sang file mới rồi in ra. | Rà soát bằng mắt tốn nhiều giờ, dễ sót dữ liệu, định dạng lộn xộn mất uy tín. |
| **2. Quản lý Thu phí** | Tính phí hàng tháng | Xem Quyết định, tra diện tích từ Hộ khẩu, dùng Excel nhân tay với đơn giá. | Dễ nhầm lẫn định mức, nhập tay dễ sai, công thức Excel dễ bị đè hỏng. |
| | Thông báo & Thu tiền | In giấy báo gửi từng nhà. Thu tiền mặt, xé biên lai giấy viết tay. | Cư dân hay làm mất giấy báo. Xé biên lai chậm, dễ thất lạc sổ sách, không lưu vết người thu. |
| | Tổng hợp Công nợ | Cuối ngày cầm biên lai đối chiếu đánh dấu "Đã nộp" vào sổ/Excel. | Cập nhật chậm trễ; rất khó tổng hợp danh sách các hộ nợ dai dẳng qua nhiều tháng. |
| **3. Quản lý Quỹ tự nguyện** | Vận động & Thu tiền | Dán giấy A4 ở sảnh. Cán bộ cầm sổ bìa cứng gõ cửa từng nhà buổi tối thu tiền mặt. | Tốn sức, tỷ lệ gặp cư dân thấp ($< 60\%$), làm phiền giờ nghỉ ngơi, cư dân ngại chuyển khoản cá nhân. |
| | Ghi nhận nợ | Kế toán BQT cộng gộp tiền quỹ tự nguyện vào tổng tiền nợ trên phiếu báo thu phí bắt buộc. | **Gây bức xúc lớn:** Cư dân cho là "ép buộc", dẫn tới cãi vã, kế toán rối loạn theo dõi công nợ. |
| | Tổng kết & Báo cáo | Bấm máy tính Casio cộng dồn sổ tay. Mất 3-5 ngày gõ lại Excel rồi dán bản giấy ở sảnh. | Rủi ro sai số/thất thoát. Chậm trễ công bố, bản giấy dán dễ bị rách mờ, cư dân không tiện tra cứu. |

---

## PHẦN 3: KỊCH BẢN KỲ VỌNG (TO-BE SCENARIO MAP)

| Nghiệp vụ | Bước thực hiện | Giải pháp Hệ thống | Lợi ích |
|---|---|---|---|
| **1. Quản lý Nhân khẩu & Cư trú** | Tiếp nhận & Nhập liệu | Nhập liệu 1 lần trên Desktop App. Hệ thống tự động quét chặn trùng lặp số CCCD toàn tòa nhà. | Dữ liệu chuẩn hóa 100%, chặn đứng tình trạng rác dữ liệu, giảm áp lực nhân viên văn phòng. |
| | Lưu trữ & Theo dõi | Dữ liệu lưu tập trung CSDL. Có lịch sử lưu vết (Audit log) vĩnh viễn và cảnh báo ngày hết hạn tạm trú. | An toàn tuyệt đối. Quản lý chặt thời hạn cư trú, đáp ứng ngay thanh tra công an không sợ sót. |
| | Báo cáo Công an | Chọn bộ lọc trên giao diện (Tầng/Trạng thái), 1 click kết xuất ra file Excel/PDF chuẩn hành chính. | Hoàn thành trong 3 giây. Số liệu chính xác tuyệt đối, form mẫu đúng quy chuẩn pháp lý. |
| **2. Quản lý Thu phí** | Tính phí hàng tháng | Khai báo 1 lần danh mục đơn giá. Hệ thống tự động lấy diện tích nhân đơn giá xuất ra bảng kê toàn tòa nhà. | Tự động hóa 100% khâu tính toán, tiết kiệm hàng chục giờ đồng hồ mỗi tháng, không sai số. |
| | Thông báo & Thu tiền | Gõ số phòng tìm ra ngay. Bấm gạch nợ và hệ thống tự động xuất/in Phiếu thu dạng PDF tức thì. | Gạch nợ chỉ mất vài giây/hộ. Bỏ biên lai giấy, chứng từ minh bạch, lưu vết chính xác tài khoản thu. |
| | Tổng hợp Công nợ | Dashboard hiển thị Real-time. Xuất nhanh báo cáo dòng tiền và danh sách hộ nợ phí bằng 1 click. | BQT nắm bức tranh tài chính chính xác từng phút; ra thông báo nhắc nợ kịp thời. |
| **3. Quản lý Quỹ tự nguyện** | Vận động & Thu tiền | Khởi tạo chiến dịch trên app. Ghi nhận tiền linh hoạt (chọn tên quỹ, số tiền, hình thức nộp) $\rightarrow$ In phiếu thu độc lập. | Quy trình chuyên nghiệp, minh bạch chứng từ ngay lập tức khi cư dân nộp tiền. |
| | Ghi nhận nợ | Tách bạch rõ trên UI & Giấy báo: Mục A (Bắt buộc tự tính nợ) và Mục B (Tự nguyện để trống, không cộng dồn vào nợ xấu). | **Giải quyết triệt để xung đột:** Khẳng định sự tôn trọng tính tùy tâm, kế toán không bị nhầm lẫn sổ sách. |
| | Tổng kết & Báo cáo | Hệ thống xuất báo cáo "Public Mode" (ẩn CCCD/SĐT cư dân) để dán bảng tin hoặc gửi group Zalo. | Báo cáo tức thì sau khi đóng đợt thu. Cư dân yên tâm vì sự minh bạch và được bảo vệ quyền riêng tư. |

---

## PHẦN 4: YÊU CẦU PHI CHỨC NĂNG THÔ (RAW NFR TABLE)

| Mã NFR | Nhóm Yêu cầu | Mô tả Yêu cầu | Tiêu chí đo lường (Metrics) | Ưu tiên |
|:---:|---|---|---|:---:|
| **NFR-01** | Hiệu năng & Chịu tải | Hệ thống phản hồi nhanh chóng, hoạt động mượt mà ngay cả trong giờ cao điểm (đầu tháng/cuối tháng) với quy mô dữ liệu lớn (5 năm, $\ge 500$ hộ). | • Hỗ trợ $\ge 200$ user truy cập đồng thời giờ cao điểm.<br>• Chuyển trang / Tra cứu tìm kiếm: $\le 2 - 3$ giây.<br>• Tạo kỳ thu phí hàng tháng toàn tòa nhà: $\le 30$ giây.<br>• Kết xuất báo cáo thống kê (5 năm dữ liệu): $\le 10$ giây. | **Cao** |
| **NFR-02** | Bảo mật, Phân quyền & Pháp lý | Bảo vệ nghiêm ngặt dữ liệu cá nhân cư dân và số liệu tài chính; tuân thủ tuyệt đối quy định pháp luật Việt Nam. | • Mật khẩu băm (`bcrypt`/`PBKDF2`); khóa tài khoản sau 5 lần đăng nhập sai.<br>• Phân quyền (RBAC) rõ ràng (Trưởng ban, Kế toán...).<br>• Kết nối CSDL mã hóa TLS.<br>• Pháp lý: Tuân thủ Nghị định 13/2023/NĐ-CP về bảo vệ dữ liệu cá nhân (chỉ người có quyền mới được xem/xuất dữ liệu). | **Rất cao** |
| **NFR-03** | Toàn vẹn & Chính xác dữ liệu | Số liệu tiền bạc và thông tin cư dân phải đảm bảo chính xác tuyệt đối, không được phép sai số hay thất thoát dữ liệu ngầm. | • Thuật toán tính phí đúng 100% khi test với Data mẫu.<br>• Tiền tệ bắt buộc dùng kiểu `DECIMAL` (không dùng Float/Double).<br>• Ghi nhận nộp tiền phải dùng DB Transaction (đảm bảo rollback nếu lỗi).<br>• Có Audit Trail (lưu vết) cho mọi thao tác Sửa/Xóa. | **Rất cao** |
| **NFR-04** | Khả dụng & Trải nghiệm (UI/UX) | Giao diện trực quan, thuần Việt, thiết kế thân thiện cho người dùng không chuyên IT hoặc người lớn tuổi (BQT, Tổ dân phố). | • Font chữ $\ge 12\text{pt}$; thông báo lỗi rõ ràng bằng tiếng Việt.<br>• User mới tự thao tác được luồng chính sau $\le 30$ phút hướng dẫn.<br>• Ghi nhận khoản nộp phí thành công trong tối đa $\le 3$ thao tác click chuột.<br>• Điểm hài lòng (System Usability Scale - SUS) khi UAT $\ge 70$. | **Cao** |
| **NFR-05** | Tin cậy, Bảo trì & Khả năng mở rộng | Hệ thống vận hành ổn định, an toàn dữ liệu và có kiến trúc sẵn sàng mở rộng các tính năng mới (điện, nước, gửi xe) ở phiên bản v2.0. | • Uptime $\ge 99\%$ trong giờ hành chính.<br>• Tự động sao lưu (Backup) MySQL hàng ngày.<br>• Phục hồi dữ liệu (Restore) thử nghiệm $\le 1$ giờ; $\text{RPO} \le 24$ giờ.<br>• Kiến trúc phân tầng rõ ràng (UI - Service - DAO); tương thích môi trường Windows 10+ / Java LTS. | **Cao** |
