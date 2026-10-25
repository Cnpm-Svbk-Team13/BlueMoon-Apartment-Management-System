# TÀI LIỆU KHAI PHÁ VÀ ĐẶC TẢ YÊU CẦU PHẦN MỀM (REQUIREMENT SPECIFICATION)
## Dự án: Quản lý Thu phí và Dân cư Chung cư BlueMoon (BlueMoon BFMS)

---

## 1. Mục lục tài liệu yêu cầu

Bộ tài liệu yêu cầu của dự án được biên dịch và chuẩn hóa đầy đủ từ đề bài bài tập lớn môn **Công nghệ phần mềm** (`CNPM_20261_YcBTL`), phân tách thành các tập tin Markdown tương ứng theo đúng tiến trình kỹ thuật Scrum/Agile:

| STT | Tập tin | Nội dung chính | Trọng tâm nghiệp vụ |
|:---:|---|---|---|
| **01** | [01_de_bai_tong_quan.md](./01_de_bai_tong_quan.md) | **Đề bài & Giới thiệu bài toán** | Quy mô chung cư BlueMoon (30 tầng), cơ cấu các loại phí (phí dịch vụ $2.500 - 16.500\text{ đ}/m^2$, phí quản lý $\ge 7.000\text{ đ}/m^2$, quỹ tự nguyện), **đánh giá chi tiết ảnh chụp sổ thu quỹ viết tay (Hình 1-1)**, phạm vi v1.0, gợi mở v2.0 và lưu ý công nghệ mở. |
| **02** | [02_ke_hoach_khai_pha_yeu_cau.md](./02_ke_hoach_khai_pha_yeu_cau.md) | **Kế hoạch Khai phá Yêu cầu (Elicitation Plan)** | 8 mục tiêu SMART (OBJ-01 đến OBJ-08), ma trận kỹ thuật Elicitation, cấu trúc phân tầng 3 lớp tài liệu, kịch bản phỏng vấn mẫu (Interview Script) 8 phần dành cho các Stakeholders. |
| **03** | [03_client_wishlist_va_raw_findings.md](./03_client_wishlist_va_raw_findings.md) | **Client's Wish List & Yêu cầu thô** | Bảng danh sách mong muốn thô gồm 32 mục (`RAW-001` đến `RAW-032`), bảng 8 yêu cầu phi chức năng thô (`NFR-001` đến `NFR-008`) và bảng Raw Log tổng hợp (CSV/Excel format). |
| **04** | [04_kich_ban_hien_trang_va_tuong_lai.md](./04_kich_ban_hien_trang_va_tuong_lai.md) | **Kịch bản Hiện trạng (As-Is) & Tương lai (Visionary)** | Ma trận phân tích quy trình thủ công hiện tại và các điểm đau (Pain points); ma trận dòng chảy nghiệp vụ tương lai khi số hóa bằng phần mềm (v1.0 và roadmap v2.0). |
| **05** | [05_scrum_artefacts.md](./05_scrum_artefacts.md) | **Các Scrum Artefacts chuẩn hóa** | Danh mục 6 Epics và 15 Features; Bộ 20 User Stories chuẩn INVEST (kèm vai trò, mục tiêu, giá trị nghiệp vụ, nguồn gốc RAW và ưu tiên MoSCoW); Bộ tiêu chí chấp nhận Acceptance Criteria chi tiết viết theo chuẩn **Given - When - Then (GWT)**. |
| **06** | [06_product_backlog_traceability.md](./06_product_backlog_traceability.md) | **Product Backlog & Ma trận Truy vết (Traceability Matrix)** | Product Backlog ưu tiên theo MoSCoW và phân chia Sprint 1-4; Ma trận truy vết đa chiều đạt tỷ lệ 100% từ `RAW ID` $\rightarrow$ `Epic` $\rightarrow$ `Feature` $\rightarrow$ `User Story` $\rightarrow$ `Acceptance Criteria`. |

---

## 2. Lưu ý quan trọng từ Nhóm phát triển

1. **Về Công nghệ hoàn thành dự án:**
   - Đề bài gốc có gợi ý ứng dụng desktop Java + MySQL.
   - **Tuy nhiên, theo quyết định chính thức của Nhóm:** Công nghệ để hoàn thành dự án là **hoàn toàn mở, không giới hạn & không ràng buộc**. Nhóm tự chốt kiến trúc phù hợp (ví dụ: nền tảng Web Java Spring Boot + Thymeleaf / PostgreSQL như trong kiến trúc kiến nghị tại [BLUEMOON.md](../../BLUEMOON.md)).

2. **Về Bằng chứng hiện trạng và Phân tích Hình ảnh:**
   - Trong đề bài có **Hình 1-1** minh họa mẫu danh sách các hộ góp quỹ tự nguyện viết tay năm 2020 của UBND Phường Khương Đình.
   - Chi tiết phân tích các trường thông tin (Số thứ tự, Họ tên, Địa chỉ, các cột quỹ từ thiện, chữ ký tay người nộp...) cùng các hạn chế (chữ viết tay khó đọc, nguy cơ tính nhầm, rách/ướt sổ giấy) đã được bóc tách và giải trình chi tiết trong tài liệu [01_de_bai_tong_quan.md](./01_de_bai_tong_quan.md) và [04_kich_ban_hien_trang_va_tuong_lai.md](./04_kich_ban_hien_trang_va_tuong_lai.md).
