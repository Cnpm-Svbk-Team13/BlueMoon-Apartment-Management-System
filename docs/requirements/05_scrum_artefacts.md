# CÁC SCRUM ARTEFACTS: EPIC, FEATURE, USER STORY & ACCEPTANCE CRITERIA
## Dự án: Hệ thống Quản lý Thu phí Chung cư BlueMoon

---

## 1. Danh sách Epic và Feature (Chuẩn hóa từ Client's Wish List)

Hệ thống được chuẩn hóa thành **6 Epics** lớn và **15 Features** chi tiết, đáp ứng và vượt mục tiêu **OBJ-05** ($\ge 5\text{ Epics}$ và $\ge 10\text{ Features}$):

| Epic ID | Tên Epic (Epic Name) | Mô tả tóm tắt Epic | Feature ID | Tên Feature (Feature Name) | Mô tả chi tiết Feature |
|:---:|---|---|:---:|---|---|
| **EPIC-01** | **Quản lý Dân cư và Căn hộ** | Quản lý toàn bộ dữ liệu vật lý căn hộ, sổ hộ khẩu, nhân khẩu và các biến động cư trú của cư dân tòa nhà BlueMoon. | **FEAT-01** | Quản lý thông tin căn hộ | Quản lý 30 tầng (kiot, căn hộ, penthouse), diện tích $m^2$, mã phòng, tình trạng sở hữu. |
| | | | **FEAT-02** | Quản lý hộ gia đình & nhân khẩu | Quản lý thông tin chủ hộ, sổ hộ khẩu, danh sách nhân khẩu trong hộ, quan hệ với chủ hộ, CCCD. |
| | | | **FEAT-03** | Quản lý biến động cư trú | Ghi nhận thay đổi nhân khẩu (sinh, tử, chuyển đến/đi), khai báo tạm vắng, tạm trú có thời hạn. |
| | | | **FEAT-04** | Kết xuất hồ sơ cư dân | Xuất danh sách nhân khẩu, tạm trú/tạm vắng phục vụ nộp cho Công an phường và Tổ dân phố. |
| **EPIC-02** | **Quản lý Danh mục Khoản thu & Định mức** | Thiết lập, cấu hình và quản lý các loại phí bắt buộc định kỳ và các đợt phát động quỹ quyên góp tự nguyện. | **FEAT-05** | Cấu hình phí dịch vụ & quản lý | Thiết lập định mức phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$) và phí quản lý ($\ge 7.000\text{ đ}/m^2$) theo diện tích. |
| | | | **FEAT-06** | Quản lý chiến dịch quỹ tự nguyện | Tạo và quản lý các đợt vận động (Quỹ vì người nghèo, biển đảo, khuyến học, người cao tuổi...) theo thời gian. |
| **EPIC-03** | **Quản lý Thu phí và Hóa đơn** | Tự động hóa phát sinh công nợ hàng tháng, tiếp nhận thanh toán tại quầy, gạch nợ và phát hành biên lai thu tiền. | **FEAT-07** | Lập bảng kê thu phí định kỳ | Tự động tính toán số tiền phải nộp cho toàn bộ căn hộ dựa trên diện tích và đơn giá trong tháng. |
| | | | **FEAT-08** | Ghi nhận thu phí tại quầy | Màn hình quầy thu ngân cho phép chọn căn hộ, chọn khoản thu nộp, nhập số tiền thực tế và gạch nợ tức thì. |
| | | | **FEAT-09** | Phát hành và in ấn biên lai | Sinh mã biên lai tự động, in phiếu thu tiền hoặc xuất file PDF đầy đủ chi tiết các khoản đã đóng. |
| **EPIC-04** | **Tra cứu và Báo cáo Thống kê** | Cung cấp công cụ tìm kiếm dữ liệu đa tiêu chí và hệ thống báo cáo tài chính, tiến độ thu nộp trực quan theo thời gian thực. | **FEAT-10** | Tra cứu thông tin & công nợ | Tìm kiếm căn hộ, chủ hộ, tra cứu tình trạng nợ phí, lịch sử thu tiền theo số phòng hoặc thời gian. |
| | | | **FEAT-11** | Báo cáo thống kê thu phí & quỹ | Báo cáo doanh thu phí dịch vụ/quản lý theo tháng, tỷ lệ nộp, bảng tổng hợp đóng góp quỹ tự nguyện theo đợt. |
| **EPIC-05** | **Quản trị Hệ thống và Bảo mật** | Đảm bảo an ninh truy cập, xác thực danh tính, phân quyền chức năng và giám sát nhật ký hoạt động hệ thống. | **FEAT-12** | Xác thực & Quản lý tài khoản | Đăng nhập tài khoản BQT, mã hóa mật khẩu, thay đổi mật khẩu và cập nhật thông tin cá nhân. |
| | | | **FEAT-13** | Phân quyền vai trò (RBAC) & Audit | Phân quyền Admin, Kế toán, Thủ quỹ; ghi nhật ký kiểm toán thao tác nhạy cảm (sửa/hủy giao dịch). |
| **EPIC-06** | **Quản lý Dịch vụ Mở rộng (Roadmap v2.0)** | Mở rộng tính năng cho giai đoạn 2.0 gồm quản lý phương tiện gửi xe và thu hộ tiện ích bên ngoài. | **FEAT-14** | Quản lý phương tiện & phí gửi xe | Quản lý xe máy ($70.000\text{ đ}/xe$), ô tô ($1.200.000\text{ đ}/xe$) và tự động tính vào bảng phí hàng tháng. |
| | | | **FEAT-15** | Thu hộ chi phí điện, nước, internet | Nhập số liệu cước từ đơn vị cung cấp dịch vụ, phát hành thông báo thu hộ và quyết toán định kỳ. |

