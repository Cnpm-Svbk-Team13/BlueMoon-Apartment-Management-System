# Kiểm tra mô hình dữ liệu Sprint 1 — 04/11/2026

Truy vết: US-001 / RAW-001, RAW-002; US-SEC-01/02 / NFR-02; DB-01, DB-02, BE-01/02/03; QA-05/BA-03.

## Kết quả thực chạy

- PostgreSQL **17.11**, image `postgres:17-alpine`, container tên ngẫu nhiên `bluemoon-erd-*`, `--network none`, không publish cổng, không gắn volume.
- Lượt cuối: **60/60 kiểm tra đạt**, gồm kiểm tra thiết lập/đối soát fixture, ràng buộc/rollback và **6 kịch bản hai kết nối**. Đây không phải 60 ca UAT hoặc kiểm thử Java.
- Seed: 4 users, 11 persons, 7 apartments, 3 households, 6 residents, 4 household_head_history, 4 audit_logs.
- Bootstrap lại, seed lặp và seed trên dữ liệu một phần đều bị từ chối; dữ liệu vẫn được bảo toàn theo các kiểm tra đối soát.
- Kiểm tra các FK người, unique tài khoản/người, email trong tập tài khoản (tạo/sửa), contact bắt buộc; CCCD/SĐT; loại độc lập tầng; biên diện tích/tầng/loại; hộ cố định căn; một hộ ACTIVE; HEAD và ngày cư trú; ba dạng lịch sử, snapshot không bị sửa theo hồ sơ hiện tại, actor, append-only và truy căn qua hộ.
- Kiểm tra hoãn tới cuối transaction cho phép thay chủ và cập nhật quan hệ cùng transaction; trạng thái cuối không nhất quán bị từ chối. Lỗi ghi lịch sử hoàn tác cập nhật hồ sơ trong cùng transaction.

- Kiểm tra tĩnh: tập cột ERD và SQL khớp 7/7 bảng; các liên kết file cục bộ trong tài liệu thay đổi đều tồn tại; `git diff --check` không báo lỗi. Không có thay đổi Java/build.

## Hai kết nối

A giữ khóa đến COMMIT, B ghi trong lúc A chưa commit. B phải chờ, sau đó thấy dữ liệu đã commit và từ chối xung đột. Runner xác nhận A đang ở PgSleep trước khi chạy B, kiểm tra thời gian chờ >1 giây và SQLSTATE cụ thể.

| Kịch bản | Thời gian B chờ (giây) | Kết quả |
|---|---:|---|
| Hai người cùng tạo tài khoản với email trùng | 3.85 | B bị chặn 23505 |
| Đổi email người có tài khoản / tạo tài khoản người khác | 3.84 | B bị chặn 23505 |
| Tạo tài khoản / đổi email tài khoản khác về cùng email | 4.03 | B bị chặn 23505 |
| Tạo tài khoản / xóa SĐT của người vừa có tài khoản | 3.84 | B bị chặn 23514 |
| Hai tài khoản cùng đổi sang một email | 3.81 | B bị chặn 23505 |
| Đổi chủ hộ / đặt lại quan hệ HEAD cho chủ cũ | 4.04 | B bị chặn 23514 |

Sau tranh chấp, đối soát không có email tài khoản trùng và không có HEAD sai.

## Lệnh tái lập

```powershell
python database/tests/verify_erd_revision.py
git diff --check
```

Runner dùng Python chuẩn và Docker, tự tạo PostgreSQL cô lập rồi dọn container trong `finally`. `trust` chỉ dành cho container không mạng này, không dùng trên VM. PostgreSQL phải có image sẵn hoặc Docker được phép tải image. Chạy script SQL cũ `sprint1_schema_checks.sql` chỉ với schema lịch sử 02/11, không dùng cho schema mới.

## Trạng thái và giới hạn

- **Đã thiết kế:** ERD 7 bảng, khóa/phụ thuộc hàm, ánh xạ Entity/DTO và hợp đồng transaction.
- **Đã triển khai:** bootstrap SQL, seed, trigger, bộ kiểm tra và tài liệu. Không triển khai chức năng Java.
- **Đã thực chạy:** PostgreSQL 17.11, 60 kiểm tra nêu trên; không dùng H2 làm bằng chứng DDL.
- **Chưa nghiệm thu:** Service/Controller, RBAC, UAT, Sprint và NFR hiệu năng. Không chạy Maven vì không sửa Java/build. Chưa chạy PostgreSQL 16, chưa áp dụng VM; không migration.
- Khóa ghi chung persons/users/households/residents yêu cầu READ COMMITTED, tuần tự hóa các transaction ghi liên quan. Mức cô lập khác bị từ chối. Chưa đo tải 200 người dùng, không cam kết thông lượng NFR.
- Trigger kiểm tra cấu trúc lịch sử, không tự sinh lịch sử hoặc xác nhận snapshot đúng với dữ liệu trước/sau thực tế; việc đó thuộc Service cùng transaction và kiểm thử tích hợp sau này.
- Snapshot JSONB và audit text là dữ liệu tài liệu có chủ đích, không được coi là bằng chứng toàn hệ thống đạt 1NF–3NF.

[Báo cáo 02/11](SPRINT1_SCHEMA_VERIFICATION.md) là bằng chứng lịch sử, không thay kết quả lần này. [ERD hiện hành](../../docs/02_design_and_architecture/erd_sprint_1_proposal.md).
