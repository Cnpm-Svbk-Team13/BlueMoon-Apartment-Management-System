# 📐 TÀI LIỆU CHỨNG MINH HỌC THUẬT KIỂM THỬ SPRINT 1
## Học phần: Nhập môn Công nghệ Phần mềm (IT3180) — SoICT / HUST
### Cơ sở Lý thuyết: Chương 8 (Kiểm thử Hộp trắng CFG) & Chương 9 (Kiểm thử Hộp đen Bảng Quyết định)

---

## 1. KIỂM THỬ HỘP TRẮNG: ĐỒ THỊ DÒNG ĐIỀU KHIỂN (CFG) CHO PHƯƠNG THỨC XÁC THỰC ĐĂNG NHẬP (`US-SEC-02`)

### 1.1. Đoạn Mã Nguồn Nghiệp vụ Xác thực (`AuthService.java`)
```java
public LoginResult authenticate(String username, String password) {
    // Nút 1: Bắt đầu, tìm kiếm người dùng theo username
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null) { // Nút 2: Rẽ nhánh kiểm tra user tồn tại
        return LoginResult.USER_NOT_FOUND; // Nút 3: Thoát - Không tìm thấy
    }

    // Kiểm tra trạng thái hoạt động (Chống cán bộ bị đình chỉ/vô hiệu hóa truy cập trái phép)
    if (!user.getIsActive()) throw new DisabledException("Tài khoản đã bị vô hiệu hóa");

    // Xử lý tự động giải phóng khóa khi hết thời hạn (Reset counter on lockout expiry)
    ZonedDateTime now = ZonedDateTime.now();
    if (user.getLockedUntil() != null && user.getLockedUntil().isBefore(now)) {
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
    }
    
    // Nút 4: Kiểm tra tài khoản có đang trong thời gian bị khóa hay không
    if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
        return LoginResult.ACCOUNT_LOCKED; // Nút 5: Thoát - Tài khoản bị khóa
    }
    
    // Nút 6: So khớp mật khẩu băm BCrypt
    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
        // Nút 7: Mật khẩu sai, tăng biến đếm
        int attempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);
        if (attempts >= 5) { // Nút 8: Rẽ nhánh kiểm tra mốc 5 lần sai
            user.setLockedUntil(now.plusMinutes(15)); // Nút 9: Khóa 15 phút
            userRepository.save(user);
            return LoginResult.LOCKED_NOW; // Nút 10: Thoát - Bị khóa ngay lập tức
        } else {
            userRepository.save(user);
            return LoginResult.BAD_CREDENTIALS; // Nút 11: Thoát - Sai mật khẩu
        }
    }
    
    // Nút 12: Mật khẩu đúng, đặt lại số lần sai
    user.setFailedAttempts(0);
    user.setLockedUntil(null);
    userRepository.save(user);
    return LoginResult.SUCCESS; // Nút 13: Thoát - Thành công
}
```

---

### 1.2. Vẽ Đồ thị Dòng Điều khiển (Control Flow Graph - CFG)
```
             (1)
              |
             (2) [user == null?]
            /   \
       True/     \False
          v       v
        (3)*     (4) [lockedUntil != null && lockedUntil > now?]
                /   \
           True/     \False
              v       v
            (5)*     (6) [!passwordEncoder.matches(...)?]
                    /   \
               True/     \False
                  v       v
                 (7)     (12)
                  |        |
                 (8)     (13)*
                /   \
           True/     \False
              v       v
             (9)     (11)*
              |
            (10)*
(* biểu thị nút kết thúc/return)
```

---

### 1.3. Tính toán Độ phức tạp Chu trình (Cyclomatic Complexity - McCabe)
Đồ thị dòng điều khiển CFG của luồng xác thực và khóa tài khoản có:
- $N = 13$ nút (Nút 1 đến Nút 13).
- $E = 12$ cạnh có hướng.
- $P = 4$ nút vị từ rẽ nhánh (Nút 2, Nút 4, Nút 6, Nút 8).
- $e = 5$ nút thoát/return (Nút 3, Nút 5, Nút 10, Nút 11, Nút 13).