---

## 2. Bộ User Stories (Chuẩn INVEST, Phân loại MoSCoW)

Toàn bộ các User Stories được xây dựng theo chuẩn cấu trúc:
$$\text{As a } [\text{Role}], \text{ I want } [\text{Goal/Action}], \text{ so that } [\text{Business Benefit}]$$
và tuân thủ các nguyên tắc **INVEST** (Independent, Negotiable, Valuable, Estimable, Small, Testable):

| US ID | User Story (Nội dung câu chuyện người dùng) | Epic / Feature liên quan | Nguồn gốc (RAW ID) | Ưu tiên (MoSCoW) |
|:---:|---|:---:|:---:|:---:|
| **US-01** | **As a** Quản trị viên BQT,<br>**I want to** thêm, sửa và quản lý danh sách các căn hộ của tòa nhà BlueMoon kèm diện tích $m^2$, loại căn (ki-ốt, nhà ở, penthouse),<br>**so that** hệ thống có đầy đủ dữ liệu không gian vật lý làm căn cứ tính các khoản phí chính xác. | EPIC-01 / FEAT-01 | RAW-001, RAW-002 | **Must-have** |
| **US-02** | **As a** Quản trị viên BQT,<br>**I want to** tạo mới và cập nhật sổ hộ khẩu cho từng căn hộ (thông tin chủ hộ, số điện thoại, ngày đăng ký),<br>**so that** Ban quản trị quản lý được ai là đại diện pháp lý cho mỗi căn hộ. | EPIC-01 / FEAT-02 | RAW-003, RAW-031 | **Must-have** |
| **US-03** | **As a** Quản trị viên BQT,<br>**I want to** thêm và quản lý các nhân khẩu thuộc sổ hộ khẩu (họ tên, CCCD/Định danh cá nhân, ngày sinh, giới tính, mối quan hệ với chủ hộ),<br>**so that** nắm rõ danh sách mọi cá nhân đang sinh sống tại chung cư. | EPIC-01 / FEAT-02 | RAW-004, RAW-031 | **Must-have** |
| **US-04** | **As a** Thành viên BQT,<br>**I want to** ghi nhận biến động nhân khẩu (thêm người mới sinh, tách hộ, chuyển đi nơi khác, qua đời),<br>**so that** cơ sở dữ liệu dân cư luôn phản ánh đúng thực tế biến động tại tòa nhà. | EPIC-01 / FEAT-03 | RAW-005 | **Must-have** |
| **US-05** | **As a** Thành viên BQT,<br>**I want to** đăng ký thông tin tạm trú và tạm vắng cho cư dân (thời hạn từ ngày đến ngày, lý do, địa chỉ liên quan),<br>**so that** quản lý đúng quy định về cư trú và phát hiện kịp thời các trường hợp quá hạn. | EPIC-01 / FEAT-03 | RAW-006 | **Must-have** |
| **US-06** | **As a** Trưởng BQT,<br>**I want to** xuất danh sách nhân khẩu, danh sách tạm trú/tạm vắng ra file hoặc định dạng in ấn,<br>**so that** cung cấp kịp thời và chính xác cho Công an phường và Tổ dân phố khi có yêu cầu kiểm tra. | EPIC-01 / FEAT-04 | RAW-007, RAW-032 | **Should-have** |
| **US-07** | **As a** Kế toán BQT,<br>**I want to** thiết lập đơn giá cho Phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$) và Phí quản lý ($\ge 7.000\text{ đ}/m^2$) có hiệu lực theo kỳ,<br>**so that** hệ thống áp dụng đúng công thức tính phí cho từng căn hộ theo diện tích. | EPIC-02 / FEAT-05 | RAW-008, RAW-009, RAW-010 | **Must-have** |
| **US-08** | **As a** Trưởng BQT / Tổ dân phố,<br>**I want to** tạo một đợt vận động đóng góp quỹ tự nguyện (tên quỹ, mục đích, thời hạn phát động),<br>**so that** có thể phát động cư dân ủng hộ các phong trào xã hội (vì người nghèo, biển đảo, đền ơn đáp nghĩa...). | EPIC-02 / FEAT-06 | RAW-011 | **Must-have** |
| **US-09** | **As a** Kế toán BQT,<br>**I want to** phát sinh tự động bảng kê các khoản phí định kỳ phải nộp trong tháng cho toàn bộ các căn hộ,<br>**so that** tiết kiệm thời gian tính toán thủ công và đảm bảo không bỏ sót bất kỳ hộ dân nào. | EPIC-03 / FEAT-07 | RAW-013 | **Must-have** |
| **US-10** | **As a** Thủ quỹ BQT,<br>**I want to** tra cứu căn hộ theo số phòng và thực hiện ghi nhận nộp tiền (chọn khoản phí đóng, nhập số tiền đóng góp tự nguyện, ghi nhận người nộp),<br>**so that** việc thu tiền tại quầy diễn ra nhanh chóng, chính xác và tự động gạch nợ. | EPIC-03 / FEAT-08 | RAW-012, RAW-014, RAW-015, RAW-017 | **Must-have** |
| **US-11** | **As a** Thủ quỹ BQT,<br>**I want to** xuất và in biên lai thu tiền chi tiết ngay sau khi hoàn tất giao dịch thanh toán,<br>**so that** bàn giao chứng từ nộp tiền hợp lệ và minh bạch cho cư dân lưu giữ. | EPIC-03 / FEAT-09 | RAW-016, RAW-032 | **Must-have** |
| **US-12** | **As a** Thành viên BQT,<br>**I want to** tra cứu nhanh trạng thái công nợ của bất kỳ căn hộ nào (đã nộp, còn nợ những khoản nào),<br>**so that** kịp thời giải đáp thắc mắc cho cư dân và theo dõi đôn đốc thu phí. | EPIC-04 / FEAT-10 | RAW-018 | **Must-have** |
| **US-13** | **As a** Kế toán BQT,<br>**I want to** tìm kiếm và trích xuất lịch sử các giao dịch thu tiền theo khoảng thời gian tùy chọn,<br>**so that** đối soát dòng tiền và chốt sổ quỹ tiền mặt cuối ngày/cuối tuần. | EPIC-04 / FEAT-10 | RAW-019 | **Should-have** |
| **US-14** | **As a** Trưởng BQT,<br>**I want to** xem báo cáo thống kê tổng hợp số tiền thu phí định kỳ và tỷ lệ hoàn thành thu nộp theo tháng/tầng lầu,<br>**so that** nắm bắt toàn cảnh tình hình tài chính vận hành chung cư để báo cáo hội nghị cư dân. | EPIC-04 / FEAT-11 | RAW-020 | **Must-have** |
| **US-15** | **As a** Ban quản trị & Tổ dân phố,<br>**I want to** xem bảng thống kê kết quả đóng góp của từng đợt quỹ tự nguyện (danh sách từng hộ và số tiền ủng hộ),<br>**so that** công khai, minh bạch các khoản đóng góp từ thiện thay thế cho danh sách giấy thủ công. | EPIC-04 / FEAT-11 | RAW-021 | **Must-have** |
| **US-16** | **As a** Thành viên BQT,<br>**I want to** đăng nhập an toàn vào hệ thống bằng tài khoản và mật khẩu được cấp,<br>**so that** ngăn chặn những người không có phận sự truy cập vào thông tin nhạy cảm của chung cư. | EPIC-05 / FEAT-12 | RAW-022, NFR-001 | **Must-have** |
| **US-17** | **As a** Thành viên BQT,<br>**I want to** tự thay đổi mật khẩu và cập nhật thông tin cá nhân của mình,<br>**so that** chủ động bảo vệ an toàn cho tài khoản cá nhân. | EPIC-05 / FEAT-12 | RAW-023 | **Must-have** |
| **US-18** | **As a** Quản trị viên cấp cao (Admin),<br>**I want to** phân quyền người dùng theo vai trò (Admin, Kế toán, Thủ quỹ) và xem nhật ký thao tác sửa/hủy giao dịch,<br>**so that** kiểm soát chặt chẽ trách nhiệm và phòng chống thất thoát tài chính. | EPIC-05 / FEAT-13 | RAW-024, RAW-025, NFR-002, NFR-005 | **Should-have** |
| **US-19** | **As a** Ban quản lý chung cư,<br>**I want to** đăng ký xe máy/ô tô theo căn hộ và tự động tính phí gửi xe ($70.000\text{ đ}/xe máy$, $1.200.000\text{ đ}/ô tô$) hàng tháng,<br>**so that** quản lý bãi đỗ xe và tính phí gửi xe tập trung. *(Roadmap v2.0)* | EPIC-06 / FEAT-14 | RAW-027 | **Won't-have (v2.0)** |
| **US-20** | **As a** Kế toán BQT,<br>**I want to** nhập số liệu hóa đơn điện, nước, cước internet từ nhà cung cấp để thông báo thu hộ cư dân,<br>**so that** cư dân thanh toán tiện ích một lần và BQT dễ dàng quyết toán với nhà mạng. *(Roadmap v2.0)* | EPIC-06 / FEAT-15 | RAW-028 | **Won't-have (v2.0)** |

