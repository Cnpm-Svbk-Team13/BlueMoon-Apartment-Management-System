# Tổng hợp các ràng buộc đã được giao

Ngày tổng hợp: 14/11/2026

Tài liệu này gom các yêu cầu trực tiếp trong trao đổi và các quy ước dự án đang áp dụng. Đây là bản tổng hợp để tham chiếu, không thay thế nguồn chuẩn của dự án, không phải xác nhận các tính năng đã hoàn thành và không tự mở rộng phạm vi công việc.

## 1. Yêu cầu trực tiếp trong trao đổi

1. **Vai trò và phạm vi:** hỗ trợ backend, thành viên TV3; tập trung model và repository khi được giao phần đó.
2. **Tôn trọng code hiện có:** không tự ý thay đổi hoặc ghi đè phần đã có. Nếu phát hiện lỗi/điểm bất thường trước khi sửa, báo lại trước. Chỉ thay đổi phần nằm trong yêu cầu và các call site phụ thuộc trực tiếp khi được cho phép.
3. **Giải thích code:** khi thêm code, viết chú thích tiếng Việt giải thích hành vi cần thiết; không thêm chú thích rườm rà cho điều hiển nhiên.
4. **Báo cáo:** khi được yêu cầu, ghi báo cáo Markdown trong `.hoa`; phân biệt việc đã thiết kế, đã triển khai, đã chạy kiểm tra và đã nghiệm thu. `.hoa/` đang bị Git ignore; không sửa `.gitignore` chỉ để đưa báo cáo vào Git.
5. **Model/repository:** bổ sung method cần thiết theo nhu cầu của tầng gọi và schema; không tạo method công khai hàng loạt nếu chưa có người gọi hoặc use case.
6. **Visibility:** xem xét từng method theo phạm vi sử dụng. `public` chỉ khi cần gọi qua package/API phù hợp; constructor JPA và trạng thái nội bộ cần giữ giới hạn truy cập. Không biến `passwordHash` thành dữ liệu trả về cho giao diện.
7. **Phân tầng API:** FE gọi HTTP endpoint ở Controller. Controller định tuyến, kiểm tra đầu vào/quyền và gọi Service; Service thực hiện nghiệp vụ; Repository truy cập dữ liệu. Không đặt nghiệp vụ trong Controller hoặc Repository.
8. **Truy vấn:** khi rà N+1, lần theo luồng gọi và quan hệ được truy cập trước khi tối ưu; chỉ thêm fetch join/EntityGraph khi cần dữ liệu và tránh tải dư thừa.
9. **Kiểm thử gần nhất:** người dùng đã nói “Không cần viết file test” trong lượt yêu cầu giải thích Service. Áp dụng cho phạm vi lượt đó; không tự hiểu thành miễn trừ yêu cầu xác minh/test của dự án ở mọi thay đổi sau này. Khi người dùng yêu cầu cụ thể không tạo test, không tạo file test cho công việc đó.
10. **Handoff:** giải thích thay đổi Service bằng ngôn ngữ dễ hiểu, nêu giới hạn xác minh thay vì khẳng định đã chạy/đạt khi chưa có bằng chứng.

## 2. Quy ước kiến trúc và kỹ thuật dự án

Nguồn chính: `AGENTS.md`, `BLUEMOON.md`, `docs/PROJECT_CONVENTIONS.md` và `docs/sprints/sprint-1/DATA_NAMING_CONVENTIONS.md`.

