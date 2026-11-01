# Quy chuẩn hiện hành — BlueMoon BFMS

Ngày cập nhật: 01/11/2026 (điều chỉnh phân công Sprint 1). Tài liệu này ghi quyết định hiện hành; hồ sơ khai phá cũ là nguồn lịch sử, không tự thay đổi phân công Sprint.

## Nguồn chuẩn và truy vết

- [US và AC](01_requirements_and_planning/05_chi_tiet_us_va_acceptance_criteria.md): `US-001…011` là nghiệp vụ; `US-SEC-01` là tài khoản/RBAC; `US-SEC-02` là đăng nhập/đổi mật khẩu.
- [Ma trận truy vết](01_requirements_and_planning/07_traceability_matrix_va_quan_ly_rui_ro.md): RAW → US → AC; task kỹ thuật ghi thêm NFR và task ID.
- [Kế hoạch Sprint 1](KE_HOACH_CHI_TIET_SPRINT_1.md) và [backlog](sprints/sprint-1/github_issues_backlog.md): 31/10–06/11/2026; mục tiêu hoàn thành US-SEC-01, US-SEC-02, toàn bộ US-001 gồm hồ sơ/lịch sử chủ hộ.
- Core Workflow 1 là quản lý tài khoản/xác thực. Bất biến nghiệp vụ 1 là tách quỹ tự nguyện khỏi nợ bắt buộc; hai số 1 không phải cùng một yêu cầu.
- Mã NFR chuẩn là `NFR-01…05`; bảo mật dùng `NFR-02`. Các ghi chú quy chiếu US cũ chỉ dùng khi đọc lịch sử.
- [Schema](../database/schema.sql) là thiết kế dữ liệu hiện có. Task chủ hộ phải cập nhật DDL/migration và ERD trước khi nghiệm thu; đợt đồng bộ này không thay database.

## Vai trò và review

- Nguyễn Xuân Sơn: trưởng nhóm, phụ trách Database/Backend, căn hộ và chủ hộ; chốt thay đổi phạm vi/ưu tiên sau khi trao đổi với nhóm, xử lý vấn đề vượt thẩm quyền điều phối và xác nhận nghiệm thu nội bộ dựa trên AC, test, UAT.
- Phạm Nam Khánh: điều phối Sprint, BA/UAT; phụ trách PM-01 (6h/2 SP), Kanban, daily, theo dõi phụ thuộc/chậm tiến độ, chủ trì Sprint Review/Retrospective; chuẩn hóa yêu cầu, chuẩn bị/thực hiện UAT và tổng hợp hồ sơ nghiệm thu.
- Mỗi thành viên tự cập nhật task và báo trở ngại cho Khánh. Khánh kết nối người bàn giao/nhận, chuyển vướng kỹ thuật cho Sơn/Hoà theo phân hệ và chuyển đề xuất đổi phạm vi/ưu tiên cho Sơn. Khánh theo dõi thỏa thuận đầu vào/đầu ra; Sơn, Hoà và Đức Anh chịu trách nhiệm nội dung kỹ thuật của phần mình.
- PM-01 là vận hành Sprint, bao gồm tổ chức Sprint Review; không phải review mã nguồn. PM-03 (3h/1 SP) vẫn do Sơn review PR Hoà và kiểm tra bằng chứng trước nghiệm thu; Huy cung cấp test, Khánh tổng hợp UAT/hồ sơ, Sơn không làm lại báo cáo. Task thiếu bằng chứng vẫn mở. Người triển khai trình bày sản phẩm tại Sprint Review do Khánh tổ chức.
- Phân bổ hiện hành: Sơn 36h/10 SP, Khánh 22h/6 SP, Hoà 28h/8 SP, Đức Anh 28h/7 SP, Huy 24h/5 SP; tổng 138h/36 SP, 24 gói cha/44 phần việc. Đây là ước lượng, chưa xác nhận phù hợp với thời gian thực tế của từng người.
- Hoà: Tech Lead/Backend; Đức Anh: giao diện; Huy: QA. Giữ các mã TV hiện có.
- PR của Sơn cần Hoà duyệt; PR của Hoà cần Sơn duyệt. PR tác giả khác cần ít nhất một trong hai duyệt. Không tự duyệt; mỗi task có một người chịu trách nhiệm chính.
- Review kiểm tra truy vết, phân tầng, test liên quan và CI. Các cài đặt branch protection/required reviewers trên GitHub cần thiết lập riêng; file trong repo không tự bật chúng.

## Đăng nhập — US-SEC-02 / NFR-02

