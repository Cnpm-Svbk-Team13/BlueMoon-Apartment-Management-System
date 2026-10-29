# CHI TIẾT USER STORIES & TIÊU CHÍ CHẤP NHẬN (US & AC)
## Dự án: Phần mềm Quản lý Chung cư BlueMoon (BMS)
### Tài liệu nguồn: File `CNPM_Team13_US-AC.xlsx` & `CNPM_Team13_US-AC.pdf` (Team 13)

---

## 1. Bảng Tổng hợp Bộ User Stories (US-001 $\rightarrow$ US-011)

| Mã US | Tên User Story | Ưu tiên | Ước lượng | Phụ thuộc | Giải quyết RAW |
|:---:|---|:---:|:---:|:---:|---|
| **US-001** | Quản lý Thông tin Căn hộ và Chủ hộ | **HIGH** | 5 Story Points | Không có | RAW-001, RAW-002 |
| **US-002** | Thêm mới và Quản lý Chi tiết Nhân khẩu | **HIGH** | 6 Story Points | US-001 | RAW-003, RAW-004, RAW-005, RAW-007, RAW-008 |
| **US-003** | Quản lý Trạng thái và Biến động Cư trú | **MEDIUM** | 4 Story Points | US-002 | RAW-006 |
| **US-004** | Trích xuất Báo cáo Nhân khẩu học Hành chính | **MEDIUM** | 5 Story Points | US-001, US-002, US-003 | RAW-009, RAW-010 |
| **US-005** | Khởi tạo & Quản lý Biểu phí | **HIGH** | 3 Story Points | Login BQT, CSDL | RAW-011, RAW-013, RAW-014 |
| **US-006** | Tính phí tự động hàng tháng | **HIGH** | 5 Story Points | US-005, Data Căn hộ | RAW-012, RAW-015 |
| **US-007** | Thu tiền, Gạch nợ & In phiếu PDF | **HIGH** | 5 Story Points | US-006 | RAW-016, RAW-017, RAW-020 |
| **US-008** | Tạo và Quản lý Đợt Vận động Quyên góp | **HIGH** | 3 Story Points | Đăng nhập BQT (Epic 5) | RAW-021 |
| **US-009** | Ghi nhận Đóng góp Tự nguyện & In Phiếu Xác nhận | **HIGH** | 5 Story Points | US-001, US-008 | RAW-022, RAW-023, RAW-024, RAW-025 |
| **US-010** | Báo cáo Tổng kết Đợt Vận động & Công khai Quỹ | **HIGH** | 5 Story Points | US-009 | RAW-026, RAW-027, RAW-028 |
| **US-011** | Tra cứu Lịch sử & Báo cáo Nợ phí | **HIGH** | 3 Story Points | US-006, US-007 | RAW-017, RAW-018, RAW-019 |

---

## 2. Đặc tả Chi tiết Từng User Story

### US-001: Quản lý Thông tin Căn hộ và Chủ hộ
- **User Story:**
  > Là một Nhân viên/Kế toán Ban quản trị, tôi muốn thêm mới, chỉnh sửa thông tin căn hộ và thiết lập hồ sơ Chủ hộ đại diện để làm cơ sở dữ liệu nền tảng cho việc thu phí và quản lý tòa nhà.
- **Mô tả (Description):** Quản lý toàn bộ danh mục không gian 30 tầng của tòa nhà. Tính năng này cho phép định danh loại hình mặt bằng và gắn kết mỗi căn hộ với đúng một Chủ hộ đại diện có đầy đủ thông tin pháp lý.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **5 story points** | **Phụ thuộc:** Không có
- **Ghi chú (Notes):** Giải quyết các điểm nghẽn RAW-001, RAW-002. Cần thống nhất danh sách "Loại hình mặt bằng" (Kiot, Thương mại, Căn hộ ở, Penthouse) làm danh mục cứng dưới CSDL.
- **Acceptance Criteria (AC):**
  1. Giao diện có đủ các trường nhập: Mã căn, Tầng, Diện tích, Loại hình.
  2. Hệ thống cảnh báo đỏ và chặn lưu nếu người dùng nhập diện tích $\le 0$ hoặc Mã căn hộ đã tồn tại.
  3. Khi cập nhật Chủ hộ mới, thông tin Chủ hộ cũ phải được lưu tự động vào danh sách "Lịch sử chủ hộ" để đối soát.
  4. Form khai báo Chủ hộ yêu cầu bắt buộc nhập đúng định dạng số điện thoại (10 chữ số) và số CCCD (12 chữ số).
