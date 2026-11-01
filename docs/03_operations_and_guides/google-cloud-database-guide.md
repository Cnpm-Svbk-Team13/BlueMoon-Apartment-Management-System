[🏠 Trang chủ Docs](../README.md) > [Giai đoạn 3: Vận hành, Môi trường & Cơ sở Dữ liệu](./README.md) > **google-cloud-database-guide.md**

---

# ☁️ CẨM NANG TRIỂN KHAI CƠ SỞ DỮ LIỆU POSTGRESQL TRÊN GOOGLE CLOUD
## Dự án: BlueMoon Facility Management System (BlueMoon BFMS)
### Học phần: IT3180 - Nhập môn Công nghệ Phần mềm | Nhóm 13 (Team 13) - SoICT / HUST
**Phụ trách chuyên môn:** TV2 Nguyễn Xuân Sơn (Database & Backend Developer)  
**Cố vấn kiến trúc:** Senior Tech Lead & Trợ giảng (TA)

---

## 📌 1. TỔNG QUAN KIẾN TRÚC TRIỂN KHAI (ARCHITECTURE OVERVIEW)

Trong giai đoạn phát triển và kiểm thử tích hợp (Staging/Demo), Đội ngũ Dự án BlueMoon BFMS cần một cơ sở dữ liệu dùng chung trên đám mây để:
1. Tất cả các thành viên (Frontend Đức Anh, QA Huy, PO Khánh) có thể kết nối đồng thời vào cùng một nguồn dữ liệu duy nhất mà không phụ thuộc vào máy cá nhân của Sơn.
2. Kiểm thử các luồng nghiệp vụ thực tế qua mạng Internet, phát hiện sớm các vấn đề về độ trễ (latency), xử lý kết nối đồng thời và bảo mật mạng.

```
       [Đức Anh - Frontend]          [Huy - QA Test]          [Khánh - PO UAT]
                 │                          │                        │
                 ▼                          ▼                        ▼
      ┌──────────────────────────────────────────────────────────────────┐
      │                        INTERNET ENCRYPTED                        │
      └──────────────────────────────────────────────────────────────────┘
                                      │
              ┌───────────────────────┴───────────────────────┐
              │                                               │
       [Cách 1: Trực tiếp qua SSL]             [Cách 2: SSH Tunneling]
       (Cổng 5432 - IP Whitelist)               (Cổng 22 - An toàn 100%)
              │                                               │
              └───────────────────────┬───────────────────────┘
                                      ▼
               ┌──────────────────────────────────────────────┐
               │         GOOGLE CLOUD COMPUTE ENGINE          │
               │  VM Instance: bluemoon-db-vm                 │
               │  OS: Ubuntu Server 24.04 LTS (e2-small)      │
               │  Region: asia-southeast1 (Singapore, ~30ms)  │
               │  Disk: 20 GB Standard Persistent Disk        │
               │  ┌────────────────────────────────────────┐  │
               │  │       DOCKER CONTAINER ENGINE          │  │
               │  │  ┌──────────────────────────────────┐  │  │
               │  │  │ Container: bluemoon-postgres     │  │  │
               │  │  │ Image: postgres:17-alpine        │  │  │
               │  │  │ Port: 5432 -> 5432               │  │  │
               │  │  │ Volume: postgres_cloud_data      │  │  │
               │  │  │ Database: bluemoon               │  │  │
               │  │  │ 11 Tables + 30 Floors Seed Data  │  │  │
               │  │  └──────────────────────────────────┘  │  │
               │  └────────────────────────────────────────┘  │
               └──────────────────────────────────────────────┘
```

---

## 💰 2. PHÂN TÍCH LỰA CHỌN PHẦN CỨNG & TỐI ƯU CHI PHÍ (COST OPTIMIZATION)

