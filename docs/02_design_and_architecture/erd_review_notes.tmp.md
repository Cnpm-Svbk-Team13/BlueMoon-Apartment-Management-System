# Ghi chú tạm — rà soát thuộc tính và liên kết ERD Sprint 1

Ngày: 03/11/2026. Trạng thái: **đang thảo luận, chưa chốt phương án sửa**.

Phạm vi: giữ nguyên 7 thực thể hiện tại; rà soát thuộc tính, phụ thuộc hàm và liên kết. Không đề xuất thêm thực thể Floor trong ghi chú này. Nhóm cần quản lý **chủ hộ**, chưa quản lý quyền sở hữu căn hộ.

Đây là ghi chú phân tích, không thay thế quy chuẩn hiện hành, không xác nhận nghiệm thu và không phải chỉ dẫn chạy migration. Chưa sửa schema, seed hoặc mã nghiệp vụ.

## Nguồn và truy vết

- [Quy chuẩn hiện hành](../PROJECT_CONVENTIONS.md).
- [ERD Sprint 1](erd_sprint_1_proposal.md).
- [Schema thực tế](../../database/schema.sql).
- [US/AC](../01_requirements_and_planning/05_chi_tiet_us_va_acceptance_criteria.md): US-001 / RAW-001, RAW-002; đối chiếu phần cư trú với US-002/003, không tự mở rộng phạm vi Sprint 1.
- Task liên quan khi sửa thiết kế: DB-01, DB-02, BE-02, BE-03; chưa tạo task mới hoặc đổi phân công.

## 1. Phụ thuộc bắc cầu đã xác định

### F01 — `apartment_owner_history.apartment_id`

- Hiện trạng: lịch sử lưu cả `household_id` và `apartment_id`; hộ đã tham chiếu một căn hộ. FK ghép hiện tại buộc căn trong lịch sử khớp căn của hộ.
- Phụ thuộc: `history.id → household_id → apartment_id`.
- Kết luận: theo cấu trúc hiện tại, `household_id` không là siêu khóa của bảng lịch sử, `apartment_id` không thuộc khóa ứng viên; đây là vi phạm 3NF. FK ghép ngăn sai lệch nhưng không loại bỏ dư thừa.
- Hướng xem xét: bỏ `apartment_id` và liên kết trực tiếp lịch sử–căn hộ; truy xuất qua hộ. Khi đó xem xét bỏ UNIQUE `(id, apartment_id)` chỉ phục vụ FK ghép, sau khi kiểm tra mọi nơi sử dụng.
- Điều kiện cần chốt: hộ lịch sử có được đổi `apartment_id` không? Nếu cần lưu căn tại thời điểm sự kiện khác căn hiện tại của hộ, phải thiết kế lại ý nghĩa và quy tắc thời gian; không xóa cột một cách máy móc.

### F02 — `apartments.apartment_type`

- Hiện trạng: CHECK cố định tầng 1 = KIOT, tầng 2–5 = COMMERCIAL, tầng 6–29 = RESIDENTIAL, tầng 30 = PENTHOUSE.
- Phụ thuộc: `apartments.id → floor_number → apartment_type`.
- Kết luận: với quy tắc hiện tại, tầng không là siêu khóa và loại không thuộc khóa ứng viên; bảng không đạt 3NF theo phụ thuộc này.
- Hướng xem xét, chưa chốt: giữ tầng, suy ra loại khi đọc bằng quy tắc tập trung; không cần thêm bảng Floor. Cần đồng bộ cách hiển thị/nhập Loại hình trong AC nếu chọn cách này.
- Phương án khác chỉ hợp lệ khi đổi yêu cầu: nếu một tầng được phép có nhiều loại mặt bằng, loại là thuộc tính riêng của căn; khi đó phụ thuộc tầng → loại không còn đúng và CHECK hiện tại phải đổi.
- Không coi `room_number → floor_number` là lỗi: `room_number` là khóa duy nhất của căn hộ.

## 2. Trùng thông tin hoặc có thể suy diễn — không gộp chung thành lỗi 3NF

### F03 — `head_person_id` và quan hệ “Chủ hộ”

- `households.head_person_id` xác định chủ hộ; `residents.relationship_with_head` cũng có thể biểu diễn “Chủ hộ”. Hai nguồn có thể chỉ hai người khác nhau nếu không đối chiếu.
- Đây là vấn đề nhất quán liên bảng, chưa phải chứng minh vi phạm 3NF trong một bảng.
- Hướng xem xét: giữ `head_person_id` là nguồn xác định chính; không cho nhập quan hệ “Chủ hộ” trái với nguồn đó. Quan hệ vợ/chồng, con, bố/mẹ vẫn cần lưu.
- Không bỏ liên kết hộ–người chủ hộ chỉ vì có đường hộ–cư trú–người: hai đường mang ý nghĩa khác nhau. ERD hiện hành cho phép chủ hộ không trực tiếp cư trú; chưa có quyết định thay quy tắc này.
- Khi đổi chủ hộ cần rà soát quan hệ của các thành viên với chủ mới; không tự suy đoán quan hệ mới.

### F04 — `event_type` trong lịch sử

- Theo ba loại hiện tại, có thể phân loại từ cặp ID: chủ trước NULL → ASSIGNED; hai ID khác nhau → REPLACED; hai ID giống nhau → PROFILE_UPDATED.
- Đây là thuộc tính có thể suy diễn theo điều kiện. Do có NULL và ý nghĩa sự kiện, chưa kết luận máy móc như phụ thuộc hàm thông thường.
- Có thể giữ để đọc/lọc sự kiện; CHECK hiện tại kiểm soát sự tương ứng. Chưa chốt giữ hay bỏ.

