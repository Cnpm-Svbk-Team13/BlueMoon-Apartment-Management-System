[🏠 Trang chủ Docs](../README.md) > **Giai đoạn 2: Phân tích, Đặc tả & Thiết kế Hệ thống**

---

# PHÂN TÍCH, ĐẶC TẢ & THIẾT KẾ HỆ THỐNG - BLUEMOON BFMS
## Giai đoạn 2: Architecture, ERD, SRS & System Workflows

---

## 1. Giới thiệu Giai đoạn 2

Giai đoạn 2 chuyển đổi các yêu cầu kinh doanh và kế hoạch từ [Giai đoạn 1 (01_requirements_and_planning)](../01_requirements_and_planning/README.md) thành các mô hình kỹ thuật, kiến trúc phần mềm, cấu trúc dữ liệu và luồng xử lý chi tiết trước khi bước vào cài đặt mã nguồn.

---

## 2. Danh mục Tài liệu Kỹ thuật

```text
docs/02_design_and_architecture/
├── README.md               # Tổng quan và hướng dẫn điều hướng Giai đoạn 2
├── DE_BAI_GOC.md           # Toàn văn đề bài gốc môn học IT3180 & phân tích ảnh mẫu phiếu thu Hình 1-1
├── SRS.md                  # Tài liệu Đặc tả Yêu cầu Phần mềm (Software Requirements Specification)
├── architecture.md         # Thiết kế Kiến trúc Hệ thống (Pure Web, Layered Architecture)
├── erd.md                  # Mô hình Thực thể Liên kết (Entity-Relationship Diagram & Data Dictionary)
└── workflows.md            # Thiết kế chi tiết 4 Luồng nghiệp vụ cốt lõi (Sequence Diagrams)
```

---

## 3. Bản đồ Tra cứu Tài liệu theo Mục đích

| Tài liệu | Trọng tâm nội dung | Đối tượng chính |
|---|---|---|
| [DE_BAI_GOC.md](./DE_BAI_GOC.md) | Toàn văn đề bài bài tập lớn; phân loại phí dịch vụ (2.500 - 16.500 đ/m2), phí quản lý (>= 7.000 đ/m2), các quỹ tự nguyện; phân tích chi tiết ảnh chụp sổ thu viết tay Hình 1-1. | Giảng viên, Toàn bộ thành viên |
| [SRS.md](./SRS.md) | Tài liệu Đặc tả Yêu cầu Phần mềm chính thức: các tác nhân (Actors), phân hệ chức năng v1.0, yêu cầu phi chức năng (NFRs), và tiêu chí nghiệm thu. | BA, QA Tester, Dev Lead |
| [architecture.md](./architecture.md) | Kiến trúc hệ thống phân tầng (Presentation $\rightarrow$ Security $\rightarrow$ Service $\rightarrow$ Persistence $\rightarrow$ Database); các nguyên tắc thiết kế và quyết định công nghệ then chốt (Java 25 LTS, Spring Boot 3.5.16, PostgreSQL). | Backend Dev, Tech Lead |
| [erd.md](./erd.md) | Mô hình CSDL quan hệ gồm 11 bảng chuẩn: `users`, `apartments`, `households`, `residents`, `fee_types`, `campaign_funds`, `invoices`, `invoice_items`, `fund_contributions`, `receipts`, `audit_logs`. | Data Engineer, Backend Dev |
| [workflows.md](./workflows.md) | Biểu đồ tuần tự (Sequence Diagram) và lưu đồ (Flowchart) của **4 luồng nghiệp vụ chính**: Đăng ký/đăng nhập, Tạo khoản thu, Thu phí & phát hành biên lai, Thống kê báo cáo. | Frontend Dev, Tester, BA |

---

## 4. Chuyển tiếp Giai đoạn tiếp theo
- Triển khai CSDL và môi trường chạy: Xem [Giai đoạn 3: Vận hành, Môi trường & CSDL (03_operations_and_guides)](../03_operations_and_guides/README.md).
- Scripts SQL khởi tạo CSDL: Xem [`database/`](../../database/README.md).