### 2.1. Tại sao chọn Compute Engine VM (IaaS) thay vì Cloud SQL (DBaaS)?
* **Google Cloud SQL (Dịch vụ Database quản lý sẵn):** Chi phí tối thiểu dao động từ **\$30 – \$55 USD/tháng** (~750.000 – 1.400.000 VND). Mức phí này quá lớn đối với ngân sách sinh viên làm bài tập lớn.
* **Compute Engine VM (Máy ảo tự quản lý):** Tận dụng gói chia sẻ CPU dòng **E2**, chi phí chỉ từ **\$0 (Free Tier)** đến **\$7 – \$14 USD/tháng** (hoặc chỉ tính phí theo giờ bật máy, tắt máy chỉ tốn vài nghìn đồng/tháng cho ổ đĩa).

### 2.2. Bảng so sánh các cấu hình VM phù hợp:

| Cấu hình | Thông số phần cứng | Chi phí ước tính | Đánh giá mức độ phù hợp cho Dự án BFMS |
|:---|:---|:---:|:---|
| **`e2-micro`** | 2 vCPUs (shared 25%), **1 GB RAM**, 20 GB Disk | **\$0 / tháng** (Thuộc GCP Free Tier nếu đặt tại US) | **Đạt mức tối thiểu.** Chạy được PostgreSQL nhưng 1GB RAM hơi hạn hẹp, dễ bị crash nếu truy vấn nặng nếu không cấu hình Swap. |
| **`e2-small`** *(Khuyên dùng)* | 2 vCPUs (shared 50%), **2 GB RAM**, 20 GB Disk | **~\$14.5 USD / tháng** (~480đ / giờ) | **ĐỘI HÌNH VÀNG (Sweet Spot).** 2GB RAM chạy cực mượt PostgreSQL 17, buffer cache dồi dào, chịu tải tốt cho 5-10 kết nối đồng thời. |
| **`e2-medium`** | 2 vCPUs, **4 GB RAM**, 20 GB Disk | ~\$29 USD / tháng | Dư thừa so với nhu cầu hiện tại của đồ án Sprint 1. |

> [!TIP]
> **MẸO TIẾT KIỆM 80% NGÂN SÁCH CỦA KỸ SƯ CLOUD:**
> 1. **Chỉ bật VM khi làm việc:** Khi cả nhóm kết thúc phiên làm việc/buổi tối đi ngủ, Sơn chỉ cần nhấn nút **STOP (Dừng)** trên Google Cloud Console. Khi máy tắt, Google chỉ tính tiền ổ cứng lưu trữ (~0.04 USD/tháng). Một tháng làm đồ án chỉ tốn chưa tới **\$2 – \$3 USD**!
> 2. **Lựa chọn Region:**
>    - Nếu muốn **miễn phí 100% Free Tier**: Chọn vùng Mỹ `us-central1` (Iowa) hoặc `us-east1` (South Carolina). Độ trễ ping từ VN khoảng 200–240ms (chấp nhận được).
>    - Nếu muốn **tốc độ nhanh nhất**: Chọn vùng `asia-southeast1` (Singapore) hoặc `asia-east1` (Taiwan). Ping chỉ 30–50ms, trải nghiệm gõ query trên VS Code mượt như chạy trên máy local.

---

## 🐧 3. LỰA CHỌN HỆ ĐIỀU HÀNH: TẠI SAO LÀ UBUNTU SERVER 24.04 LTS?

Chúng ta lựa chọn **Ubuntu Server 24.04 LTS (Noble Numbat)** (hoặc **Debian 12 Bookworm**) vì 4 lý do kỹ thuật then chốt:

1. **Tiết kiệm tài nguyên tuyệt đối (Low Resource Footprint):**
   * Ubuntu Server là bản không có giao diện đồ họa (Headless CLI). Khi khởi động xong, hệ thống chỉ ngốn **~150 MB RAM**!
   * Trong khi đó, nếu chọn *Windows Server*, riêng hệ điều hành đã nuốt chửng 2.5 GB – 3 GB RAM và phải trả thêm phí bản quyền Windows (~15–20 USD/tháng).
