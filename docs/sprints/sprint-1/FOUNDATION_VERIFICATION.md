# Bàn giao đợt đồng bộ tài liệu và nền tảng Sprint 1

Ngày kiểm tra: 31/10/2026. Truy vết: US-001 / RAW-001, RAW-002; US-SEC-01/02 / NFR-02; nền tảng NFR-05. Báo cáo này không thay `SPRINT_1_TEST_REPORT.md` hoặc UAT nghiệp vụ.

> Phân công/ước lượng dưới đây ghi nhận tại ngày kiểm tra 31/10/2026, đã được thay thế ngày 01/11/2026: PM-01 về Khánh; Sơn 36h/10 SP, Khánh 22h/6 SP. Xem [quy chuẩn hiện hành](../../PROJECT_CONVENTIONS.md). Các bằng chứng build/test lịch sử giữ nguyên; không phải kết quả chạy lại.

## Thay đổi

- Boot 3.5.16, Java 25; JaCoCo 0.8.16; Mockito nạp bằng javaagent khi test để tránh tự attach JVM.
- Smoke test H2 và workflow CI verify/read-only, lưu báo cáo; không tự deploy.
- `sprint1-quality` kiểm tra >=85% aggregate line coverage trên Service/Controller.
- Quy chuẩn chia sẻ trong AGENTS.md và [PROJECT_CONVENTIONS.md](../../PROJECT_CONVENTIONS.md); ba skill `.agents/skills/` được Git nhận, teamwork/plugins cá nhân vẫn bị bỏ qua.
- US/AC, ma trận truy vết, WBS, kế hoạch, cẩm nang và backlog đồng bộ vai trò Sơn/Khánh, review chéo, lịch và khóa đăng nhập. CFG chỉ còn thiết kế chờ mã cuối.
- Backlog bổ sung chủ hộ/review: 24 gói cha, 44 phần việc 2–4h, tổng 138h/36 SP. Sơn 42h, Hoà 28h, Đức Anh 28h, Huy 24h, Khánh 16h; cần kiểm soát năng lực mỗi ngày.

## Bằng chứng thực chạy

| Kiểm tra | Kết quả | Giới hạn |
|---|---|---|
| Maven Wrapper `clean verify` | BUILD SUCCESS, 1 test, 0 lỗi/thất bại/bỏ qua | Smoke khởi động và H2; không test nghiệp vụ |
| `clean verify -Psprint1-quality` sau cấu hình Mockito | BUILD SUCCESS, 1 test | Chỉ ba dòng constructor Controller nằm trong phạm vi hiện tại |
| Thử coverage với lớp Service tạm chưa chạy | Check thất bại đúng kỳ vọng: 0,37 < 0,85 | Fixture chỉ biên dịch trong target, không sửa source nghiệp vụ |
| Dọn fixture rồi chạy `jacoco:check@sprint1-coverage-check -Psprint1-quality` | BUILD SUCCESS | Xác nhận profile chặn ngưỡng, không xác nhận hoàn thành US |
| Ba skill: `python -X utf8 .../quick_validate.py <skill-folder>` | Cả ba: Skill is valid | Kiểm tra manifest, không chứng minh mọi hành vi tự chọn skill |
| Liên kết Markdown tài liệu hiện hành | Không có link local hỏng | Báo cáo UAT/test tương lai đã ghi bằng tên file, không giả là có sẵn |
| Bảng phần việc con | 24 gói/44 phần việc; từng phần 2–4h; tổng 138h | Không đồng nghĩa đã hoàn thành hoặc đủ năng lực |
| YAML workflow và XML Maven | Parse và kiểm tra cấu trúc thành công | GitHub Actions chưa chạy trên remote |

## Thử áp dụng ba skill trên yêu cầu đại diện

Thực hiện trực tiếp trong chat này, không gọi agent độc lập và không tạo hành động GitHub:

- `bluemoon-docs-audit`: yêu cầu rà soát Sprint 1 → đọc nguồn chuẩn, tìm và sửa tham chiếu bảo mật, lịch, reviewer, liên kết báo cáo dự kiến; kiểm tra lại không còn link hỏng trong tài liệu hiện hành. Các hồ sơ audit cũ bị loại khỏi nguồn hiện hành.
- `bluemoon-sprint-task`: yêu cầu bổ sung chủ hộ trong tuần đầu → tạo DB-02/BE-03/FE-05/QA-05/BA-03, gắn US-001/RAW-002/AC 3–4; tách phần việc, kiểm tổng 138h và khai báo thêm review PM-03. Không tạo issue trên GitHub.
- `bluemoon-verification`: yêu cầu kiểm tra nâng Boot → chọn smoke H2 và coverage check dương/âm; báo riêng PostgreSQL, nghiệp vụ/UAT và remote CI chưa được xác minh. Không suy ra Sprint Done từ coverage mã khung.

## Còn phải làm trong Sprint

- Triển khai/test đăng nhập, RBAC, tài khoản, căn hộ, hồ sơ/lịch sử chủ hộ theo backlog; hoàn thiện CFG từ mã cuối.
- Chạy migration/constraints trên PostgreSQL cô lập và kiểm tra rollback/cạnh tranh khi đổi chủ; đợt này không chạy SQL hoặc thay dữ liệu.
- Chạy CI trên GitHub và cấu hình quy tắc branch/reviewer ở dịch vụ nếu nhóm muốn cưỡng chế. Không tạo PR/issue trong đợt này.
- Thực thi UAT và đo coverage trên mã nghiệp vụ thật trước nghiệm thu. Hiện ba constructor được phủ 100%; đó không phải tỷ lệ hoàn thành chức năng.
- Cảnh báo JDK từ thư viện Maven, cảnh báo H2Dialect và mật khẩu phát triển tự sinh vẫn có thể xuất hiện; chúng không được mô tả là bản build “không cảnh báo”.
- Các việc ngoài phạm vi như đối soát seed, mô hình cư dân nhiều căn hộ và cập nhật căn cứ pháp lý cần xử lý riêng.
