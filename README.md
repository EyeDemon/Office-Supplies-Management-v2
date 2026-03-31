# 🏢 Quản Lý Văn Phòng Phẩm — v1.1.0 (Fixed)

Hệ thống quản lý người dùng theo kiến trúc **Clean Architecture**.

---

## ⚡ Bắt đầu nhanh (3 bước)

### Bước 1 — Cấu hình database
Mở file `backend/src/main/resources/application.properties` và đặt đúng mật khẩu MySQL:
```properties
db.password=your_mysql_password
# Nếu root không có mật khẩu, để trống:
db.password=
```

### Bước 2 — Khởi tạo database
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/data.sql
```
Tài khoản mẫu sau khi import: mật khẩu tất cả là **123456**
| Username | Role |
|---|---|
| admin | ADMIN |
| manager | ADMIN |
| nhanvien1–8 | USER |

### Bước 3 — Build & Run
```bash
cd backend
mvn clean package cargo:run -DskipTests
```
Truy cập: **http://localhost:8080**

---

## 🏗 Cấu trúc Clean Architecture

```
backend/src/main/java/com/qlvanphongpham/
├── domain/                          ← Enterprise Business Rules
│   ├── User.java                    ← Domain Entity
│   └── valueobjects/
│       ├── Email.java
│       ├── Password.java
│       └── PhoneNumber.java
│
├── usecase/                         ← Application Business Rules
│   ├── boundary/
│   │   ├── input/                   ← Input Ports (interfaces)
│   │   │   ├── ManageUserInputBoundary.java
│   │   │   └── GetUserInputBoundary.java
│   │   └── output/                  ← Output Ports (interfaces only)
│   │       └── ManageUserOutputBoundary.java
│   ├── interactor/                  ← Use Case implementations
│   │   ├── ManageUserInteractor.java
│   │   └── GetUserInteractor.java
│   ├── port/
│   │   ├── UserRepository.java      ← Repository Port
│   │   └── PasswordHasher.java      ← Security Port
│   ├── request/                     ← Input DTOs
│   └── response/                    ← Output DTOs
│
└── adapter/                         ← Interface Adapters
    ├── controller/                  ← HTTP Servlet Controllers
    │   ├── AuthController.java
    │   └── UserController.java
    ├── persistence/                 ← Repository implementations (JDBC)
    │   ├── DatabaseConnection.java
    │   └── UserRepositoryImpl.java
    ├── presenter/                   ← Output Boundary implementations
    │   └── UserPresenter.java       ← ✅ Đúng vị trí (adapter layer)
    ├── security/
    │   └── PasswordHasherImpl.java  ← BCrypt
    └── viewmodel/
        ├── UserViewModel.java
        └── CreateUserForm.java
```

---

## 🔧 Các vấn đề đã sửa (v1.0 → v1.1)

| # | Vấn đề | Loại | Trạng thái |
|---|---|---|---|
| 1 | `db.password=YOUR_PASSWORD` gây Access Denied | Bug nghiêm trọng | ✅ Fixed |
| 2 | Gson không serialize `LocalDateTime` → trả về `{}` | Bug runtime | ✅ Fixed |
| 3 | `LoginResponse` chứa domain entity `User` thay vì DTO | Vi phạm CA | ✅ Fixed |
| 4 | `UserPresenter` đặt sai trong `usecase/boundary/output` | Vi phạm CA | ✅ Fixed |
| 5 | `ManageUserInteractor` trộn 2 paradigms không nhất quán | Vi phạm CA | ✅ Fixed |
| 6 | `User.setActive()` và `User.setPhone()` là no-op stubs | Code smell | ✅ Fixed |
| 7 | `ResultSet` không đóng đúng cách trong repository | Bug tiềm ẩn | ✅ Fixed |
| 8 | `DatabaseConnection` load driver mỗi lần gọi | Performance | ✅ Fixed |

---

## 🛠 Tech Stack
- **Backend**: Java 21, Jakarta Servlet 6.0, Gson, BCrypt, MySQL 8
- **Frontend**: React 18, React Router 6, Bootstrap 5, Axios
- **Build**: Maven 3, Vite 4, Cargo (Tomcat 10.1 embedded)
- **Test**: JUnit 5, Mockito 5
