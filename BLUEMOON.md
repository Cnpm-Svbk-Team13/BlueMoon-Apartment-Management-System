# BlueMoon BFMS

**Phần mềm Quản lý Thu phí Chung cư BlueMoon** (BlueMoon Facility Management System — **BFMS**) là ứng dụng web hỗ trợ Ban quản trị, bộ phận Kế toán và cư dân thực hiện các nghiệp vụ quản lý chung cư trên một hệ thống tập trung.

> **Phiên bản khởi động:** Java Spring Boot, Spring MVC, Thymeleaf và PostgreSQL. Ứng dụng kết nối tới PostgreSQL qua cấu hình môi trường; không lưu database trong repository. Cấu trúc trong tài liệu là cấu trúc Maven đề xuất để các thành viên và AI Agent cùng phát triển nhất quán.

## 1. Tổng quan

- **Trang Quản trị (Admin Panel):** dành cho Kế toán/Ban quản trị đăng nhập, quản lý nhân khẩu và căn hộ, thiết lập/tính phí, theo dõi hóa đơn và xuất báo cáo PDF.
- **Cổng Cư dân (Web Portal):** cư dân tra cứu công nợ, hóa đơn và gửi phản ánh (ticket) qua trình duyệt.
- **Backend:** Spring Boot và Spring MVC tiếp nhận request, kiểm tra quyền, điều phối nghiệp vụ và render trang Thymeleaf.
- **Database:** PostgreSQL chạy như database server; ứng dụng truy cập qua JDBC và Spring Data JPA/Hibernate.

Đây là **Pure Web Architecture**: trình duyệt chỉ giao tiếp với ứng dụng web qua HTTP/HTTPS, không kết nối trực tiếp tới database.

## 2. Kiến trúc phân tầng và luồng dữ liệu

```text
┌─────────────────────────┐       HTTP/HTTPS       ┌─────────────────────────────┐
│ Admin Panel             │ ─────────────────────► │ Spring Boot / Spring MVC    │
│ Browser + Thymeleaf HTML│                        │                             │
└─────────────────────────┘                        │ Controller → Service →      │
                                                   │ Repository → Entity         │
┌─────────────────────────┐       HTTP/HTTPS       │                             │
│ Cổng Cư dân             │ ─────────────────────► │ Thymeleaf + Static assets   │
│ Browser + HTML/CSS/JS   │                        └──────────────┬──────────────┘
└─────────────────────────┘                                       │ JDBC/JPA
                                                                  ▼
                                                   ┌─────────────────────────────┐
                                                   │ PostgreSQL                  │
                                                   │ Database: bluemoon          │
                                                   └─────────────────────────────┘
```

1. Người dùng gửi request từ trình duyệt đến ứng dụng Spring Boot.
2. Controller nhận request, xác thực dữ liệu đầu vào và gọi service phù hợp.
3. Service thực thi quy tắc nghiệp vụ, sau đó dùng repository để đọc/ghi entity trong PostgreSQL.
4. Controller trả về view Thymeleaf hoặc chuyển hướng; tài nguyên CSS/JS/ảnh được phục vụ từ `static/`.

**Trách nhiệm từng tầng:** Controller không chứa nghiệp vụ phức tạp; Service không phụ thuộc vào giao diện web; Repository đóng gói truy cập dữ liệu; Entity ánh xạ bảng; DTO là dữ liệu trao đổi giữa các tầng/giao diện khi cần.

## 3. Cây thư mục Maven Standard Structure

Ví dụ dùng package gốc `com.bluemoon.bfms`; các package Java được đặt dưới `src/main/java/` theo cấu trúc Maven:

```text
bluemoon-bfms/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/
│   └── wrapper/
├── .gitignore
├── README.md
├── docs/
│   ├── README.md                # Cổng điều hướng tài liệu trung tâm
│   ├── 01_requirements_and_planning/ # Giai đoạn 1: Khai phá & Kế hoạch
│   ├── 02_design_and_architecture/   # Giai đoạn 2: SRS, Kiến trúc, ERD
│   └── 03_operations_and_guides/     # Giai đoạn 3: Cài đặt, GCP, Docker



│   └── local-development.md     # Hướng dẫn chạy và sao lưu local
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── bluemoon/
    │   │           └── bfms/
    │   │               ├── BlueMoonBfmsApplication.java
    │   │               ├── config/         # Cấu hình MVC, bảo mật, beans
    │   │               ├── controller/
    │   │               │   ├── AdminController.java
    │   │               │   ├── PortalController.java
    │   │               │   └── AuthController.java
    │   │               ├── service/        # Quy tắc và điều phối nghiệp vụ
    │   │               ├── repository/     # Spring Data JPA repositories/DAO
    │   │               ├── model/          # JPA entities ánh xạ bảng PostgreSQL
    │   │               ├── dto/            # Request/response/view data objects
    │   │               ├── exception/      # Exception miền và xử lý lỗi chung
    │   │               └── util/           # Tiện ích dùng chung, không chứa nghiệp vụ
    │   └── resources/
    │       ├── application.yml             # Cấu hình Spring và PostgreSQL
    │       ├── templates/
    │       │   ├── admin/                  # Trang Thymeleaf cho quản trị
    │       │   ├── portal/                 # Trang Thymeleaf cho cư dân
    │       │   ├── auth/                   # Đăng nhập/đăng xuất
    │       │   └── fragments/              # Layout, navigation, component dùng chung
    │       └── static/
    │           ├── css/
    │           ├── js/
    │           └── images/
    └── test/
        └── java/
            └── com/
                └── bluemoon/
                    └── bfms/
                        ├── controller/
                        ├── service/
                        └── repository/
```

