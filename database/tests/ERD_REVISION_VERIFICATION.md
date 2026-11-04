# Kiểm tra mô hình dữ liệu Sprint 1 — hardening 04/11/2026

Truy vết: US-001 / RAW-001, RAW-002; US-SEC-01/02 / NFR-02; DB-01, DB-02, BE-01/02/03; QA-05/BA-03.

## Kết quả thực chạy

- PostgreSQL **17.11** (`postgres:17-alpine`): **85/85 kiểm tra đạt**.
- PostgreSQL **16.15** (`postgres:16-alpine`): **85/85 kiểm tra đạt**.
- Mỗi lượt dùng container tên ngẫu nhiên, `--network none`, không publish cổng, không gắn volume và được dọn trong `finally`.
- Maven `clean verify`: **1/1 smoke test H2 đạt**. H2 chỉ chứng minh ứng dụng khởi động với profile test, không thay kiểm tra PostgreSQL.

85 kiểm tra gồm bootstrap/seed, fixture, FK/CHECK/UNIQUE, chuẩn hóa khóa nghiệp vụ, `moved_out_date`, ngày cư trú, snapshot JSONB, quyền `bluemoon_app`, rollback và 6 kịch bản hai kết nối. Đây không phải 85 ca UAT hoặc kiểm thử Java nghiệp vụ.

Seed maintenance bị từ chối khi writer đang hoạt động sau khoảng **2,12 giây** trên PostgreSQL 17 và **2,14 giây** trên PostgreSQL 16; transaction rollback và bảng persons trở về rỗng trước lượt seed thật. Seed lặp và seed trên dữ liệu một phần tiếp tục bị từ chối mà không làm mất dữ liệu.

## Phạm vi đã xác minh

- 7 bảng ERD/DDL khớp; bootstrap lặp bị từ chối và giữ nguyên cấu trúc.
- Username/email lowercase, room/registration code uppercase, chuỗi nghiệp vụ phải được trim.
- `THUONG_TRU` không mang khoảng ngày; `TAM_TRU/TAM_VANG` cần đủ hai ngày và ngày kết thúc không trước ngày bắt đầu.
- Hộ ACTIVE không có `moved_out_date`; hộ kết thúc cho phép ngày chưa biết, ngày đã biết không trước `moved_in_date`.
- Snapshot yêu cầu object, bốn khóa, đúng kiểu/định dạng và cho phép khóa mở rộng; lỗi snapshot rollback cả transaction.
- Role `bluemoon_app` được cập nhật bảng nghiệp vụ và thêm history, nhưng không được DELETE bảng nghiệp vụ, UPDATE/TRUNCATE history hoặc ALTER/tắt trigger.
- Append-only, actor, cặp previous ID/snapshot, HEAD, email tài khoản và các tình huống cạnh tranh vẫn giữ bất biến cuối transaction.

## Hai kết nối

Sáu kịch bản cạnh tranh dùng hai kết nối thật. Kết nối B phải chờ A COMMIT rồi nhìn thấy trạng thái mới và từ chối xung đột:

1. Hai người cùng tạo tài khoản với email trùng.
2. Đổi email người có tài khoản trong khi tạo tài khoản khác.
3. Tạo tài khoản trong khi tài khoản khác đổi sang cùng email.
4. Tạo tài khoản trong khi xóa SĐT của người đó.
5. Hai tài khoản cùng đổi sang một email.
6. Đổi chủ hộ trong khi đặt lại quan hệ HEAD cho chủ cũ.

Thời gian chờ quan sát được khoảng 3,80–4,05 giây; đối soát cuối không có email tài khoản trùng hoặc quan hệ HEAD sai.

## Lệnh tái lập

```powershell
python database/tests/verify_erd_revision.py --image postgres:17-alpine
python database/tests/verify_erd_revision.py --image postgres:16-alpine
.\mvnw.cmd --batch-mode --no-transfer-progress clean verify
git diff --check
```

Runner dùng Python chuẩn và Docker. PostgreSQL image phải có sẵn hoặc Docker được phép tải; không kết nối VM hay database dùng chung.

## Trạng thái và giới hạn

- **Đã thiết kế:** ràng buộc hardening, quyền owner/app, seed maintenance-only và hợp đồng Entity/DTO.
- **Đã triển khai:** bootstrap, permissions, seed, runner và tài liệu ERD/quy ước.
- **Đã thực chạy:** 85 kiểm tra trên cả PostgreSQL 16.15 và 17.11; Maven H2 smoke 1/1.
- **Chưa nghiệm thu:** Service/Controller, RBAC web, transaction sinh history/audit, UAT, tải NFR và Sprint. Chưa áp dụng VM; không có migration cho database đã tồn tại.
- Trigger kiểm tra cấu trúc snapshot nhưng không tự sinh lịch sử hoặc chứng minh snapshot là dữ liệu trước/sau thật; trách nhiệm này vẫn thuộc Service cùng transaction.

[Báo cáo 02/11](SPRINT1_SCHEMA_VERIFICATION.md) là bằng chứng lịch sử. [ERD hiện hành](../../docs/02_design_and_architecture/erd_sprint_1_proposal.md).