---

## 3. Tiêu chí Chấp nhận Chi tiết (Acceptance Criteria theo chuẩn Given-When-Then)

Đáp ứng $100\%$ User Stories chính của phiên bản v1.0 (vượt mục tiêu $\ge 80\%$ tại **OBJ-06**):

### US-01: Quản lý thông tin căn hộ
```gherkin
Scenario 1: Thêm căn hộ mới thành công
  Given Quản trị viên đã đăng nhập vào hệ thống với quyền Admin
  And đang ở màn hình "Quản lý Căn hộ"
  When Quản trị viên nhập mã phòng "P1204", tầng "12", diện tích "75.5", loại căn "Nhà ở"
  And nhấn nút "Lưu căn hộ"
  Then hệ thống lưu thông tin căn hộ vào cơ sở dữ liệu
  And hiển thị thông báo "Thêm mới căn hộ P1204 thành công"
  And căn hộ P1204 xuất hiện trong danh sách hiển thị.

Scenario 2: Thêm căn hộ với mã phòng đã tồn tại
  Given trong hệ thống đã tồn tại căn hộ có mã phòng "P1204"
  When Quản trị viên cố gắng tạo thêm căn hộ mới với mã phòng "P1204"
  Then hệ thống từ chối lưu dữ liệu
  And hiển thị thông báo lỗi "Mã phòng P1204 đã tồn tại trong hệ thống".

Scenario 3: Nhập diện tích không hợp lệ
  When Quản trị viên nhập diện tích là số âm hoặc bằng 0 (ví dụ: "-5" hoặc "0")
  Then hệ thống hiển thị thông báo lỗi "Diện tích căn hộ phải là số thực lớn hơn 0"
  And không cho phép submit form.
```