- **Definition of Done (DoD):**
  1. Giao diện Form "Thêm/Sửa Căn hộ" hiển thị đúng chuẩn thiết kế UI/UX đã chốt.
  2. Pass 100% Unit Test cho các hàm kiểm tra định dạng. QA đã test và xác nhận hệ thống chặn thành công các trường hợp lỗi.
  3. Database ghi nhận thành công lịch sử thay đổi chủ hộ mỗi khi cập nhật, đảm bảo dữ liệu chủ hộ cũ không bị xóa cứng.

---

### US-002: Thêm mới và Quản lý Chi tiết Nhân khẩu
- **User Story:**
  > Là một Nhân viên Ban quản trị, tôi muốn thêm mới, cập nhật và quản lý danh sách các thành viên thực tế sinh sống trong từng căn hộ kèm theo số CCCD và mối quan hệ với chủ hộ để kiểm soát chính xác số lượng nhân khẩu cư trú tại tòa nhà.
- **Mô tả (Description):** Chức năng cho phép thêm không giới hạn các nhân khẩu vào một căn hộ. Hệ thống kiểm soát chặt chẽ mối quan hệ với chủ hộ, phân loại độ tuổi tự động và đặc biệt có cơ chế chặn việc đăng ký trùng một số CCCD ở nhiều căn hộ khác nhau.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **6 story points** | **Phụ thuộc:** US-001
- **Ghi chú (Notes):** Giải quyết RAW-003, RAW-004, RAW-005, RAW-007, RAW-008.
- **Acceptance Criteria (AC):**
  1. Có nút "Thêm nhân khẩu" mở bảng nhập liệu: Họ tên, Ngày sinh, Giới tính, CCCD, Ngày cấp, Nơi cấp, Mối quan hệ.
  2. Khi nhập CCCD, nếu hệ thống phát hiện số này đã tồn tại ở căn hộ khác, hiển thị popup cảnh báo đỏ và chặn thao tác lưu.
  3. Trường "Mối quan hệ với chủ hộ" bắt buộc phải chọn từ danh mục chuẩn hóa có sẵn (Chủ hộ, Vợ/Chồng, Con đẻ/nuôi, Bố/Mẹ, Khách thuê, Khác), không cho gõ tự do.
  4. Có tính năng ấn đính kèm ảnh chụp CCCD hoặc hợp đồng thuê nhà thẳng vào hồ sơ người đó.
- **Definition of Done (DoD):**
  1. Backend hoàn thiện thuật toán Global Validation (quét trùng lặp CCCD toàn cục). Trường "Mối quan hệ" trên UI bị khóa cứng dưới dạng Dropdown, không thể gõ text tự do.
  2. Chức năng Upload hoạt động đúng giới hạn (tối đa 5MB/file, chỉ nhận ảnh và PDF). File đính kèm tải lên có thể mở xem trước (Preview) hoặc tải xuống bình thường.
  3. Database ghi nhận thành công dữ liệu nhân khẩu và mối quan hệ.
  4. Product Owner (PO) đã review và nghiệm thu tính năng trên môi trường Staging.

---

### US-003: Quản lý Trạng thái và Biến động Cư trú
- **User Story:**
  > Là một Nhân viên Ban quản trị, tôi muốn thay đổi trạng thái cư trú của nhân khẩu (Thường trú, Tạm trú, Tạm vắng) và thiết lập thời hạn hiệu lực để phản ánh đúng thực trạng cư trú thay vì phải xóa bỏ hoàn toàn dữ liệu khi họ vắng mặt hoặc chuyển đi.