Độ phức tạp chu trình $V(G)$ được tính toán theo 2 phương pháp chuẩn mực (SoICT Chapter 8):
1. **Theo số nút vị từ rẽ nhánh:**  
   $$V(G) = P + 1 = 4 + 1 = 5$$
   *(với $P=4$ là 4 nút điều kiện: `user == null`, `lockedUntil != null && isAfter(...)`, `!passwordEncoder.matches(...)`, `attempts >= 5`)*.
2. **Theo công thức đồ thị đa điểm thoát (Multi-exit Graph):**  
   $$V(G) = E - N + e + 1 = 12 - 13 + 5 + 1 = 5$$
3. **Thực nghiệm ma trận:** Ma trận liên thuộc các cạnh đạt $\text{Rank} = 5$, bảo đảm 5 Đường dẫn cơ sở độc lập tuyến tính.

$\Rightarrow$ Bắt buộc thiết kế **5 Đường dẫn Cơ sở Độc lập (Basis Paths)** để đạt 100% độ bao phủ nhánh rẽ.

---

### 1.4. Tập 5 Đường dẫn Cơ sở Độc lập & Ca Kiểm thử
1. **Path 1 (Không tìm thấy user):** `1 -> 2 -> 3`  
   - *Test Case TC_LOGIN_BP_01:* Nhập `username = "unknown_user"`, `password = "any"`. Kỳ vọng: Trả về `USER_NOT_FOUND`.
2. **Path 2 (Tài khoản đang bị khóa tạm thời 15 phút):** `1 -> 2 -> 4 -> 5`  
   - *Test Case TC_LOGIN_BP_02:* Nhập `username = "locked_user"` (có `locked_until = now() + 10m`). Kỳ vọng: Trả về `ACCOUNT_LOCKED`.
3. **Path 3 (Sai mật khẩu chạm mốc 5 lần $\rightarrow$ Khóa ngay lập tức):** `1 -> 2 -> 4 -> 6 -> 7 -> 8 -> 9 -> 10`  
   - *Test Case TC_LOGIN_BP_03:* Nhập `username = "admin"`, sai mật khẩu khi `failedAttempts = 4`. Kỳ vọng: `failedAttempts` tăng lên 5, gán `locked_until = now() + 15m`, trả về `LOCKED_NOW`.
4. **Path 4 (Sai mật khẩu dưới 5 lần):** `1 -> 2 -> 4 -> 6 -> 7 -> 8 -> 11`  
   - *Test Case TC_LOGIN_BP_04:* Nhập `username = "admin"`, sai mật khẩu khi `failedAttempts = 2`. Kỳ vọng: `failedAttempts` tăng lên 3, `locked_until` vẫn NULL, trả về `BAD_CREDENTIALS`.
5. **Path 5 (Đăng nhập thành công):** `1 -> 2 -> 4 -> 6 -> 12 -> 13`  
   - *Test Case TC_LOGIN_BP_05:* Nhập `username = "admin"`, đúng mật khẩu `'Password@123'`. Kỳ vọng: `failedAttempts` reset về 0, `locked_until = null`, trả về `SUCCESS`.

**Các ca kiểm thử biên bổ trợ:**
- `TC_LOGIN_EDGE_01`: Cán bộ bị vô hiệu hóa (`is_active = false`) đăng nhập đúng mật khẩu $\rightarrow$ Chặn ngay, ném `DisabledException`.
- `TC_LOGIN_EDGE_02`: Tài khoản hết hạn khóa 15 phút gõ sai lần đầu $\rightarrow$ Tự động reset bộ đếm về 0, ghi nhận lần sai mới là 1.

---

## 2. KIỂM THỬ HỘP ĐEN: BẢNG QUYẾT ĐỊNH (DECISION TABLE) CHO QUẢN LÝ CĂN HỘ (`US-001`)

