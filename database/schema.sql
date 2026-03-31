-- ================================================================
-- 1. SCHEMA: Tạo Database và Bảng Quản Lý Văn Phòng Phẩm
-- ================================================================
CREATE DATABASE IF NOT EXISTS qlvanphongpham
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE qlvanphongpham;

CREATE TABLE IF NOT EXISTS users (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(50)  UNIQUE NOT NULL COMMENT 'Tên đăng nhập',
    email        VARCHAR(100) UNIQUE NOT NULL COMMENT 'Email',
    password     VARCHAR(255)        NOT NULL COMMENT 'BCrypt hash',
    full_name    VARCHAR(100)        NOT NULL COMMENT 'Họ và tên',
    phone_number VARCHAR(15)                  COMMENT 'Số điện thoại VN',
    role         ENUM('ADMIN','USER') DEFAULT 'USER',
    deleted      BOOLEAN             DEFAULT FALSE,
    created_at   TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_username   (username),
    INDEX idx_email      (email),
    INDEX idx_role       (role),
    INDEX idx_created_at (created_at),
    INDEX idx_deleted    (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT 'Schema created successfully.' AS status;