- **Mô tả (Description):** Phân hệ theo dõi biến động lưu trú, hỗ trợ khai báo thời hạn tạm trú/tạm vắng. Tất cả lịch sử thay đổi đều được ghi vết vĩnh viễn trên CSDL nhằm phục vụ công an kiểm tra mà không làm mất thông tin gốc.
- **Ưu tiên:** **MEDIUM** | **Ước lượng:** **4 story points** | **Phụ thuộc:** US-002
- **Ghi chú (Notes):** Giải quyết điểm nghẽn RAW-006.
- **Acceptance Criteria (AC):**
  1. Tại giao diện nhân khẩu, trường "Trạng thái cư trú" cho phép chọn 3 giá trị: Thường trú, Tạm trú, Tạm vắng (Mặc định khi thêm mới là Thường trú).
  2. Nếu chọn trạng thái "Tạm trú" hoặc "Tạm vắng", giao diện tự động bật và yêu cầu bắt buộc nhập hai trường ngày tháng: Thời hạn từ ngày và Thời hạn đến ngày.
  3. Hệ thống tự động ghi nhận nhật ký (Audit log) thời điểm thay đổi trạng thái và mã tài khoản nhân viên thực hiện thao tác đó.
- **Definition of Done (DoD):**
  1. Logic UI xử lý ẩn/hiện và set required cho trường Datepicker hoạt động chính xác.
  2. QA xác nhận không thể Submit (Lưu) form nếu thiếu thông tin ngày tháng đối với các trạng thái tạm trú/vắng.
  3. Hệ thống tự động ghi nhận nhật ký (Audit log) thời điểm thay đổi trạng thái và mã tài khoản nhân viên thực hiện thao tác đó.

---

### US-004: Trích xuất Báo cáo Nhân khẩu học Hành chính
- **User Story:**
  > Là một Trưởng Ban Quản trị, tôi muốn trích xuất nhanh danh sách nhân khẩu theo tầng, theo căn hộ hoặc theo trạng thái cư trú thành file chuẩn để cung cấp kịp thời cho Công an phường Văn Phú và Tổ dân phố khi có yêu cầu kiểm tra hành chính.
- **Mô tả (Description):** Phân hệ cung cấp các bộ lọc mạnh mẽ và thanh tìm kiếm toàn cục (Global Search) để tra cứu cư dân tức thời. Đồng thời cho phép kết xuất dữ liệu ra file Excel (.xlsx) hoặc PDF chuẩn biểu mẫu hành chính trong vài giây.
- **Ưu tiên:** **MEDIUM** | **Ước lượng:** **5 story points** | **Phụ thuộc:** US-001, US-002, US-003
- **Ghi chú (Notes):** Giải quyết yêu cầu RAW-009, RAW-010. Yêu cầu đội BA xin ngay mẫu biểu mẫu báo cáo bản cứng đang dùng thực tế tại phường để thiết kế bản PDF cho chính xác.
- **Acceptance Criteria (AC):**
  1. Màn hình Báo cáo cung cấp các bộ lọc (Filters) dạng dropdown, cho phép lọc dữ liệu linh hoạt theo: Khoảng tầng, Trạng thái cư trú, Mã căn hộ cụ thể.
  2. Hệ thống cảnh báo đỏ và chặn lưu nếu người dùng nhập diện tích $\le 0$ hoặc Mã căn hộ đã tồn tại.
  3. Khi nhấn nút "Xuất Excel", tải về tệp .xlsx sắp xếp đúng thứ tự cột: Mã căn, Họ tên, Ngày sinh, CCCD, Quan hệ, Trạng thái.
  4. Khi nhấn nút "Xuất PDF", kết xuất tệp tài liệu trang giấy dọc/ngang định dạng sẵn tiêu đề, bảng biểu, quốc hiệu tiêu ngữ và ô chữ ký. Hoàn thành trong dưới 3 giây.
- **Definition of Done (DoD):**
  1. Thanh tìm kiếm nhanh (Global Search) trả về kết quả gợi ý (Tên + Mã căn) dưới 1 giây ngay sau khi người dùng ngừng gõ phím.
  2. Khách hàng (Trưởng BQT hoặc Kế toán BlueMoon) trực tiếp test thử xuất file PDF và Ký nghiệm thu (Sign-off) xác nhận biểu mẫu đã đạt chuẩn để nộp cho Công an phường.

---

### US-005: Khởi tạo & Quản lý Biểu phí
- **User Story:**
  > As a Ban quản trị, I want to thiết lập và điều chỉnh đơn giá Phí dịch vụ và Phí quản lý theo $m^2$, so that hệ thống có căn cứ chuẩn để tính tiền hàng tháng.