### F05 — Trạng thái căn hộ và trạng thái hộ

- Chưa đủ căn cứ coi `apartments.status` thừa so với `households.status`.
- VACANT/OCCUPIED nói về sử dụng, RENOVATING nói về sửa chữa; cần làm rõ khả năng vừa có người ở vừa sửa chữa.
- Chỉ khi OCCUPIED được định nghĩa chính xác là có hộ ACTIVE mới có thể suy ra phần trạng thái này từ hộ. Không tự áp định nghĩa đó.

## 3. Vấn đề vòng đời và tên gọi, không phải lỗi 3NF

### F06 — `UNIQUE(residents.person_id)`

- Giới hạn mỗi người có một dòng cư trú trong toàn bảng, không chỉ một dòng đang hiệu lực.
- Có thể gây khó lưu lịch sử chuyển hộ: thêm dòng mới bị chặn; sửa dòng cũ không giữ lịch sử có cấu trúc trong bảng cư trú.
- Cần xác định đây là hồ sơ hiện tại hay từng lần cư trú trước khi sửa UNIQUE. Không mặc định cho phép nhiều nơi cư trú đồng thời; không mở rộng US-002/003 vào Sprint 1 khi chưa chốt phạm vi.

### F07 — Tên `owner`

- `apartment_owner_history`, `previous_owner_id`, `new_owner_id` dễ bị hiểu là quyền sở hữu căn hộ.
- Hướng đặt tên đang đề xuất: `household_head_history`, `previous_head_person_id`, `new_head_person_id`.
- Đây là sửa ngữ nghĩa tên gọi, không phải lỗi chuẩn hóa; chưa thực hiện đổi tên.

## 4. Những phần chưa có căn cứ kết luận là dư

| Thành phần | Lý do giữ hoặc tiếp tục xem xét |
|---|---|
| FK chủ trước và chủ sau trong lịch sử | Hai vai trò khác nhau; không suy chủ trước từ sự kiện liền trước nếu chưa bảo đảm chuỗi sự kiện đầy đủ. |
| Snapshot trước/sau | Hồ sơ tại thời điểm sự kiện khác hồ sơ hiện tại; một person có nhiều snapshot qua thời gian. Không dùng snapshot làm nguồn hồ sơ hiện hành. |
| Chủ hộ hiện tại và chủ mới trong lịch sử | Trạng thái hiện tại và sự kiện quá khứ khác nghĩa; cần cập nhật nhất quán trong cùng transaction. |
| `users` và `persons` cùng có tên/email/điện thoại | Chưa có quy tắc một–một khẳng định cùng một nguồn; trùng tên cột giữa các bảng không chứng minh lỗi 3NF. |
| `residents.id` và `person_id UNIQUE NOT NULL` | Hai khóa ứng viên; khóa thay thế không tự gây vi phạm 3NF. |
| `audit_logs.entity_name`, `entity_id` | Cần loại đối tượng cùng ID để xác định đích; thiếu FK kiểm chứng đích là vấn đề toàn vẹn tham chiếu, không phải chứng minh lỗi 3NF. |
| `created_at`, `updated_at` | Hai thời điểm khác nghĩa. |

## 5. Nội dung cần thảo luận tiếp

1. Loại mặt bằng luôn suy ra từ tầng hay được khai báo độc lập cho từng căn?
2. Một bản ghi hộ có gắn cố định với một căn suốt vòng đời không?
3. Chủ hộ có bắt buộc là thành viên trong danh sách cư trú không? Hiện chưa chốt thay quy tắc cũ.
4. Tình trạng sử dụng căn hộ được xác định từ dữ liệu nào; có độc lập với tình trạng sửa chữa không?
5. Hồ sơ cư trú biểu diễn hiện tại hay các lần cư trú qua thời gian?

## 6. Giới hạn bằng chứng

Đã đối chiếu tĩnh ERD và định nghĩa SQL; chưa chạy kiểm thử PostgreSQL trong đợt ghi chú này. Phát hiện không đồng nghĩa phương án sửa đã được nhóm phê duyệt. Khi triển khai cần kiểm tra dữ liệu hiện có, đồng bộ tài liệu/schema/seed và kiểm thử PostgreSQL phù hợp; không dùng bootstrap trên database đã có cấu trúc.

## Cập nhật xử lý 04/11/2026

- F01: bỏ căn khỏi lịch sử; hộ cố định căn, trigger chặn đổi.
- F02: nhóm chốt loại độc lập tầng; bỏ CHECK cố định, giữ apartment_type. Phụ thuộc cũ không còn áp dụng.
- F03: HEAD kiểm tra với head_person_id cuối transaction; chủ hộ không bắt buộc cư trú.
- F04: bỏ event_type lưu độc lập, suy từ cặp ID.
- F05: chỉ VACANT/OCCUPIED, do BQT xác nhận; sửa chữa ngoài Sprint 1.
- F06: giữ hồ sơ cư trú hiện tại, chưa triển khai lịch sử các lần cư trú.
- F07: đổi thành household_head_history và FK previous/new_head_person_id.
- Bổ sung users.person_id, hồ sơ dùng chung bao gồm nhân viên BQT. Xem ERD hiện hành và báo cáo kiểm thử mới; không coi cập nhật này là nghiệm thu nghiệp vụ.