### US-02 & US-03: Quản lý Sổ hộ khẩu và Nhân khẩu
```gherkin
Scenario 1: Tạo sổ hộ khẩu mới cho căn hộ
  Given căn hộ "P1502" chưa có sổ hộ khẩu đăng ký
  When Quản trị viên chọn căn hộ "P1502", nhập họ tên chủ hộ "Nguyễn Văn An", SĐT "0912345678", số CCCD "001090123456"
  And nhấn "Tạo sổ hộ khẩu"
  Then hệ thống tạo mã sổ hộ khẩu duy nhất gắn liền với phòng P1502
  And thiết lập "Nguyễn Văn An" là chủ hộ với quan hệ "Chủ hộ".

Scenario 2: Thêm nhân khẩu thành viên vào sổ hộ khẩu
  Given sổ hộ khẩu của phòng "P1502" đã tồn tại
  When Quản trị viên nhấn "Thêm nhân khẩu"
  And nhập họ tên "Trần Thị Bình", quan hệ "Vợ", ngày sinh "15/08/1992", giới tính "Nữ", CCCD "001192654321"
  And nhấn "Lưu"
  Then thành viên "Trần Thị Bình" được lưu vào danh sách nhân khẩu của hộ P1502
  And tổng số nhân khẩu của hộ P1502 tăng thêm 1.

Scenario 3: Kiểm tra định dạng số CCCD
  When Quản trị viên nhập số CCCD có độ dài khác 12 chữ số hoặc chứa ký tự chữ
  Then hệ thống hiển thị thông báo "Số CCCD phải gồm đúng 12 chữ số hợp lệ".
```