- Lần nhập sai mật khẩu thứ 5 liên tiếp đặt `locked_until = now + 15 phút`.
- Trong thời hạn khóa, chặn đăng nhập kể cả mật khẩu đúng; không kéo dài thời hạn bởi các lần thử bị chặn.
- Khi `now >= locked_until`, xóa khóa và đặt bộ đếm về 0 trước khi xét lần thử mới. Đăng nhập đúng cũng đặt lại bộ đếm.
- `is_active = false` luôn chặn đăng nhập, độc lập với khóa tạm thời.
- Dùng một nguồn thời gian có thể thay thế trong test (`Clock`); không dùng sleep 15 phút để kiểm thử.
- UI trả lỗi chung, không tiết lộ username tồn tại hay không. Các kết quả nội bộ như USER_NOT_FOUND không phải thông báo trực tiếp cho người dùng.
- Mã và CFG phải khớp nhau; phân biệt đếm điều kiện ghép ở mức câu lệnh với nhánh short-circuit. Không cam kết số basis paths trước khi chốt mã.

## Build, CI và coverage

- Java 25; Spring Boot 3.5.16; Maven Wrapper 3.9.9; JaCoCo 0.8.16. Không dùng JDK 21 để biên dịch cấu hình release 25.
- Windows: `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify`.
- Linux/macOS/CI: `sh ./mvnw --batch-mode --no-transfer-progress clean verify`.
- Smoke test `BlueMoonBfmsApplicationTest` dùng profile `test` và H2; không cần PostgreSQL/secret thật. Test này chỉ xác minh khởi động và kết nối H2.
- JaCoCo tạo `target/site/jacoco/index.html` và `jacoco.xml`. Báo cáo thông thường hiển thị toàn bộ mã Java để quan sát; ngưỡng nghiệm thu tính gộp các package `service/**` và `controller/**`, line coverage >=85%.
- CI nền tảng chạy verify trên PR, push vào main/develop và chạy thủ công; tải báo cáo test/coverage lên artifact. CI xanh không đồng nghĩa đã hoàn thành nghiệp vụ.
- Nghiệm thu Sprint phải chạy `.\mvnw.cmd --batch-mode --no-transfer-progress clean verify -Psprint1-quality` (Linux dùng `sh ./mvnw`), kèm test nghiệp vụ và UAT. Profile này đo coverage, không thay checklist hoàn thành US.
- Nếu chưa có test hoặc không có dữ liệu coverage, không được ghi nhận đạt ngưỡng. Smoke test chạm constructor của controller rỗng cũng không chứng minh có chức năng.
- Test DDL, partial unique index, regex CCCD và migration phải thực hiện trên PostgreSQL; H2 không chứng minh tương thích SQL.

## Skills chia sẻ trong repository

Các skill nằm tại `.agents/skills/<name>/SKILL.md`; được chia sẻ qua Git. Codex chọn theo tên/mô tả hoặc người dùng gọi trực tiếp:

- `$bluemoon-docs-audit`: rà soát tài liệu, liên kết và truy vết.
- `$bluemoon-sprint-task`: chia task 2–4 giờ và tiêu chí nghiệm thu.
- `$bluemoon-verification`: chọn kiểm tra theo loại thay đổi và báo bằng chứng thực chạy.

Skill là hướng dẫn, không chạy nền, không cấp quyền thay đổi GitHub/database. Nếu chưa thấy skill sau khi kéo code, mở phiên Codex mới. Không cần cài thêm plugin để dùng ba skill này.

## Trạng thái và tài liệu dự kiến

Phân biệt: đã thiết kế → đã triển khai → đã chạy kiểm tra → đã nghiệm thu. Mỗi báo cáo ghi lệnh, số test, kết quả và giới hạn chưa kiểm tra.

Các tệp dưới đây là sản phẩm dự kiến, không phải báo cáo đã có:

- `docs/sprints/sprint-1/UAT_SCENARIOS_SPRINT_1.md` — Khánh chuẩn bị.
- `docs/sprints/sprint-1/UAT_REPORT_SPRINT_1.md` — Khánh ghi kết quả, Sơn nghiệm thu nội bộ.
- `docs/sprints/sprint-1/SPRINT_1_TEST_REPORT.md` — Huy tổng hợp test/coverage.

Các phát hiện ngoài đợt này (đối soát seed, thiết kế cư dân nhiều căn hộ, cập nhật căn cứ pháp lý) vẫn cần xử lý riêng; đồng bộ tài liệu không phải xác nhận tuân thủ pháp lý toàn diện.
