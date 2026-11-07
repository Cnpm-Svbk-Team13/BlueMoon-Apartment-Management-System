# OOP/JPA backend — BlueMoon BFMS

## Javadoc: `@param` và `{@code ...}`

- `@param tenMoTa` ghi ý nghĩa của tham số hoặc thành phần record trong tài liệu API/Javadoc. Nó không thay đổi hành vi chương trình. Với phương thức có tham số, nên ghi khi người đọc cần hiểu định dạng, điều kiện hoặc cách dùng; không cần thêm nếu tên và kiểu đã đủ rõ.
- `{@code ...}` là cú pháp inline để hiển thị đoạn chữ/mã trong font monospace và tránh diễn giải như HTML, ví dụ `{@code personId}`. Nó chỉ là định dạng chú thích, không phải lệnh Java.
- Cả hai chỉ cần khi có ích cho người đọc. Có thể tạo tài liệu Javadoc bằng công cụ Javadoc nếu dự án cần; build Java thông thường không cần các tag này.

## Đối tượng hiện có

Các lớp trong `src/main/java/com/bluemoon/bfms/model/` là JPA entity, không còn là `record` thuần:

- `Person`: hồ sơ cá nhân dùng chung.
- `User`: tài khoản, liên kết một-một tới `Person`.
- `Apartment`: căn hộ và diện tích `BigDecimal`.
- `Household`: hộ gia đình, liên kết nhiều-một tới `Apartment` và `Person` (chủ hộ).
- `Resident`: hồ sơ cư trú, liên kết một-một tới `Person` và nhiều-một tới `Household`.
- `HouseholdHeadHistory`: sự kiện lịch sử, tham chiếu hộ, chủ hộ trước/sau và tài khoản thực hiện; snapshot lưu JSONB.
- `AuditLog`: nhật ký, tham chiếu tùy chọn tới tài khoản thực hiện.
- Các enum biểu diễn giới tính, vai trò, loại/trạng thái căn hộ, trạng thái hộ, quan hệ với chủ hộ và trạng thái cư trú.

Entity tham chiếu entity khác thay vì lưu một trường ID khóa ngoại độc lập. Ví dụ `User.person` được ánh xạ tới `users.person_id` bằng `@JoinColumn`; khi cần ID có thể lấy qua quan hệ hoặc dùng DTO có trường ID. `@ManyToOne` tương ứng quan hệ nhiều bản ghi cùng tham chiếu một bản ghi; `@OneToOne` có cột FK duy nhất.

JPA cần entity class có constructor không tham số và ID. ID dùng `IDENTITY`; enum lưu bằng tên; các timestamp được cấu hình do database/trigger quản lý. Lịch sử và audit được đánh dấu chỉ đọc (`@Immutable`) ở Hibernate.

## Repository

`src/main/java/com/bluemoon/bfms/repository/` có Spring Data `JpaRepository` cho các entity. Spring Data tạo CRUD và truy vấn theo tên phương thức, ví dụ `findByRoomNumber(...)` tương đương truy vấn theo cột mã căn. Danh sách được phân trang ở các truy vấn liệt kê.

Các phương thức đọc quan hệ thường dùng gắn `@EntityGraph` để nạp quan hệ `to-one` trong cùng truy vấn và hạn chế N+1. Đây là tối ưu cho những phương thức đã khai báo graph, không phải cam kết rằng mọi cách duyệt entity đều không phát sinh truy vấn bổ sung. `LAZY` vẫn có thể truy vấn khi truy cập quan hệ ngoài transaction hoặc qua truy vấn không có graph; cần đo SQL trên luồng thực tế nếu tối ưu sâu hơn.

## Điểm cần lưu ý / bước kế tiếp

- Entity class không còn tự có `equals`, `hashCode` và accessor theo record. Hiện có các accessor dạng `field()`; thống nhất với các tầng tiếp theo trước khi dùng rộng rãi.
- Một số phương thức nghiệp vụ tạo entity mới với cùng ID (`normalized()`, `with...()`, trạng thái đăng nhập). Khi service/JPA quản lý entity trong persistence context, cần quyết định cập nhật entity đang được quản lý hay dùng DTO/domain object riêng; tránh coi bản sao là entity được quản lý.
- Constructor hiện tại còn nhận ID và timestamp do database quản lý. Khi viết service, nên tạo entity mới mà không nhận các giá trị này từ form; để database cấp ID/thời gian.
- Chưa có DTO và service nghiệp vụ; repository không thay thế validation đầu vào, phân quyền hay transaction. Đặc biệt, đổi chủ hộ và ghi lịch sử phải được xử lý trong cùng transaction; quy tắc nhiều bản ghi phải kiểm tra ở service và được database bảo vệ khi có constraint.
- `@EntityGraph` hạn chế N+1 cho các truy vấn đã cấu hình nhưng chưa được đo bằng test truy vấn hoặc kiểm chứng trên PostgreSQL. Smoke test H2 chỉ xác nhận khởi động, không xác minh đầy đủ PostgreSQL, JSONB, constraints hay hiệu năng.
- Các phương thức truy vấn repository là điểm khởi đầu; bổ sung truy vấn theo use case, phân trang và fetch graph vừa đủ khi xây dựng service/controller, tránh nạp cả đồ thị entity không cần thiết.

Các entity và repository là nền tảng persistence đã được viết; điều đó chưa đồng nghĩa các user story đã triển khai hoặc nghiệm thu.