- **Mô tả (Description):** Quản lý danh mục biểu phí cố định hàng tháng cho tòa nhà chung cư BlueMoon v1.0.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **3 Story Points** | **Phụ thuộc:** Cần kết nối CSDL MySQL và tài khoản BQT đã đăng nhập.
- **Ghi chú (Notes):** Traceability: RAW-011, RAW-013, RAW-014.
- **Acceptance Criteria (AC):**
  1. Thêm/sửa thành công đơn giá Phí dịch vụ ($2.500 - 16.500\text{ đ}/m^2$) và lưu vào CSDL MySQL.
  2. Ràng buộc đơn giá Phí quản lý tối thiểu $\ge 7.000\text{ đ}/m^2$ (hiển thị thông báo nếu nhập sai).
  3. Lưu vết thời gian và tài khoản BQT thực hiện thay đổi đơn giá.
- **Definition of Done (DoD):**
  1. Viết mã nguồn (Java) và review code.
  2. Kiểm thử Unit Test các điều kiện biên giá ($< 7.000\text{ đ}$).
  3. Lưu trữ thành công trên CSDL MySQL.
  4. Kiểm thử giao diện thành công.

---

### US-006: Tính phí tự động hàng tháng
- **User Story:**
  > As a Kế toán/Thủ quỹ, I want to hệ thống tự động lấy diện tích $m^2$ nhân đơn giá để lập bảng kê thu phí hàng tháng, so that giảm thời gian nhập tay và tránh sai sót công thức Excel.
- **Mô tả (Description):** Tính toán tự động toàn bộ bảng kê phí cho 24 tầng căn hộ & Penthouse.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **5 Story Points** | **Phụ thuộc:** US-005 (Đã cấu hình biểu phí) và dữ liệu Căn hộ/Diện tích $m^2$.
- **Ghi chú (Notes):** Traceability: RAW-012, RAW-015.
- **Acceptance Criteria (AC):**
  1. Tự động kết xuất diện tích $m^2$ của tất cả căn hộ và nhân với đơn giá Phí DV + Phí QL.
  2. Tạo bảng kê thu phí định kỳ hàng tháng với trạng thái mặc định "Chưa thanh toán".
  3. Cảnh báo căn hộ chưa có dữ liệu diện tích ($0\text{ m}^2$).
- **Definition of Done (DoD):**
  1. Viết mã nguồn (Java) và review code.
  2. Kiểm thử tích hợp (Integration Test) với bảng Căn hộ.
  3. Kiểm thử chính xác số tiền của từng hộ.
  4. Phê duyệt kết quả tính phí.

---

### US-007: Thu tiền, Gạch nợ & In phiếu PDF
- **User Story:**
  > As a Thủ quỹ, I want to tra cứu phòng, bấm gạch nợ thanh toán và xuất phiếu thu dạng PDF, so that hoàn tất giao dịch tài chính và cung cấp chứng từ cho cư dân.
- **Mô tả (Description):** Ghi nhận thu tiền mặt tại văn phòng BQL và cấp chứng từ cho cư dân.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **5 Story Points** | **Phụ thuộc:** US-006 (Đã có bảng kê tính phí hàng tháng).
- **Ghi chú (Notes):** Traceability: RAW-016, RAW-017, RAW-020.
- **Acceptance Criteria (AC):**
  1. Tìm kiếm căn hộ nhanh theo Mã/Số phòng và hiển thị tổng tiền nợ phí.
  2. Cập nhật trạng thái thanh toán thành "Đã nộp", lưu ngày thu và tài khoản thu.
  3. Tự động xuất/in Phiếu thu PDF chứa đầy đủ thông tin giao dịch.
- **Definition of Done (DoD):**
  1. Xây dựng hàm gạch nợ và tạo file PDF.
  2. Kiểm thử chức năng tìm kiếm & gạch nợ.
  3. Kiểm thử định dạng file PDF Phiếu thu.
  4. Kiểm thử UAT trực tiếp với Thủ quỹ.

---

### US-008: Tạo và Quản lý Đợt Vận động Quyên góp
- **User Story:**
  > As a Thành viên Ban Quản trị / Tổ trưởng Tổ dân phố, I want to tạo mới và quản lý các đợt vận động quyên góp xã hội (tên đợt, thời gian, mục đích, đơn vị tiếp nhận) so that dễ dàng thông báo và phát động phong trào ủng hộ đến toàn thể cư dân tòa nhà.
