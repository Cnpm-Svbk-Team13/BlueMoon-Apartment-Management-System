# DANH SÁCH YÊU CẦU THÔ & CLIENT'S WISH LIST
## Dự án: Phần mềm Quản lý Thu phí Chung cư BlueMoon

---

## 1. Giới thiệu

Tài liệu này ghi nhận nguyên văn toàn bộ các yêu cầu, mong muốn, khó khăn và kỳ vọng được thu thập từ các bên liên quan (Ban quản trị tòa nhà, Kế toán/Thủ quỹ, đại diện Cư dân, Cán bộ Tổ dân phố/Công an phường) cùng các bằng chứng hiện vật (mẫu sổ thu quỹ Hình 1-1, bảng tính Excel hiện hành).

Toàn bộ các yêu cầu được chuẩn hóa mã hóa theo tiền tố `RAW-xxx` (đối với yêu cầu nghiệp vụ / chức năng thô) và `NFR-xxx` (đối với yêu cầu phi chức năng thô), đáp ứng đầy đủ mục tiêu **OBJ-01** ($\ge 30$ yêu cầu thô) và **OBJ-02** ($\ge 5$ NFR thô).

---

## 2. Client's Wish List (Danh sách mong muốn thô)

| Mã ID | Nguồn cung cấp | Mong muốn thô (Nguyên văn / Phản ánh thực tế) | Ghi chú phân loại |
|:---:|---|---|---|
| **RAW-001** | Trưởng BQT | Tôi muốn hệ thống lưu trữ được danh sách tất cả các căn hộ của tòa nhà BlueMoon, bao gồm cả ki-ốt tầng 1, căn hộ từ tầng 6 đến 29 và căn penthouse tầng 30. | Quản lý căn hộ |
| **RAW-002** | Kế toán BQT | Mỗi căn hộ phải lưu chính xác diện tích ($m^2$) vì đây là căn cứ bắt buộc để tính phí dịch vụ và phí quản lý hàng tháng. | Thuộc tính căn hộ / Tính phí |
| **RAW-003** | Trưởng BQT | Tôi cần quản lý thông tin từng hộ gia đình (sổ hộ khẩu), biết rõ ai là chủ hộ, số điện thoại liên lạc và số phòng họ đang ở. | Quản lý Hộ gia đình |
| **RAW-004** | Trưởng BQT | Phải xem được danh sách tất cả nhân khẩu (thành viên) trong một hộ gia đình, gồm họ tên, ngày sinh, giới tính, CCCD và mối quan hệ với chủ hộ. | Quản lý Nhân khẩu |
| **RAW-005** | Ban Quản trị | Khi có cư dân sinh thêm con, có người chuyển đến sống cùng hoặc chuyển đi nơi khác, ban quản trị phải cập nhật được biến động nhân khẩu này. | Biến động nhân khẩu |
| **RAW-006** | Cán bộ TDP / Công an | Hệ thống phải hỗ trợ ghi nhận việc đăng ký tạm trú và tạm vắng của cư dân kèm theo khoảng thời gian và lý do để nộp cho công an phường khi được yêu cầu. | Pháp lý / Cư trú |
| **RAW-007** | Trưởng BQT | Phải có chức năng xuất danh sách nhân khẩu, danh sách tạm trú/tạm vắng ra file hoặc in ra giấy để nộp cho cơ quan chức năng khi có đợt kiểm tra. | Báo cáo cơ quan chức năng |
| **RAW-008** | Kế toán BQT | Tôi muốn hệ thống cho phép tạo và quản lý danh mục các loại phí: có loại bắt buộc theo tháng và có loại thu tự nguyện theo từng đợt vận động. | Danh mục khoản thu |
| **RAW-009** | Kế toán BQT | Phí dịch vụ chung cư phải cho phép cài đặt mức thu dao động từ 2.500 đến 16.500 đ/$m^2$/tháng và tự động nhân với diện tích căn hộ để ra số tiền phải nộp. | Công thức phí dịch vụ |
| **RAW-010** | Kế toán BQT | Phí quản lý chung cư cần cấu hình được đơn giá từ 7.000 đ/$m^2$/tháng trở lên và cũng tính theo diện tích căn hộ. | Công thức phí quản lý |
| **RAW-011** | Cán bộ Tổ dân phố | Khi có các đợt phát động như Quỹ vì người nghèo, Quỹ biển đảo, Quỹ đền ơn đáp nghĩa, Quỹ khuyến học, Quỹ người cao tuổi... BQT phải tạo được đợt vận động với tên quỹ và khoảng thời gian thu. | Quỹ tự nguyện |
| **RAW-012** | Thủ quỹ BQT | Các khoản đóng góp tự nguyện thì không áp đặt số tiền cố định, hộ dân ủng hộ bao nhiêu thì ghi nhận bấy nhiêu vào hệ thống. | Quy tắc thu tự nguyện |
| **RAW-013** | Kế toán BQT | Hàng tháng, chỉ cần một nút bấm là hệ thống tự động sinh ra danh sách các khoản phí cần nộp của tất cả các hộ dân trong toàn tòa nhà. | Tự động lập bảng kê thu |
| **RAW-014** | Thủ quỹ BQT | Khi chủ hộ đến văn phòng nộp tiền, tôi gõ số phòng hoặc tên chủ hộ là ra ngay danh sách các khoản phí căn hộ đó phải đóng trong tháng. | Giao diện thu phí tại quầy |
| **RAW-015** | Thủ quỹ BQT | Tôi có thể tích chọn thanh toán từng khoản hoặc thanh toán toàn bộ các khoản một lúc cho hộ dân. | Nghiệp vụ gạch nợ |
| **RAW-016** | Đại diện Cư dân | Khi đóng tiền, tôi muốn nhận được biên lai thu tiền rõ ràng, liệt kê chi tiết từng khoản đã đóng để lưu giữ làm bằng chứng đối soát. | Biên lai thu tiền |
| **RAW-017** | Kế toán BQT | Trong danh sách sổ thu đóng góp thủ công hiện tại có chữ ký người nộp (như mẫu Hình 1-1), phần mềm cần ghi nhận được người nộp thực tế và ngày giờ nộp. | Đối soát chứng từ |
| **RAW-018** | Trưởng BQT | Tôi muốn có màn hình tra cứu nhanh: chỉ cần gõ số phòng là biết phòng này đã nộp đủ tiền tháng này chưa, còn nợ những khoản nào. | Tra cứu công nợ |
| **RAW-019** | Kế toán BQT | Cần có chức năng tìm kiếm lịch sử thu tiền theo khoảng thời gian (từ ngày... đến ngày...) để kiểm kê quỹ tiền mặt hàng ngày/hàng tuần. | Tra cứu lịch sử thu |
| **RAW-020** | Trưởng BQT | Cuối tháng BQT cần xem báo cáo tổng kết: Tổng số tiền thu được của từng loại phí (dịch vụ, quản lý), tỷ lệ số hộ đã đóng / chưa đóng. | Thống kê phí định kỳ |
| **RAW-021** | Cán bộ TDP / BQT | Cần có báo cáo riêng cho các đợt quyên góp: tổng số tiền ủng hộ của từng quỹ, danh sách chi tiết các hộ gia đình đã đóng góp kèm số tiền. | Thống kê quỹ tự nguyện |
| **RAW-022** | Trưởng BQT | Chỉ những thành viên BQT được cấp tài khoản mới được truy cập vào phần mềm, người ngoài không thể tùy tiện vào xem số liệu tài chính hay thông tin cư dân. | Xác thực & An ninh |
| **RAW-023** | Kế toán BQT | Mỗi nhân sự BQT phải có tài khoản riêng, có thể tự thay đổi mật khẩu và cập nhật thông tin cá nhân của mình khi cần. | Quản lý tài khoản cá nhân |
| **RAW-024** | Trưởng BQT | Cần phân quyền rõ ràng: Thủ quỹ chỉ được thu tiền và xem báo cáo; Kế toán được tạo khoản thu; Trưởng ban được duyệt và quản trị người dùng. | Phân quyền vai trò (RBAC) |
| **RAW-025** | Kế toán BQT | Nếu thủ quỹ lỡ nhập sai số tiền, việc sửa hoặc hủy phiếu thu phải được ghi nhận vết (log) để tránh gian lận tiền bạc. | Nhật ký giao dịch / Audit |
| **RAW-026** | Đại diện Cư dân | Cư dân chúng tôi mong muốn sau này có thể tra cứu thông tin căn hộ và tiền phí trực tuyến qua cổng web mà không cần phải gọi điện hỏi BQT. | Mong muốn v1.0/v2.0 |
| **RAW-027** | Trưởng BQT (v2.0) | Sang phiên bản 2.0, BQT muốn quản lý thêm xe máy và ô tô của các hộ: xe máy 70.000 đ/tháng, ô tô 1.200.000 đ/tháng để tính gộp vào thông báo phí. | Mở rộng v2.0 (Phí gửi xe) |
| **RAW-028** | Trưởng BQT (v2.0) | Sang phiên bản 2.0, BQT muốn thu hộ tiền điện, tiền nước, internet theo số liệu bên điện lực, nước, viễn thông gửi sang để cư dân nộp 1 lần tiện lợi. | Mở rộng v2.0 (Thu hộ tiện ích) |
| **RAW-029** | Kế toán BQT | Dữ liệu phải được sao lưu thường xuyên đề phòng máy tính kế toán bị hỏng hóc hoặc nhiễm virus là mất trắng số liệu thu chi. | Sao lưu dữ liệu (Backup) |
| **RAW-030** | Thủ quỹ BQT | Phần mềm phải dễ dùng, chữ to rõ ràng vì nhiều cô chú trong ban quản trị lớn tuổi, thao tác máy tính chưa thành thạo. | Tính khả dụng (Usability) |
| **RAW-031** | Cán bộ TDP | Thông tin nhân khẩu phải lưu trữ đúng chuẩn quy định nhà nước (Họ tên đầy đủ, ngày tháng năm sinh, số định danh cá nhân VNeID/CCCD). | Chuẩn hóa dữ liệu cư trú |
| **RAW-032** | Kế toán BQT | Cho phép xuất các bảng danh sách thu tiền và công nợ ra định dạng Excel hoặc PDF để in ấn và dán bảng tin chung cư. | Xuất dữ liệu (Export) |