### US-04 & US-05: Quản lý biến động cư trú và Tạm trú / Tạm vắng
```gherkin
Scenario 1: Đăng ký tạm trú thành công
  Given nhân khẩu "Lê Văn Cường" (khách thuê nhà) tại căn hộ "P0803"
  When cán bộ chọn chức năng "Đăng ký tạm trú"
  And nhập ngày bắt đầu "01/11/2026", ngày kết thúc "01/11/2027", lý do "Thuê nhà đi làm"
  And nhấn "Xác nhận"
  Then trạng thái cư trú của nhân khẩu đổi thành "Tạm trú"
  And thông tin tạm trú được lưu vào lịch sử cư trú của căn hộ.

Scenario 2: Đăng ký tạm vắng cho thành viên trong hộ
  Given công dân "Nguyễn Văn An" đang là thường trú tại căn hộ "P1502"
  When cán bộ chọn "Đăng ký tạm vắng"
  And nhập thời gian từ "01/12/2026" đến "30/06/2027", nơi đến "TP. Hồ Chí Minh", lý do "Công tác"
  Then trạng thái của cư dân đổi thành "Tạm vắng"
  And ngày kết thúc tạm vắng không được nhỏ hơn ngày bắt đầu.
```

### US-07: Cấu hình Phí dịch vụ và Phí quản lý
```gherkin
Scenario 1: Cấu hình phí dịch vụ trong giới hạn quy định
  Given Kế toán đăng nhập vào hệ thống
  When Kế toán nhập đơn giá Phí dịch vụ chung cư là "10.000" VNĐ/m2/tháng
  And nhấn "Lưu cấu hình phí"
  Then hệ thống ghi nhận đơn giá 10.000 VNĐ/m2/tháng
  And hiển thị thông báo "Cập nhật định mức phí dịch vụ thành công".

Scenario 2: Cảnh báo khi cấu hình phí dịch vụ ngoài khung quy định
  When Kế toán nhập đơn giá Phí dịch vụ là "20.000" VNĐ/m2/tháng (vượt khung 2.500 - 16.500 đ)
  Then hệ thống hiển thị cảnh báo "Định mức phí dịch vụ chung cư BlueMoon dao động từ 2.500 đến 16.500 đ/m2".

Scenario 3: Cấu hình phí quản lý hợp lệ
  When Kế toán nhập đơn giá Phí quản lý là "7.000" VNĐ/m2/tháng
  Then hệ thống lưu thành công vì đạt định mức tối thiểu từ 7.000 đ/m2 trở lên.
```

### US-08: Quản lý chiến dịch Quỹ tự nguyện
```gherkin
Scenario 1: Tạo đợt vận động quỹ tự nguyện mới
  Given người dùng có quyền Quản lý quỹ
  When tạo chiến dịch "Quỹ vì biển đảo Việt Nam 2026"
  And đặt ngày bắt đầu "01/11/2026", ngày kết thúc "30/11/2026", mô tả "Vận động ủng hộ cán bộ chiến sĩ Trường Sa"
  And nhấn "Kích hoạt chiến dịch"
  Then chiến dịch chuyển sang trạng thái "Đang vận động"
  And xuất hiện trên màn hình thu ngân để ghi nhận các khoản đóng góp tự nguyện.
```

### US-09: Tự động lập bảng kê thu phí tháng
```gherkin
Scenario 1: Phát sinh bảng kê thu phí định kỳ cho tháng mới
  Given ngày tính phí tháng 11/2026
  And Phí dịch vụ là 10.000 đ/m2, Phí quản lý là 7.000 đ/m2
  When Kế toán nhấn nút "Phát sinh hóa đơn phí tháng 11/2026"
  Then hệ thống tự động quét tất cả các căn hộ hiện có
  And với căn hộ P1204 diện tích 75.5 m2:
    - Phí dịch vụ: 75.5 * 10.000 = 755.000 VNĐ
    - Phí quản lý: 75.5 * 7.000 = 528.500 VNĐ
    - Tổng cộng kỳ này: 1.283.500 VNĐ
  And tạo ra các bản ghi công nợ tháng 11/2026 với trạng thái "Chưa thanh toán".
```

