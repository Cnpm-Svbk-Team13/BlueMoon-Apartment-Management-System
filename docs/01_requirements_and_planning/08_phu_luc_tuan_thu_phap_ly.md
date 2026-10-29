# PHỤ LỤC: YÊU CẦU TUÂN THỦ VÀ PHÁP LÝ (COMPLIANCE REQUIREMENTS)
## Dự án: Phần mềm Quản lý Thu phí Chung cư BlueMoon (BFMS)
### Tài liệu nguồn: Tab 3 ("Phụ lục") - Google Docs `CNPM_Proposal_Team13`

---

## 1. Tổng quan nguyên tắc tuân thủ

Hệ thống phần mềm quản lý chung cư BlueMoon được thiết kế và phát triển dựa trên sự tuân thủ nghiêm ngặt các quy định pháp luật hiện hành của Nhà nước và các tiêu chuẩn hành chính tại địa phương. Cụ thể bao gồm 4 nhóm yêu cầu tuân thủ cốt lõi sau:

---

## 2. Chi tiết 4 Yêu cầu Tuân thủ Cốt lõi

### 2.1. Luật Cư trú năm 2020 (Quản lý Nhân khẩu và Cư trú)
- **Quy định áp dụng:** Luật Cư trú số 68/2020/QH14 và các văn bản hướng dẫn thi hành.
- **Yêu cầu tích hợp vào phần mềm:**
  1. **Chuẩn hóa trạng thái cư trú:** Phân hệ quản lý nhân khẩu bắt buộc phải phân loại và lưu trữ chính xác 3 trạng thái theo luật định gồm:
     - **Thường trú**
     - **Tạm trú**
     - **Tạm vắng**
  2. **Quản lý thời hạn:** Đối với các trường hợp khai báo tạm trú hoặc tạm vắng, hệ thống bắt buộc phải có các trường dữ liệu ghi nhận khoảng thời gian hiệu lực (**Từ ngày... Đến ngày...**) nhằm kiểm soát sát sao thực tế cư trú.
  3. **Biểu mẫu báo cáo hành chính:** Tính năng xuất báo cáo danh sách nhân khẩu (`RAW-009`) phải cung cấp đầy đủ các trường thông tin tối thiểu theo đúng tiêu chuẩn hành chính để Ban quản trị phục vụ công tác kiểm tra, rà soát của Công an khu vực (Công an phường Văn Phú, quận Hà Đông).

---

### 2.2. Thông tư số 02/2016/TT-BXD (Quy chế Quản lý, Sử dụng Nhà Chung cư)
- **Quy định áp dụng:** Thông tư ban hành Quy chế quản lý, sử dụng nhà chung cư của Bộ Xây dựng (cùng các thông tư sửa đổi, bổ sung liên quan).
- **Yêu cầu tích hợp vào phần mềm:**
  1. **Cơ sở tính phí theo diện tích:** Dữ liệu diện tích căn hộ dùng trong công thức tính Phí dịch vụ và Phí quản lý hàng tháng phải tuân thủ quy định về diện tích sử dụng căn hộ (**diện tích thông thủy**) được ghi nhận trong hợp đồng mua bán/sở hữu hợp pháp.
  2. **Minh bạch hóa tài chính:** Giao diện tra cứu của cư dân (Web Portal) phải tuân thủ nguyên tắc công khai, minh bạch các khoản thu - chi, bảo đảm quyền được thông tin của cư dân theo đúng quy chế nhà chung cư, hạn chế tối đa tranh chấp pháp lý.

---

### 2.3. Nghị định số 93/2021/NĐ-CP (Vận động và Quản lý Quỹ Đóng góp Tự nguyện)
- **Quy định áp dụng:** Nghị định của Chính phủ về vận động, tiếp nhận, phân phối và sử dụng các nguồn đóng góp tự nguyện hỗ trợ khắc phục khó khăn.
- **Yêu cầu tích hợp vào phần mềm:**
  1. **Tính độc lập của quỹ tự nguyện:** Phân hệ Quản lý Quỹ tự nguyện (ủng hộ vì người nghèo, thiên tai, quỹ từ thiện...) phải được lập trình tách biệt hoàn toàn khỏi các khoản phí dịch vụ bắt buộc.
  2. **Nguyên tắc tài chính:** Hệ thống tuyệt đối không được gộp số tiền đóng góp tự nguyện vào tổng dư nợ bắt buộc của căn hộ, tôn trọng nguyên tắc **"tùy tâm, không áp đặt"** theo quy định pháp luật.

---

### 2.4. Quy định Hành chính và Báo cáo tại Địa phương
- **Quy định áp dụng:** Các hướng dẫn về công tác quản lý hành chính dân cư trên địa bàn quận Hà Đông, thành phố Hà Nội.
- **Yêu cầu tích hợp vào phần mềm:**
  1. Đảm bảo khả năng kết xuất dữ liệu biến động nhân khẩu, danh sách tạm trú theo định dạng chuẩn (Excel/PDF) để sẵn sàng cung cấp cho Ủy ban nhân dân cấp xã/phường hoặc Công an phường khi có yêu cầu thanh tra, kiểm tra hành chính định kỳ hoặc đột xuất.