---

## 3. Danh sách Yêu cầu Phi chức năng thô (Raw NFR Checklist)

| Mã ID | Mô tả yêu cầu phi chức năng (NFR) | Nguồn thu thập | Phân loại NFR | Tiêu chí đo lường / Đánh giá |
|:---:|---|---|---|---|
| **NFR-001** | Mật khẩu người dùng phải được mã hóa một chiều an toàn (BCrypt/Argon2); không lưu mật khẩu dạng văn bản thô (plain text). | Yêu cầu an toàn thông tin | **Bảo mật (Security)** | Đạt $100\%$ kiểm tra mã hóa mật khẩu trong CSDL; tự động khóa tài khoản sau 5 lần nhập sai liên tiếp. |
| **NFR-002** | Hệ thống phải áp dụng kiểm soát truy cập dựa trên vai trò (Role-Based Access Control - RBAC) nghiêm ngặt giữa Admin, Kế toán, Thủ quỹ. | Phỏng vấn BQT | **Phân quyền (Authorization)** | Người dùng không thể truy cập các URL hoặc chức năng vượt quá thẩm quyền được phân quyền. |
| **NFR-003** | Thời gian phản hồi của hệ thống khi tra cứu căn hộ, tìm kiếm cư dân hoặc tính toán hóa đơn không được vượt quá 2 giây trong điều kiện tải thông thường. | Quan sát thủ quỹ | **Hiệu năng (Performance)** | $95\%$ các thao tác tìm kiếm và render dữ liệu phản hồi trong vòng dưới $1.5$ giây. |
| **NFR-004** | Đảm bảo tính nhất quán và toàn vẹn dữ liệu giao dịch tài chính (ACID transaction). Tuyệt đối không để xảy ra trường hợp trừ tiền/gạch nợ một nửa. | Yêu cầu nghiệp vụ kế toán | **Toàn vẹn dữ liệu (Data Integrity)** | Tất cả giao dịch ghi nhận phiếu thu đều nằm trong một Database Transaction an toàn. |
| **NFR-005** | Lưu vết nhật ký kiểm toán (Audit Trail) cho toàn bộ các hành động thêm, sửa, xóa dữ liệu nhạy cảm (nhân khẩu, biểu phí, xóa/sửa phiếu thu). | Phỏng vấn BQT | **Kiểm toán (Auditability)** | Ghi lại đầy đủ: `User_ID`, `Action`, `Timestamp`, `Old_Value`, `New_Value`. |
| **NFR-006** | Tuân thủ các quy định bảo mật dữ liệu cá nhân theo Nghị định 13/2023/NĐ-CP và Luật Cư trú của Việt Nam. | Tham vấn pháp lý TDP | **Tuân thủ pháp luật (Compliance)** | Thông tin CCCD, số điện thoại cư dân không được để lộ công khai cho người dùng không có thẩm quyền. |
| **NFR-007** | Hệ thống cung cấp cơ chế sao lưu dữ liệu tự động hoặc sao lưu thủ công bằng một thao tác xuất dump CSDL định kỳ hàng tuần. | Kế toán BQT | **Khả năng phục hồi (Recoverability)** | Thời gian phục hồi lại dữ liệu (RTO) $< 30$ phút, thất thoát dữ liệu tối đa (RPO) $< 24$ giờ. |
| **NFR-008** | Giao diện thân thiện, hỗ trợ tiếng Việt có dấu, phông chữ chuẩn, bố cục rõ ràng, thao tác nhập liệu tối ưu hóa bằng phím bấm. | Thủ quỹ BQT | **Tính tiện dụng (Usability)** | Người dùng làm quen và thực hiện thành thạo thao tác thu phí sau tối đa 2 giờ đào tạo. |