- **Mô tả (Description):** Cho phép BQT và Tổ dân phố khởi tạo linh hoạt các phong trào quyên góp từ thiện đột xuất hoặc định kỳ (Ủng hộ bão lụt, Vì người nghèo, Tết thiếu nhi) hoàn toàn độc lập với các khoản phí dịch vụ cố định theo $m^2$.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **3 story points** | **Phụ thuộc:** Đăng nhập tài khoản BQT (Epic 5)
- **Ghi chú (Notes):** Giải quyết yêu cầu thô RAW-021. Đảm bảo cấu trúc CSDL tách biệt với biểu phí $m^2$ bắt buộc.
- **Acceptance Criteria (AC):**
  1. Form tạo đợt vận động có đầy đủ các trường: Mã quỹ (tự sinh), Tên đợt vận động, Ngày bắt đầu, Ngày kết thúc, Mục đích quyên góp, Cơ quan/Đơn vị tiếp nhận.
  2. Hệ thống cảnh báo đỏ và chặn lưu nếu để trống Tên đợt vận động hoặc chọn Ngày kết thúc trước Ngày bắt đầu.
  3. Cho phép BQT chuyển đổi trạng thái đợt vận động giữa "Đang phát động" và "Đã kết thúc / Đóng quỹ".
  4. Khi đợt vận động ở trạng thái "Đang phát động", đợt này tự động xuất hiện trong danh mục lựa chọn thu tiền tại phân hệ Thu ngân.
- **Definition of Done (DoD):**
  1. Giao diện Form "Tạo/Sửa Đợt Vận động" hiển thị đúng chuẩn thiết kế UI/UX đã duyệt.
  2. Pass 100% Unit Test kiểm tra logic ngày tháng hợp lệ và các trường bắt buộc; QA xác nhận chặn lỗi thành công.
  3. Database lưu thành công bản ghi vào bảng `Campaign_Fund` trên MySQL, phân biệt rõ ràng với bảng cấu hình biểu phí cố định.

---

### US-009: Ghi nhận Đóng góp Tự nguyện & In Phiếu Xác nhận
- **User Story:**
  > As a Nhân viên Kế toán / Thu ngân BQT, I want to ghi nhận số tiền đóng góp tùy tâm của từng căn hộ và in ngay Phiếu xác nhận ủng hộ để trao cho cư dân, so that ghi nhận sự ủng hộ tự nguyện mà không bị áp đặt mức thu tối thiểu hay tính theo $m^2$.
- **Mô tả (Description):** Chức năng tác nghiệp cốt lõi thực thi giải pháp dung hòa xung đột. Tách bạch 100% tiền đóng góp tùy tâm khỏi nghĩa vụ tài chính bắt buộc, giúp kế toán gạch đúng nguồn tiền và cư dân an tâm ủng hộ.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **5 story points** | **Phụ thuộc:** US-001 (Danh mục Căn hộ), US-008 (Đợt vận động)
- **Ghi chú (Notes):** Giải quyết các điểm nghẽn RAW-022, RAW-023, RAW-024, RAW-025. Mấu chốt kỹ thuật: Không cập nhật vào trường `Debt_Amount` của căn hộ.
- **Acceptance Criteria (AC):**
  1. Cho phép tìm kiếm nhanh căn hộ theo Mã căn (VD: P.1204) và chọn đợt vận động đang mở để nhập tiền.
  2. Ô nhập số tiền cho phép nhập số tiền bất kỳ $\ge 0$ VNĐ (chấp nhận 0 VNĐ nếu không tham gia); hệ thống tuyệt đối không áp công thức nhân theo diện tích $m^2$.
  3. Cho phép chọn hình thức thanh toán (Tiền mặt hoặc Chuyển khoản) và ghi nhận tên người trực tiếp nộp tiền.
  4. Khi bấm "Lưu & In phiếu", hệ thống ghi nhận thành công và tự động xuất file PDF "Phiếu xác nhận ủng hộ tự nguyện" có logo BQT, thông tin căn hộ, số tiền và lời cảm ơn.
  5. Khoản đóng góp tự nguyện này tuyệt đối không được cộng dồn vào cột nợ đọng hay lũy kế công nợ của phí dịch vụ bắt buộc.
- **Definition of Done (DoD):**
  1. Giao diện ghi nhận thu quỹ và mẫu Phiếu thu PDF hiển thị đúng format độc lập với hóa đơn dịch vụ.
  2. Pass 100% Unit Test và QA xác nhận trường hợp căn hộ không đóng tiền (0 đ) không bị ghi nhận trạng thái nợ xấu.
  3. Database lưu thành công giao dịch vào bảng `Fund_Contribution`, liên kết khóa ngoại với `Apartment_ID` và `Fund_ID`.