- Luồng chuẩn: **Controller → Service → Repository → Entity**.
- Controller chỉ xử lý HTTP, session/quyền, DTO validation và chọn view/response. Endpoint web/REST thuộc Controller; FE không gọi Service trực tiếp.
- Service sở hữu nghiệp vụ, orchestration, phép tính và transaction. Repository đóng gói truy vấn/lưu trữ, không thay Service quyết định nghiệp vụ.
- Entity phản ánh bảng/cột/quan hệ; DTO là hợp đồng trao đổi, không trả entity nhạy cảm trực tiếp ra UI/API.
- Giữ tên/kiểu khớp schema: SQL `snake_case`, Java `camelCase`; ID `Long`; CCCD/SĐT `String`; DATE `LocalDate`; TIMESTAMPTZ `OffsetDateTime`; enum lưu theo tên.
- Tiền và diện tích dùng `BigDecimal`; PostgreSQL dùng `DECIMAL(12,2)`. Diện tích thông thủy phải dương. Không dùng `float`/`double` cho tiền hoặc diện tích.
- Entity quan hệ FK bằng tham chiếu object và `@JoinColumn`; repository method theo quy tắc Spring Data phải khớp property path thật, có thể hiểu tương đương SELECT/JOIN tương ứng.
- Dùng constructor injection; đặt transaction tại ranh giới Service phù hợp; luồng thu tiền/lập hóa đơn phải rollback theo yêu cầu và chống thanh toán đồng thời theo thiết kế.
- Bảo mật/quyền phải kiểm tra server-side; không tin ID, role, actor hoặc dữ liệu nghiệp vụ do trình duyệt tự gửi.
- Không commit secret, thông tin kết nối hoặc hồ sơ cư dân thật; chỉ dùng dữ liệu tổng hợp trong test/demo.

## 3. Bất biến nghiệp vụ cần giữ

- **Quỹ đóng góp tự nguyện tách tuyệt đối khỏi khoản phí bắt buộc:** đóng góp tự nguyện là pay-as-you-wish, không gộp vào nợ, không phạt chậm/ghi nợ xấu/ép thu; biên nhận riêng.
- **Phí bắt buộc:** phí dịch vụ theo giới hạn dự án 2.500–16.500 VND/m²/tháng; phí quản lý tối thiểu 7.000 VND/m²/tháng; tính trên diện tích sử dụng/thông thủy dương.
- **Cư trú:** phân loại Thường trú/Tạm trú/Tạm vắng; Tạm trú/Tạm vắng phải có khoảng ngày hợp lệ; CCCD 12 chữ số và duy nhất toàn cục.
- **Riêng tư:** chế độ công khai phải che CCCD và điện thoại; audit/details không ghi mật khẩu, password hash hoặc định danh cá nhân đầy đủ.
- **Account lockout:** sai mật khẩu lần thứ 5 khóa 15 phút; trong thời gian khóa chặn cả mật khẩu đúng và không gia hạn khóa do lần thử bị chặn; mở khi `now >= locked_until`, reset bộ đếm; tài khoản inactive luôn bị chặn.
- **Schema Sprint 1:** tài khoản gắn duy nhất một Person; Person là nguồn hồ sơ chung; hộ cố định một căn; chủ hộ không đồng nghĩa chủ sở hữu; lịch sử gắn với Household, không lưu apartment/event type độc lập; HEAD phải khớp chủ hộ cuối transaction.

## 4. Quy tắc thay đổi và review

- Đọc code, schema, convention, call site và test hiện có trước khi sửa; ưu tiên thay đổi chính xác, tối thiểu.
- Bảo toàn thay đổi sẵn có của người dùng; không revert thay đổi ngoài phạm vi.
- Không sửa DDL/schema/bootstrap hoặc áp dụng thao tác dữ liệu lên database chia sẻ nếu chưa được cho phép.
- Không nuốt lỗi, không trả kết quả thành công giả; giữ type safety và cập nhật call site trực tiếp bị ảnh hưởng.
- Thay đổi đáng kể cần nêu lý do; cập nhật tài liệu liên quan khi hành vi/cấu hình/route thay đổi.
- Trước handoff, báo lệnh kiểm tra, kết quả/số test, giới hạn chưa xác minh. H2 smoke test không chứng minh PostgreSQL; build xanh không đồng nghĩa nghiệm thu Sprint.
- Review chéo theo quy ước: PR của Sơn do Hoà review, PR của Hoà do Sơn review; không tự duyệt. CI không thay thế review hoặc AC/UAT evidence.

## 5. Trạng thái của bản tổng hợp

- **Đã tổng hợp:** yêu cầu trực tiếp trong trao đổi và các quy ước kỹ thuật/nghiệp vụ hiện hành đã đọc từ repository.
- **Không làm trong lượt này:** không sửa mã nguồn, không tạo test, không thay đổi `.gitignore`.
- **Giới hạn:** khi yêu cầu cụ thể mới mâu thuẫn với quy ước dự án hoặc mở rộng phạm vi, cần nêu rõ xung đột và hỏi trước thay vì tự suy diễn.