---

## 4. Bảng Raw Log Tổng hợp (Sẵn sàng xuất CSV/Excel)

| ID | Nguồn | Nội dung thô thu thập | Loại | Artefact chuẩn hóa hướng tới | Ghi chú |
|:---:|:---:|---|:---:|:---:|:---:|
| **RAW-001** | BQT | Quản lý danh sách 30 tầng căn hộ, kiot, penthouse | FR | Epic 1 / Feature 1.1 / US-01 | Phạm vi v1.0 |
| **RAW-002** | Kế toán | Lưu diện tích $m^2$ để tính phí dịch vụ, quản lý | FR | Feature 1.1, Feature 2.1 | Cốt lõi v1.0 |
| **RAW-003** | BQT | Quản lý sổ hộ khẩu, chủ hộ, liên hệ | FR | Epic 1 / Feature 1.2 / US-02 | Cốt lõi v1.0 |
| **RAW-004** | BQT | Quản lý thành viên nhân khẩu, quan hệ, CCCD | FR | Epic 1 / Feature 1.2 / US-03 | Cốt lõi v1.0 |
| **RAW-005** | BQT | Cập nhật biến động nhân khẩu (thêm, bớt, chuyển) | FR | Epic 1 / Feature 1.3 / US-04 | Cốt lõi v1.0 |
| **RAW-006** | TDP/CA | Quản lý tạm trú, tạm vắng có thời hạn và lý do | FR | Epic 1 / Feature 1.3 / US-05 | Bắt buộc pháp lý |
| **RAW-007** | BQT | Xuất danh sách nhân khẩu nộp công an phường | FR | Feature 1.4 / US-06 | Báo cáo chính quyền |
| **RAW-008** | Kế toán | Quản lý danh mục khoản thu bắt buộc và tự nguyện | FR | Epic 2 / Feature 2.1 / US-07 | Cốt lõi v1.0 |
| **RAW-009** | Kế toán | Cấu hình phí dịch vụ theo $m^2$ (2.500 - 16.500 đ) | FR | Epic 2 / Feature 2.1 / US-08 | Cốt lõi v1.0 |
| **RAW-010** | Kế toán | Cấu hình phí quản lý theo $m^2$ (từ 7.000 đ) | FR | Epic 2 / Feature 2.1 / US-08 | Cốt lõi v1.0 |
| **RAW-011** | TDP | Tạo các đợt vận động quỹ tự nguyện theo chiến dịch | FR | Epic 2 / Feature 2.2 / US-09 | Cốt lõi v1.0 |
| **RAW-012** | Thủ quỹ | Thu tự nguyện không áp mức cứng, ủng hộ tùy tâm | FR | Feature 2.2, Feature 3.2 | Nghiệp vụ đóng góp |
| **RAW-013** | Kế toán | Tự động sinh bảng kê thu phí định kỳ hàng tháng | FR | Epic 3 / Feature 3.1 / US-10 | Tối ưu vận hành |
| **RAW-014** | Thủ quỹ | Giao diện thu phí tại quầy tra cứu theo số phòng | FR | Epic 3 / Feature 3.2 / US-11 | Giảm thời gian chờ |
| **RAW-015** | Thủ quỹ | Chọn đóng từng khoản hoặc đóng tất cả | FR | Feature 3.2 / US-11 | Nghiệp vụ linh hoạt |
| **RAW-016** | Cư dân | Xuất/In biên lai thu tiền chi tiết làm bằng chứng | FR | Feature 3.3 / US-12 | Minh bạch tài chính |
| **RAW-017** | Thủ quỹ | Ghi nhận người nộp tiền thực tế và ngày giờ nộp | FR | Feature 3.2 / US-11 | Thay thế chữ ký tay |
| **RAW-018** | BQT | Tra cứu nhanh tình trạng công nợ theo từng phòng | FR | Epic 4 / Feature 4.1 / US-13 | Tra cứu nhanh |
| **RAW-019** | Kế toán | Tìm kiếm lịch sử thu tiền theo khoảng thời gian | FR | Feature 4.1 / US-14 | Chốt sổ quỹ |
| **RAW-020** | BQT | Thống kê tổng thu phí định kỳ, tỷ lệ hoàn thành | FR | Epic 4 / Feature 4.2 / US-15 | Báo cáo BQT |
| **RAW-021** | TDP/BQT | Thống kê danh sách ủng hộ các quỹ tự nguyện | FR | Feature 4.2 / US-16 | Minh bạch quỹ công |
| **RAW-022** | BQT | Đăng nhập hệ thống bảo mật, chặn truy cập trái phép | FR | Epic 5 / Feature 5.1 / US-17 | An toàn hệ thống |
| **RAW-023** | Kế toán | Quản lý thông tin cá nhân và thay đổi mật khẩu | FR | Feature 5.1 / US-18 | Tài khoản cá nhân |
| **RAW-024** | BQT | Phân quyền vai trò: Admin, Kế toán, Thủ quỹ | FR | Feature 5.2 / US-19 | Phân quyền RBAC |
| **RAW-025** | Kế toán | Ghi vết sửa/hủy phiếu thu tiền | FR/NFR | Feature 5.3 / NFR-005 | Chống thất thoát |
| **RAW-026** | Cư dân | Cổng xem công nợ trực tuyến cho cư dân | FR | Roadmap v2.0 | Tương lai v2.0 |
| **RAW-027** | BQT | Quản lý phí gửi xe máy (70k), ô tô (1.200k) | FR | Roadmap v2.0 | Mở rộng v2.0 |
| **RAW-028** | BQT | Thu hộ tiền điện, nước, viễn thông theo cước | FR | Roadmap v2.0 | Mở rộng v2.0 |
| **RAW-029** | Kế toán | Sao lưu dữ liệu dự phòng thường xuyên | NFR | NFR-007 / Technical Story | Hạ tầng hệ thống |
| **RAW-030** | Thủ quỹ | Giao diện tiếng Việt rõ ràng, dễ thao tác | NFR | NFR-008 / Definition of Done | Trải nghiệm người dùng |
| **RAW-031** | TDP | Dữ liệu nhân khẩu chuẩn hóa theo VNeID/CCCD | FR/NFR | NFR-006 / Feature 1.2 | Quy định pháp luật |
| **RAW-032** | Kế toán | Xuất danh sách ra file Excel hoặc PDF | FR | Feature 1.4, Feature 4.2 | In ấn/Niêm yết |
