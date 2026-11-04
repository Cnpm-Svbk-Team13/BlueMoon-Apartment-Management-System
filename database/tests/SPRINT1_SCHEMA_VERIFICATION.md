# Kiểm tra schema Sprint 1

> Hồ sơ lịch sử cho schema 02/11/2026; không chứng minh schema mới. Xem [báo cáo mới](ERD_REVISION_VERIFICATION.md).

Ngày: 02/11/2026. Truy vết: US-001 / RAW-001/002 / DB-02 / QA-05; US-SEC-01/02 / NFR-02 / DB-01.

## Phạm vi và kết quả

- Đã triển khai DDL cho 7 bảng, CHECK/FK/UNIQUE/index và 9 trigger: 5 timestamp, 2 kiểm tra chủ hộ, 2 bảo vệ audit/lịch sử.
- Đã chạy trên PostgreSQL **17.11**, image `postgres:17-alpine`, container riêng `bluemoon-sprint1-schema-check`, không publish cổng, không gắn volume của database khác.
- Khởi tạo schema: thành công, đủ 7 bảng và COMMIT.
- `sprint1_schema_checks.sql`: **39 kiểm tra đạt**, gồm 32 lỗi được chặn đúng SQLSTATE, 5 trigger timestamp, snapshot giữ nguyên khi sửa hồ sơ và rollback đổi chủ.
- Các luồng hợp lệ trong fixtures chạy thành công: bốn loại mặt bằng, nhiều người chưa có CCCD, cư trú có cùng ngày bắt đầu/kết thúc, chủ hộ đủ hồ sơ, lịch sử gán lần đầu, audit không xác định tài khoản.
- Chạy bootstrap lần hai: psql thoát **3**, báo `relation "users" already exists`; transaction bị hủy, database vẫn có 7 bảng. Không có DROP hoặc ghi đè dữ liệu.
- `git diff --check`: không phát hiện lỗi whitespace.

## Lệnh đã chạy

```text
docker run --detach --name bluemoon-sprint1-schema-check --env POSTGRES_HOST_AUTH_METHOD=trust postgres:17-alpine
docker cp database/schema.sql bluemoon-sprint1-schema-check:/tmp/schema.sql
docker cp database/tests/sprint1_schema_checks.sql bluemoon-sprint1-schema-check:/tmp/checks.sql
docker exec bluemoon-sprint1-schema-check psql -U postgres -v ON_ERROR_STOP=1 -f /tmp/schema.sql
docker exec bluemoon-sprint1-schema-check psql -U postgres -v ON_ERROR_STOP=1 -f /tmp/checks.sql
docker exec bluemoon-sprint1-schema-check psql -U postgres -Atc "SHOW server_version"
```

`trust` chỉ dùng cho container cô lập không publish cổng trong lần kiểm tra này, không dùng cho VM hoặc database dùng chung. Container được dọn sau kiểm tra. Dữ liệu thử nghiệm là giả, script kiểm tra kết thúc bằng ROLLBACK; sequence không rollback nên không dùng script này trên database dùng chung.

## Giới hạn và trạng thái nghiệm thu

- Chưa chạy trên VM Ubuntu, PostgreSQL 16 hoặc database có cấu trúc/dữ liệu cũ. Chưa có migration nâng cấp; seed/migration cũ không dùng được.
- Chưa kiểm thử cạnh tranh nhiều kết nối, rollback toàn bộ chuỗi Service+lịch sử+audit hoặc tích hợp Spring/JPA. Kiểm tra rollback hiện tại chỉ chứng minh thay đổi chủ hộ được transaction hoàn tác.
- Chưa kiểm thử chuỗi thay chủ REPLACED/PROFILE_UPDATED ở tầng nghiệp vụ; Service phải tạo snapshot và cung cấp actor trong cùng transaction. Trigger không tự suy tài khoản web và không tự sinh lịch sử.
- Kiểm tra JSONB xác nhận object và các khóa bắt buộc, không xác minh snapshot có khớp hồ sơ hoặc là dữ liệu trước/sau thực tế.
- Không triển khai nghiệp vụ đăng nhập, BCrypt hay đánh giá coverage/UAT; không chạy Maven vì không thay Java/build. H2 không được dùng để xác nhận DDL.
- Phần tài chính đã chuyển sang bản schema tham khảo; chưa xác nhận hoàn thành toàn bộ DB-01 hoặc Sprint 1. Trạng thái: **DDL đã triển khai và kiểm tra trong phạm vi trên; chưa nghiệm thu**.