### 2.1. Phân tích Điều kiện & Hành động (SoICT Chapter 9)
- **Điều kiện đầu vào (Conditions):**
  - **C1:** Mã căn hộ (`room_number`) đã tồn tại chưa? (T / F)
  - **C2:** Số tầng có nằm trong dải thiết kế $1 \le \text{floor} \le 30$? (T / F)
  - **C3:** Diện tích thông thủy có strictly positive ($> 0\text{ m}^2$)? (T / F)
  - **C4:** Loại hình căn hộ có thuộc enum (`KIOT`, `COMMERCIAL`, `RESIDENTIAL`, `PENTHOUSE`)? (T / F)
  - **C5:** Loại hình có tương thích với số tầng tòa nhà BlueMoon? (T / F)
    * `KIOT`: Tầng 1
    * `COMMERCIAL`: Tầng 2–5
    * `RESIDENTIAL`: Tầng 6–29
    * `PENTHOUSE`: Tầng 30
- **Hành động hệ thống (Actions):**
  - **A1:** Lưu bản ghi căn hộ thành công vào CSDL.
  - **A2:** Báo lỗi trùng mã căn (`DuplicateRoomException`).
  - **A3:** Báo lỗi số tầng ngoài dải $1 \le \text{floor} \le 30$.
  - **A4:** Báo lỗi diện tích thông thủy phải lớn hơn 0.
  - **A5:** Báo lỗi loại hình căn hộ không hợp lệ.
  - **A6:** Báo lỗi xung đột loại hình với số tầng thiết kế.

---

### 2.2. Bảng Quyết định Kiểm thử Toàn diện (6 Rules)

| Quy tắc (Rule) | R1 | R2 | R3 | R4 | R5 | R6 |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| **Điều kiện C1 (Trùng mã phòng?)** | **True** | False | False | False | False | False |
| **Điều kiện C2 (Số tầng $1 \le \text{floor} \le 30$?)** | - | **False** (`< 1` hoặc `> 30`) | True | True | True | **True** |
| **Điều kiện C3 (Diện tích $m^2 > 0$?)** | - | - | **False** (`\le 0`) | True | True | **True** |
| **Điều kiện C4 (Loại hình hợp lệ?)** | - | - | - | **False** | True | **True** |
| **Điều kiện C5 (Tương thích Tầng & Loại hình?)** | - | - | - | - | **False** | **True** |
| **Hành động A1 (Lưu thành công CSDL)** | | | | | | **X** |
| **Hành động A2 (Báo lỗi Trùng mã căn)** | **X** | | | | | |
| **Hành động A3 (Báo lỗi Số tầng sai)** | | **X** | | | | |
| **Hành động A4 (Báo lỗi Diện tích $\le 0$)** | | | **X** | | | |
| **Hành động A5 (Báo lỗi Sai loại hình)** | | | | **X** | | |
| **Hành động A6 (Báo lỗi Xung đột Tầng & Loại hình)** | | | | | **X** | |

---

### 2.3. Bộ Ca Kiểm thử Tham số hóa Tương ứng (`ApartmentServiceTest.java`)
1. `TC_APT_DT_01` (R1): Nhập `room = "P.601"` đã có trong CSDL $\rightarrow$ Báo lỗi `DuplicateRoomException`.
2. `TC_APT_DT_02a` (R2 biên dưới): Nhập `floor = 0`, `room = "P.001"` $\rightarrow$ Báo lỗi tầng $< 1$.
3. `TC_APT_DT_02b` (R2 biên trên): Nhập `floor = 31`, `room = "P.3101"` $\rightarrow$ Báo lỗi tầng $> 30$.
4. `TC_APT_DT_03a` (R3 biên 0): Nhập `area = 0.00` $\rightarrow$ Báo lỗi diện tích phải $> 0$.
5. `TC_APT_DT_03b` (R3 biên âm): Nhập `area = -75.50` $\rightarrow$ Báo lỗi diện tích phải $> 0$.
6. `TC_APT_DT_04` (R4): Nhập `type = "WAREHOUSE"` $\rightarrow$ Báo lỗi loại hình không hợp lệ.
7. `TC_APT_DT_05` (R5 xung đột): Nhập `type = "PENTHOUSE"` tại tầng 1 $\rightarrow$ Báo lỗi xung đột tầng.
8. `TC_APT_DT_06` (R6 hợp lệ): Nhập `room = "P.702"`, `floor = 7`, `area = 85.50`, `type = "RESIDENTIAL"` $\rightarrow$ Lưu thành công vào CSDL.