Các thư mục package phản chiếu tên package Java: lớp ứng dụng khai báo `package com.bluemoon.bfms;`, còn lớp trong `controller/` khai báo `package com.bluemoon.bfms.controller;`. Cây test dùng cùng package tương ứng với mã nguồn chính.

### Ý nghĩa các thành phần chính

| Thành phần | Trách nhiệm |
|---|---|
| `controller/` | Tiếp nhận HTTP request, kiểm tra dữ liệu web, gọi service và chọn view/redirect. |
| `service/` | Chứa nghiệp vụ như tính phí, lập hóa đơn, kiểm tra quyền truy cập và xử lý ticket. |
| `repository/` | Truy vấn và lưu entity bằng Spring Data JPA/DAO; không chứa quy tắc nghiệp vụ. |
| `model/` | JPA entities và mapping quan hệ giữa entity với bảng PostgreSQL. |
| `dto/` | Đối tượng dữ liệu dùng cho request/response hoặc truyền dữ liệu lên view; tránh đưa entity trực tiếp ra ngoài khi không phù hợp. |
| `templates/` | Template Thymeleaf được chia theo Admin, Portal, Auth và fragments dùng chung. |
| `static/` | CSS, JavaScript và hình ảnh tĩnh; không đặt dữ liệu bí mật tại đây. |
| `PostgreSQL` | Database server lưu dữ liệu; thông tin kết nối được cấu hình qua biến môi trường và không commit secret vào Git. |
| `docs/` | SRS, kiến trúc, ERD, quy trình nghiệp vụ và tài liệu vận hành; đặt trực tiếp các tài liệu tại đây để tránh thư mục trung gian chỉ có một tệp. |
| `src/test/` | Unit và integration tests, theo package tương ứng với mã nguồn chính. |
| `pom.xml` | Khai báo phiên bản Java, dependencies, plugins và cấu hình build Maven. |
| `application.yml` | Cấu hình ứng dụng; thông tin thay đổi theo môi trường nên được đưa ra environment variables khi cần. |

Không bắt buộc tạo mọi thư mục rỗng ngay từ đầu. Chỉ thêm package/file khi có chức năng thực sự cần đến.

## 4. Yêu cầu môi trường

- **JDK 25 LTS** (hoặc tối thiểu JDK 21 trở lên).
- **Apache Maven 3.9 trở lên**, hoặc dùng Maven Wrapper (`mvnw`/`mvnw.cmd`) để chuẩn hóa phiên bản Maven trong nhóm.
- Git và terminal.
- Cài đặt và khởi chạy PostgreSQL server local hoặc sử dụng một instance PostgreSQL được cấp cho nhóm.
- Tạo database `bluemoon` trước khi chạy ứng dụng; cấu hình URL, username và password bằng biến môi trường.

Kiểm tra cài đặt:

```bash
java -version
mvn -version
```

## 5. Cấu hình PostgreSQL

### Khai báo dependencies trong `pom.xml`

Spring Data JPA cung cấp lớp ORM/repository; PostgreSQL JDBC driver cung cấp kết nối tới PostgreSQL. Hibernate nhận diện dialect PostgreSQL tự động, không cần khai báo dialect riêng.

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

Dùng Spring Boot parent/BOM để quản lý phiên bản dependencies; không tự chọn phiên bản PostgreSQL JDBC driver nếu phiên bản Spring Boot đang dùng đã quản lý dependency này.

### Cấu hình `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/bluemoon}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false

server:
  port: 8080
```

Tạo database `bluemoon` trên PostgreSQL trước khi chạy ứng dụng. Cấu hình `DB_URL`, `DB_USERNAME` và `DB_PASSWORD` trong môi trường chạy; không ghi mật khẩu thật vào `application.yml` hoặc commit lên Git. Ví dụ URL mặc định kết nối tới PostgreSQL local ở cổng `5432`. `ddl-auto: update` chỉ phù hợp để bắt đầu phát triển, không thay thế migration có kiểm soát cho dữ liệu quan trọng.

## 6. Cài đặt và chạy thử

### Windows PowerShell

```powershell
git clone <repository-url>
cd <repository-folder>
# Đảm bảo PostgreSQL đang chạy, database bluemoon đã được tạo,
# và DB_USERNAME / DB_PASSWORD đã được cấu hình trong môi trường.
mvn spring-boot:run
```

Nếu Maven Wrapper có trong repository, có thể chạy thay bằng:

```powershell
.\mvnw.cmd spring-boot:run
```

### macOS/Linux

```bash
git clone <repository-url>
cd <repository-folder>
# Đảm bảo PostgreSQL đang chạy, database bluemoon đã được tạo,
# và DB_USERNAME / DB_PASSWORD đã được cấu hình trong môi trường.
./mvnw spring-boot:run
```

Nếu không có Maven Wrapper, dùng `mvn spring-boot:run`.

Khi ứng dụng khởi động thành công, truy cập `http://localhost:8080/`. Các route Admin/Portal được bổ sung theo controller và chức năng được triển khai. Build và chạy tests bằng:

```bash
mvn test
mvn package
```

## 7. Quy ước đặt tên

| Thành phần | Quy ước | Ví dụ |
|---|---|---|
| Package Java | chữ thường, theo cấu trúc thư mục | `com.bluemoon.bfms.service` |
| Class/interface/record/enum | `PascalCase` | `InvoiceService`, `InvoiceCreateRequest` |
| Method/field/biến | `camelCase` | `calculateMonthlyFee()` |
| Hằng số | `UPPER_SNAKE_CASE` | `DEFAULT_PAGE_SIZE` |
| Entity | tên miền số ít, `PascalCase` | `Resident`, `Invoice`, `Apartment` |
| Repository | `<Entity>Repository` | `InvoiceRepository` |
| Service | `<Domain>Service` | `InvoiceService` |
| Controller | theo khu vực/chức năng + `Controller` | `AdminController`, `PortalController` |
| Thymeleaf template | chữ thường, theo khu vực và tính năng | `templates/admin/invoices/list.html` |
| Bảng/cột database | `snake_case`, thống nhất toàn dự án | `invoices`, `created_at` |
| Test | `<ClassUnderTest>Test` hoặc tên hành vi rõ ràng | `InvoiceServiceTest` |

Dùng một thuật ngữ miền thống nhất trong code, giao diện và tài liệu; tránh tên viết tắt không phổ biến.

## 8. Coding Guidelines cho thành viên và AI Agent

1. **Đọc trước khi sửa:** kiểm tra cấu trúc, conventions, dependencies, route và tests hiện có. Mở rộng pattern của dự án thay vì tạo framework hoặc tầng mới không cần thiết.
2. **Giữ đúng phân tầng:** Controller xử lý HTTP/view; Service chứa nghiệp vụ; Repository truy cập persistence; Entity mô tả dữ liệu lưu trữ; DTO là hợp đồng trao đổi. Không đặt nghiệp vụ trong controller hoặc template.
3. **Constructor injection:** ưu tiên constructor injection (có thể dùng `final` fields) thay cho field injection.
4. **Validation và lỗi:** dùng Jakarta Bean Validation cho input; trả lỗi rõ ràng qua exception handler hoặc view phù hợp. Không nuốt exception, không trả dữ liệu thành công giả.
5. **Database:** đặt transaction ở service boundary phù hợp; dùng query có tham số/JPA, không ghép SQL từ input. Thay đổi schema cần có migration/kế hoạch nâng cấp và phải giữ tương thích với PostgreSQL.
6. **Bảo mật:** kiểm tra xác thực và quyền ở server; không tin dữ liệu từ trình duyệt; không lưu mật khẩu dạng rõ; không commit secret hoặc dữ liệu cá nhân. Escape dữ liệu động khi render HTML.
7. **Thymeleaf:** giữ trang Admin và Portal tách riêng; dùng fragment/layout chung khi hợp lý. Không đưa logic nghiệp vụ phức tạp vào biểu thức template.
8. **Kiểm thử:** thêm/cập nhật unit tests cho nghiệp vụ và integration tests cho repository/controller khi cần. Test phải dùng dữ liệu cô lập, không phụ thuộc database của người dùng.
9. **Dependencies:** chỉ thêm thư viện khi cần; ghi nhận trong `pom.xml`, ưu tiên phiên bản được Spring Boot quản lý.
10. **Thay đổi có phạm vi:** không sửa file/dữ liệu không liên quan; cập nhật README/docs khi thay đổi cách build, cấu hình, route hoặc hành vi đáng kể.

## 9. Dữ liệu và sao lưu

PostgreSQL lưu dữ liệu ngoài repository. Không commit thông tin xác thực hoặc bản sao lưu chứa dữ liệu cư dân/tài chính; chỉ dùng dữ liệu giả trong môi trường phát triển. Sao lưu và phục hồi bằng công cụ PostgreSQL phù hợp (ví dụ `pg_dump` và `pg_restore`) theo chính sách của môi trường triển khai.

## 10. Tài liệu dự án

- `docs/SRS.md`: SRS và yêu cầu nghiệp vụ.
- `docs/architecture.md`, `docs/erd.md`, `docs/workflows.md`: kiến trúc, ERD và luồng nghiệp vụ.
- `docs/local-development.md`: hướng dẫn chạy, cấu hình và sao lưu.