2. **Độ ổn định cao & Hỗ trợ lâu dài (LTS - Long Term Support):**
   * Bản phát hành LTS nhận được các bản vá bảo mật chính thức của Canonical trong 5 năm liên tục, không lo hệ điều hành bị lỗi thời hay gặp lỗ hổng zero-day.
3. **Hệ sinh thái Docker & PostgreSQL bản địa:**
   * Docker Engine và PostgreSQL chạy với hiệu năng tối ưu nhất trên nhân Linux (native Linux cgroups/namespaces), loại bỏ hoàn toàn lớp trung gian ảo hóa WSL 2 như trên Windows.
4. **Cộng đồng hỗ trợ lớn nhất thế giới:**
   * Mọi tài liệu, câu hỏi lỗi trên StackOverflow về GCP và Database đều có sẵn mẫu lệnh cho Ubuntu.

---

## 🛠️ 4. HƯỚNG DẪN CÀI ĐẶT & THIẾT LẬP CHI TIẾT TỪNG BƯỚC

### GIAI ĐOẠN 1: TẠO VÀ CẤU HÌNH MÁY ẢO TRÊN GOOGLE CLOUD CONSOLE

#### Bước 1.1: Tạo phiên bản máy ảo (Compute Engine VM Instance)
1. Đăng nhập vào [Google Cloud Console](https://console.cloud.google.com/).
2. Chọn dự án của bạn (Project).
3. Vào Menu điều hướng (Navigation Menu) $\rightarrow$ **Compute Engine** $\rightarrow$ **VM instances** $\rightarrow$ Bấm nút **CREATE INSTANCE (Tạo phiên bản)**.
4. Điền các tham số sau:
   - **Name:** `bluemoon-db-vm`
   - **Region:** `asia-southeast1` (Singapore) hoặc `us-central1` (Iowa).
   - **Zone:** Chọn mặc định (ví dụ: `asia-southeast1-b` hoặc `us-central1-a`).
   - **Machine configuration:**
     - Series: **E2**
     - Machine type: **`e2-small`** (2 vCPU, 2 GB memory) — *hoặc `e2-micro` nếu dùng Free Tier*.
   - **Boot disk (Ổ đĩa khởi động):**
     - Bấm nút **CHANGE**.
     - Operating system: **Ubuntu**
     - Version: **Ubuntu 24.04 LTS** (x86/64, amd64 noble image).
     - Boot disk type: **Standard persistent disk** (hoặc Balanced persistent disk).
     - Size: **20 GB**.
     - Bấm **SELECT**.
   - **Firewall:**
     - Tạm thời tích chọn *Allow HTTP traffic* và *Allow HTTPS traffic* (để GCP tạo sẵn network tags cơ bản).
   - **Advanced options $\rightarrow$ Networking:**
     - Trong mục **Network tags**, gõ: `bluemoon-db` rồi nhấn Enter.
5. Bấm nút **CREATE** ở cuối trang và đợi khoảng 30 giây để VM được cấp phát. Khi thấy dấu tích xanh ✅ kèm theo **External IP** (IP công khai), máy ảo đã hoạt động!

---

#### Bước 1.2: Cấu hình Tường lửa GCP (VPC Firewall Rule)
Mặc định, Google Cloud chặn toàn bộ các kết nối từ bên ngoài Internet vào VM ngoại trừ SSH (cổng 22). Chúng ta cần mở cổng `5432` cho PostgreSQL.

1. Tại thanh tìm kiếm trên cùng của Google Cloud Console, gõ **Firewall** và chọn **Firewall (VPC network)**.
2. Bấm nút **CREATE FIREWALL RULE**.
3. Điền các thông số:
   - **Name:** `allow-bluemoon-postgres`
   - **Network:** `default`
   - **Targets (Đối tượng áp dụng):** Chọn *Specified target tags*.
   - **Target tags:** Điền `bluemoon-db` (trùng với thẻ đã gắn cho VM ở Bước 1.1).
   - **Source IPv4 ranges:**
     - *Cách bảo mật an toàn nhất:* Điền địa chỉ IP công khai của các thành viên trong nhóm kèm `/32` (ví dụ: `14.232.120.45/32`). Kiểm tra IP của mình bằng cách vào trang [https://whatismyipaddress.com/](https://whatismyipaddress.com/).
     - *Cách tiện lợi cho học tập (nếu IP ở nhà đổi liên tục):* Điền `0.0.0.0/0` (Mở ra Internet, nhưng **BẮT BUỘC** phải đặt mật khẩu PostgreSQL cực mạnh $\ge 20$ ký tự như hướng dẫn ở Giai đoạn 3).
   - **Protocols and ports:**
     - Tích chọn **Specified protocols and ports**.
     - Tích chọn **TCP**, điền: `5432`.
4. Bấm **CREATE**.

---

### GIAI ĐOẠN 2: KẾT NỐI VÀO VM VÀ CHUẨN BỊ MÔI TRƯỜNG LINUX

#### Bước 2.1: Truy cập SSH vào máy ảo
Tại danh sách **VM instances**, ngay dòng `bluemoon-db-vm`, em sẽ thấy cột **Connect** có nút **SSH**. Bấm vào đó!  
Một cửa sổ dòng lệnh Linux màu đen sẽ mở ra ngay trên trình duyệt web của em.

#### Bước 2.2: Cập nhật hệ điều hành và tạo Swap Ram (Chống tràn bộ nhớ)
Dán các lệnh sau vào cửa sổ SSH:

```bash
# 1. Cập nhật danh mục gói phần mềm
sudo apt update && sudo apt upgrade -y

# 2. Tạo phân vùng Swap 2GB (Đặc biệt quan trọng cho VM 1GB - 2GB RAM để chống Out-Of-Memory)
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Kiểm tra dung lượng RAM và Swap
free -h
```
*(Em sẽ thấy hệ thống có 2GB RAM thật + 2GB Swap ảo = 4GB tổng dung lượng, đảm bảo PostgreSQL chạy ổn định tuyệt đối!)*

---

### GIAI ĐOẠN 3: CÀI ĐẶT POSTGRESQL (PHƯƠNG PHÁP DOCKER COMPOSE ĐỒNG NHẤT)

Tại sao TA khuyến nghị cài qua **Docker Compose** trên VM thay vì cài bằng `apt install postgresql`?  
Vì phương pháp này sử dụng đúng file [`docker-compose.yml`](../../docker-compose.yml) của dự án, giữ cho môi trường Cloud giống hệt 100% với môi trường máy cá nhân của Sơn, không bị xung đột phiên bản hay file cấu hình Linux phức tạp.

#### Bước 3.1: Cài đặt Docker & Docker Compose trên Ubuntu
Chạy script cài đặt chính thức của Docker (chỉ 1 phút):
```bash
# Tải và chạy script cài đặt Docker tự động
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Cấp quyền cho user hiện tại chạy Docker không cần gõ sudo
sudo usermod -aG docker $USER

# Cài đặt docker-compose plugin
sudo apt install -y docker-compose-plugin

# Khởi động lại phiên làm việc để nhận quyền
newgrp docker

# Kiểm tra phiên bản docker
docker version
docker compose version
```

#### Bước 3.2: Thiết lập thư mục triển khai và Docker Compose
Tạo thư mục riêng cho dự án trên máy ảo:
```bash
sudo mkdir -p /opt/bluemoon-db
sudo chown -R $USER:$USER /opt/bluemoon-db
cd /opt/bluemoon-db
```

Tạo file `docker-compose.yml` trên máy ảo:
```bash
cat << 'EOF' > docker-compose.yml
services:
  postgres:
    image: postgres:17-alpine
    container_name: bluemoon-postgres-cloud
    restart: always
    environment:
      POSTGRES_DB: bluemoon
      POSTGRES_USER: bluemoon_admin
      POSTGRES_PASSWORD: BlueMoonSecure2026@PlaLa
    ports:
      - "5432:5432"
    volumes:
      - postgres_cloud_data:/var/lib/postgresql/data

volumes:
  postgres_cloud_data:
EOF
```

> [!IMPORTANT]
> - **Username:** Đã chuyển thành `bluemoon_admin` (thay vì dùng user mặc định `postgres`).
> - **Password:** Đã đặt mật khẩu mạnh chuẩn doanh nghiệp `BlueMoonSecure2026@PlaLa` có chữ hoa, thường, số và ký tự đặc biệt.
> - **Restart policy:** `restart: always` giúp container tự động khởi động lại nếu máy ảo bị khởi động lại.

#### Bước 3.3: Khởi động máy chủ Database
Chạy lệnh quen thuộc mà Sơn đã thực hành:
```bash
docker compose up -d
docker compose ps
```
*(Container `bluemoon-postgres-cloud` hiển thị trạng thái `Up ... (healthy)` là máy chủ Cloud Database đã thức giấc!)*

---

### GIAI ĐOẠN 4: NẠP TOÀN BỘ SCHEMA, SEED DATA VÀ BẢN VÁ MIGRATION V1.1

Bây giờ chúng ta sẽ nạp toàn bộ cấu trúc CSDL của chung cư BlueMoon vào máy chủ Cloud.

#### Bước 4.1: Tải các file SQL từ GitHub về máy ảo
Em có thể tải nhanh các file SQL trực tiếp từ repository của dự án:
```bash
cd /opt/bluemoon-db
mkdir -p sql_init

# Clone hoặc tải các file schema từ repo chính thức của nhóm
curl -fsSL https://raw.githubusercontent.com/PhamHoa2006/BlueMoon-Apartment-Management-System/main/database/schema.sql -o sql_init/schema.sql
curl -fsSL https://raw.githubusercontent.com/PhamHoa2006/BlueMoon-Apartment-Management-System/main/database/seed_data.sql -o sql_init/seed_data.sql
curl -fsSL https://raw.githubusercontent.com/PhamHoa2006/BlueMoon-Apartment-Management-System/main/database/migrations/V1.1__fix_schema_and_seed.sql -o sql_init/v1.1.sql
```
*(Nếu repo của nhóm là Private, Sơn chỉ cần dùng lệnh `cat << 'EOF' > sql_init/schema.sql` rồi dán nội dung từ máy local lên).*

#### Bước 4.2: Thực thi nạp CSDL theo đúng thứ tự chuẩn
Thực thi tuần tự 3 tệp SQL vào container `bluemoon-postgres-cloud`:

```bash
# 1. Nạp 11 bảng cấu trúc cơ bản
docker exec -i bluemoon-postgres-cloud psql -U bluemoon_admin -d bluemoon < sql_init/schema.sql

# 2. Nạp dữ liệu mẫu 30 tầng căn hộ & tài khoản BQT
docker exec -i bluemoon-postgres-cloud psql -U bluemoon_admin -d bluemoon < sql_init/seed_data.sql

# 3. Nạp kịch bản vá lỗi Migration V1.1 (vá 7 điểm DDL/DML, chuẩn hóa BCrypt)
docker exec -i bluemoon-postgres-cloud psql -U bluemoon_admin -d bluemoon < sql_init/v1.1.sql
```

#### Bước 4.3: Kiểm tra nghiệm thu dữ liệu trên Cloud
Chạy lệnh kiểm tra số lượng bản ghi:
```bash
docker exec -it bluemoon-postgres-cloud psql -U bluemoon_admin -d bluemoon -c "
SELECT 
    (SELECT COUNT(*) FROM apartments) AS tong_can_ho,
    (SELECT COUNT(*) FROM users) AS tong_tai_khoan,
    (SELECT COUNT(*) FROM fee_types) AS tong_bieu_phi;
"
```
Kết quả hiển thị:
```text
 tong_can_ho | tong_tai_khoan | tong_bieu_phi 
-------------+----------------+---------------
          30 |              4 |             3
(1 row)
```
🎉 **Dữ liệu tòa nhà BlueMoon 30 tầng đã hiện diện trọn vẹn trên Google Cloud!**

---

### GIAI ĐOẠN 5: KẾT NỐI TỪ VS CODE & CẤU HÌNH SPRING BOOT

#### Bước 5.1: Kết nối từ VS Code của Sơn (Database Client)
1. Mở VS Code trên máy tính cá nhân của Sơn.
2. Bấm vào icon **Database Client** ở thanh công cụ bên trái $\rightarrow$ Bấm nút **`+` (Create Connection)** $\rightarrow$ Chọn **PostgreSQL**.
3. Điền thông tin kết nối máy chủ Cloud:
   - **Name:** `BlueMoon Google Cloud`
   - **Host:** `<EXTERNAL_IP_CỦA_VM>` (Lấy từ màn hình VM instances trên GCP Console, ví dụ: `34.124.78.102`).
   - **Port:** `5432`
   - **Username:** `bluemoon_admin`
   - **Password:** `BlueMoonSecure2026@PlaLa`
   - **Database:** `bluemoon`
4. Bấm **Connect**. Cây thư mục sẽ load thành công 11 bảng giống hệt như local!

---

#### Bước 5.2: Cập nhật biến môi trường cho ứng dụng Spring Boot
Để ứng dụng Spring Boot kết nối trực tiếp vào Database Google Cloud khi chạy, Sơn chỉ cần cập nhật file [`.env`](../../.env.example) hoặc thiết lập biến môi trường:

```env
# Kết nối Google Cloud PostgreSQL VM
DB_URL=jdbc:postgresql://<EXTERNAL_IP_CỦA_VM>:5432/bluemoon?sslmode=prefer
DB_USERNAME=bluemoon_admin
DB_PASSWORD=BlueMoonSecure2026@PlaLa
PORT=8080
```

Chạy kiểm tra kết nối bằng Maven Wrapper:
```powershell
.\mvnw.cmd spring-boot:run
```
Khi thấy dòng log:  
`HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@...`  
`Started BlueMoonBfmsApplication in ... seconds`  
👉 **Ứng dụng Spring Boot đã bắt tay thành công với Google Cloud Database!**

---

## 🎁 5. PHẦN BONUS: KIẾN THỨC NÂNG CAO CHO KỸ SƯ BACKEND

### Bonus 1: Kỹ thuật kết nối siêu bảo mật bằng SSH Tunnel (Zero Open Ports)
Nếu nhóm không muốn mở cổng `5432` ra Internet để tránh bị hacker quét cổng:
1. Đóng rule firewall cổng 5432 trên GCP (hoặc xóa đi). Chỉ giữ lại cổng `22` (SSH).
2. Trong extension **Database Client** trên VS Code, khi tạo kết nối:
   - Chuyển sang tab **`SSH Tunnel`** (nằm cạnh tab `Main`).
   - Tích chọn **Use SSH Tunnel**.
   - **SSH Host:** `<EXTERNAL_IP_CỦA_VM>`
   - **SSH Port:** `22`
   - **Username:** Tên tài khoản Google của Sơn (ví dụ: `xuan_son`).
   - **Private Key:** Trỏ tới file khóa cá nhân SSH (`~/.ssh/google_compute_engine` hoặc `id_rsa`).
   - Ở tab **Main**: Đổi Host thành `127.0.0.1` và Port `5432`.
👉 **Cơ chế hoạt động:** VS Code sẽ mượn kết nối SSH được mã hóa để chui thẳng vào máy chủ rồi kết nối tới PostgreSQL cục bộ. Kẻ ngoài nhìn từ Internet chỉ thấy duy nhất cổng 22, an toàn tuyệt đối 100%!

---

### Bonus 2: Thiết lập Cảnh báo Ngân sách (Google Cloud Budget Alerts)
Để đảm bảo không bao giờ bị Google trừ tiền vượt hạn mức ngoài ý muốn:
1. Vào Google Cloud Console $\rightarrow$ Menu $\rightarrow$ **Billing** $\rightarrow$ **Budgets & alerts**.
2. Bấm **CREATE BUDGET**.
3. Đặt tên: `BlueMoon-Dev-Budget`.
4. Mục **Target amount**: Điền ngân sách nhỏ (ví dụ: `100.000 VND` hoặc `5 USD`).
5. Mục **Threshold rules**: Đặt cảnh báo ở các mốc `50%`, `90%`, `100%`.
6. Tích chọn gửi email thông báo cho Sơn khi chi phí chạm ngưỡng.

---

### Bonus 3: Kịch bản Sao lưu Tự động định kỳ (Automated Backup Script)
Viết script tự động sao lưu dữ liệu mỗi đêm vào lúc 02:00 sáng và cất vào thư mục backup:
Tạo file `/opt/bluemoon-db/backup.sh` trên VM:
```bash
#!/bin/bash
BACKUP_DIR="/opt/bluemoon-db/backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
mkdir -p $BACKUP_DIR

# Sao lưu và nén tệp bằng gzip
docker exec -t bluemoon-postgres-cloud pg_dump -U bluemoon_admin bluemoon | gzip > "$BACKUP_DIR/bluemoon_$TIMESTAMP.sql.gz"

# Chỉ giữ lại các bản sao lưu trong 7 ngày gần nhất (tự xóa bản cũ hơn)
find $BACKUP_DIR -type f -name "*.sql.gz" -mtime +7 -delete

echo "Sao lưu thành công: bluemoon_$TIMESTAMP.sql.gz"
```
Cấp quyền và đưa vào cron job của Linux:
```bash
chmod +x /opt/bluemoon-db/backup.sh
# Đưa vào crontab chạy 02:00 hàng ngày
(crontab -l 2>/dev/null; echo "0 2 * * * /opt/bluemoon-db/backup.sh >> /var/log/db_backup.log 2>&1") | crontab -
```

---

## 📋 6. TỔNG KẾT BẢNG THÔNG SỐ VẬN HÀNH (STAGING ENVIRONMENT CHEAT-SHEET)

| Thông số | Giá trị triển khai Cloud | Ghi chú |
|:---|:---|:---|
| **Cloud Provider** | Google Cloud Platform (GCP) | Compute Engine |
| **Instance Name** | `bluemoon-db-vm` | Máy ảo Dedicated DB |
| **Machine Type** | `e2-small` (2 vCPU, 2 GB RAM) | Cấu hình tối ưu chi phí |
| **Operating System** | Ubuntu Server 24.04 LTS (x86_64) | Headless, tiêu thụ ~150MB RAM |
| **Swap Space** | 2 GB Swap File | Chống Out-Of-Memory |
| **Database Engine** | PostgreSQL 17 (Docker container) | Đồng bộ 100% với Local |
| **Database Name** | `bluemoon` | CSDL chính của tòa nhà |
| **Admin User** | `bluemoon_admin` | Phân quyền quản trị |
| **JDBC Connection URL** | `jdbc:postgresql://<EXTERNAL_IP>:5432/bluemoon?sslmode=prefer` | Khai báo trong `.env` |
| **Thao tác tiết kiệm** | Bấm **STOP** trên Console khi không dùng | Tiết kiệm 80% chi phí |

---
*Tài liệu thuộc phân hệ Vận hành & CSDL Sprint 1 — BlueMoon BFMS (IT3180).*