### US-10: Ghi nhận thu phí tại quầy
```gherkin
Scenario 1: Thu phí đủ các khoản định kỳ và đóng góp tự nguyện
  Given cư dân phòng "P1204" đến nộp tiền tại văn phòng
  When Thủ quỹ nhập số phòng "P1204"
  Then hệ thống hiển thị công nợ phí tháng 11/2026: 1.283.500 VNĐ
  And danh sách các đợt quỹ tự nguyện đang mở (ví dụ: Quỹ vì người nghèo)
  When Thủ quỹ tích chọn thanh toán phí dịch vụ, phí quản lý
  And nhập số tiền ủng hộ Quỹ vì người nghèo là "100.000" VNĐ, tên người nộp "Nguyễn Văn Hải"
  And nhấn "Xác nhận thu tiền"
  Then tổng số tiền thu ghi nhận là 1.383.500 VNĐ
  And trạng thái công nợ tháng 11/2026 của P1204 chuyển thành "Đã thanh toán"
  And khoản đóng góp 100.000 VNĐ được cộng vào Quỹ vì người nghèo.
```

### US-11: Phát hành và in biên lai thu tiền
```gherkin
Scenario 1: In biên lai ngay sau khi thu tiền
  Given giao dịch nộp tiền của phòng "P1204" vừa hoàn tất thành công
  When Thủ quỹ nhấn "In biên lai"
  Then hệ thống tạo mã biên lai duy nhất dạng "BL-202611-00124"
  And hiển thị bản xem trước biên lai gồm:
    - Tiêu đề: Ban Quản trị Chung cư BlueMoon - Biên lai thu tiền
    - Căn hộ: P1204 - Người nộp: Nguyễn Văn Hải
    - Danh sách khoản thu: Phí DV (755.000đ), Phí QL (528.500đ), Quỹ vì người nghèo (100.000đ)
    - Tổng tiền bằng số và bằng chữ: "Một triệu ba trăm tám mươi ba nghìn năm trăm đồng"
    - Ngày giờ thu, chữ ký điện tử của Thủ quỹ.
```

### US-14 & US-15: Báo cáo Thống kê
```gherkin
Scenario 1: Xem thống kê tổng thu phí tháng
  Given Kế toán hoặc Trưởng BQT mở màn hình "Báo cáo Thống kê"
  When chọn kỳ "Tháng 11/2026"
  Then hệ thống hiển thị:
    - Tổng số căn hộ phải thu: 350 căn
    - Số căn hộ đã hoàn thành nộp: 320 căn (91.4%)
    - Số căn hộ còn nợ: 30 căn (8.6%)
    - Tổng số tiền dự thu, số tiền thực thu, và số tiền còn tồn đọng
  And cho phép bấm "Tải file Excel" để xuất báo cáo chi tiết.

Scenario 2: Xem kết quả vận động quỹ tự nguyện
  When chọn đợt vận động "Quỹ vì biển đảo Việt Nam 2026"
  Then hệ thống hiển thị danh sách các hộ đã đóng góp, số tiền từng hộ
  And hiển thị tổng số tiền quyên góp được lũy kế đến hiện tại.
```

### US-16 & US-18: Xác thực & Phân quyền bảo mật
```gherkin
Scenario 1: Đăng nhập thành công với thông tin chính xác
  Given tài khoản "ketoan_bluemoon" tồn tại với mật khẩu hợp lệ
  When người dùng nhập đúng Username và Password rồi bấm "Đăng nhập"
  Then hệ thống xác thực thành công
  And chuyển hướng người dùng vào Dashboard quản trị tương ứng vai trò.

Scenario 2: Đăng nhập thất bại khi sai mật khẩu
  When người dùng nhập sai mật khẩu quá 5 lần liên tiếp
  Then hệ thống tạm khóa tài khoản trong 15 phút
  And thông báo "Tài khoản bị tạm khóa do nhập sai mật khẩu quá nhiều lần".

Scenario 3: Kiểm tra phân quyền truy cập
  Given người dùng đăng nhập với vai trò "Thủ quỹ"
  When người dùng cố gắng truy cập vào URL cấu hình tài khoản hệ thống hoặc sửa biểu phí
  Then hệ thống từ chối truy cập và hiển thị thông báo "403 Forbidden - Bạn không có quyền thực hiện hành động này".
```
