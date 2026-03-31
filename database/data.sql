-- ================================================================
-- 2. DATA: Dữ liệu mẫu – mật khẩu tất cả tài khoản là: 123456
-- ================================================================
USE qlvanphongpham;

-- Xóa dữ liệu cũ nếu có để tránh lỗi trùng lặp khi chạy lại file nhiều lần
TRUNCATE TABLE users;

-- Admins
INSERT INTO users (username, email, password, full_name, phone_number, role) VALUES
('admin',   'admin@qlvpp.vn',   '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Nguyễn Quản Trị',    '0912345678', 'ADMIN'),
('manager', 'manager@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Trần Văn Quản Lý',    '0934567890', 'ADMIN');

-- Nhân viên
INSERT INTO users (username, email, password, full_name, phone_number, role) VALUES
('nhanvien1', 'nv1@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Lê Thị Hoa',      '0945123456', 'USER'),
('nhanvien2', 'nv2@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Phạm Minh Tuấn', '0956234567', 'USER'),
('nhanvien3', 'nv3@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Hoàng Thị Mai',  '0967345678', 'USER'),
('nhanvien4', 'nv4@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Vũ Đức Mạnh',    '0978456789', 'USER'),
('nhanvien5', 'nv5@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Đặng Thị Lan',   '0989567890', 'USER'),
('nhanvien6', 'nv6@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Bùi Văn Hùng',   '0901678901', 'USER'),
('nhanvien7', 'nv7@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Ngô Thị Thu',    '0912789012', 'USER'),
('nhanvien8', 'nv8@qlvpp.vn', '$2a$10$QCWEwwg8Jnnud7KPjci71.Ss9QbSgX.FFJaXIU5RpSZ4EWYjv.dmq', 'Đinh Quang Khải','0923890123', 'USER');

SELECT id, username, full_name, phone_number, role FROM users WHERE deleted = FALSE ORDER BY role DESC, id;
SELECT CONCAT('Tổng tài khoản: ', COUNT(*)) AS summary FROM users WHERE deleted = FALSE;