---

### US-010: Báo cáo Tổng kết Đợt Vận động & Công khai Quỹ
- **User Story:**
  > As a Thành viên Ban Quản trị / Tổ trưởng Tổ dân phố, I want to xuất báo cáo tổng kết danh sách đóng góp và tổng số tiền thu được theo từng đợt vận động (kèm tùy chọn ẩn thông tin nhạy cảm) so that I can công khai minh bạch cho cư dân và bàn giao cho cơ quan tiếp nhận.
- **Mô tả (Description):** Cung cấp công cụ minh bạch hóa tài chính cho các hoạt động xã hội tại chung cư. Giúp Tổ dân phố và BQT dễ dàng lập biên bản bàn giao tiền ủng hộ cho Mặt trận Tổ quốc hoặc dán công khai tại bảng tin tòa nhà.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **5 story points** | **Phụ thuộc:** US-009 (Dữ liệu đóng góp quỹ)
- **Ghi chú (Notes):** Giải quyết RAW-026, RAW-027, RAW-028. Cần đảm bảo tính năng ẩn thông tin cá nhân tuân thủ quy định bảo vệ dữ liệu cá nhân (Nghị định 13/2023/NĐ-CP).
- **Acceptance Criteria (AC):**
  1. Bộ lọc báo cáo cho phép lọc theo: Đợt vận động, Khoảng thời gian thu, và Theo khoảng tầng (Tầng 1 - 30).
  2. Hệ thống tổng hợp và hiển thị chính xác 100%: Tổng số tiền đã thu, Tổng số căn hộ tham gia, và Tỷ lệ tham gia trên toàn tòa nhà.
  3. Cung cấp checkbox "Chế độ công khai sảnh": Khi bật, danh sách xuất ra sẽ tự động che/ẩn số CCCD và Số điện thoại, chỉ hiển thị Mã căn + Tên chủ hộ + Số tiền.
  4. Hỗ trợ xuất dữ liệu ra file PDF (chuẩn in A4 kèm chữ ký BQT & TDP) và file Excel phục vụ lưu trữ, đối soát.
- **Definition of Done (DoD):**
  1. Báo cáo hiển thị chính xác, tốc độ tải dữ liệu $< 1$s cho toàn bộ 30 tầng căn hộ.
  2. Pass kiểm thử đối soát: Tổng tiền trên báo cáo khớp chính xác 100% với tổng các phiếu thu quỹ trong CSDL.
  3. File PDF/Excel xuất ra đúng biểu mẫu hành chính có phần chữ ký biên bản bàn giao giữa BQT và Tổ dân phố.

---

### US-011: Tra cứu Lịch sử & Báo cáo Nợ phí
- **User Story:**
  > As a Ban quản trị, I want to tra cứu lịch sử đóng phí và xuất danh sách các hộ còn nợ phí bắt buộc, so that dễ dàng theo dõi dòng tiền và gửi thông báo nhắc nợ.
- **Mô tả (Description):** Cung cấp công cụ quản lý nợ phí bắt buộc và lịch sử nộp tiền cho BQL.
- **Ưu tiên:** **HIGH** | **Ước lượng:** **3 Story Points** | **Phụ thuộc:** US-006 và US-007 (Dữ liệu bảng kê & giao dịch thanh toán).
- **Ghi chú (Notes):** Traceability: RAW-017, RAW-018, RAW-019.
- **Acceptance Criteria (AC):**
  1. Hiển thị danh sách các căn hộ chưa thanh toán phí dịch vụ/quản lý theo từng tháng.
  2. Cho phép tìm kiếm và xem lịch sử các đợt nộp tiền cũ của một căn hộ bất kỳ.
  3. Xuất danh sách nợ phí và báo cáo tổng hợp thu chi ra file Excel hoặc PDF.
- **Definition of Done (DoD):**
  1. Viết Query lọc danh sách nợ phí trên Java Backend.
  2. Kiểm thử hiển thị lịch sử giao dịch chính xác.
  3. Kiểm thử xuất file Excel/PDF định dạng báo cáo.
  4. Phê duyệt kết quả UAT trực tiếp với BQT.
