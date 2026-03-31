package com.qlvanphongpham.adapter.persistence;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

/**
 * DatabaseConnection — quản lý kết nối MySQL.
 * Đọc cấu hình từ application.properties.
 * ⚠️  Hãy đặt đúng db.password trong application.properties trước khi chạy.
 */
public class DatabaseConnection {
    private final String url;
    private final String username;
    private final String password;

    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/qlvanphongpham?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver không tìm thấy: " + e.getMessage());
        }
    }

    public DatabaseConnection() {
        Properties prop = loadProperties();
        this.url      = prop.getProperty("db.url",      DEFAULT_URL);
        this.username = prop.getProperty("db.username", "root");
        this.password = prop.getProperty("db.password", "");

        if ("YOUR_PASSWORD".equals(this.password)) {
            System.err.println("⚠️  CẢNH BÁO: db.password trong application.properties vẫn là placeholder 'YOUR_PASSWORD'.");
            System.err.println("   Hãy thay đúng mật khẩu MySQL hoặc để trống nếu không có mật khẩu.");
        }
    }

    private Properties loadProperties() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                prop.load(input);
            } else {
                System.err.println("⚠️  Không tìm thấy application.properties, dùng giá trị mặc định.");
            }
        } catch (Exception e) {
            System.err.println("⚠️  Lỗi đọc application.properties: " + e.getMessage());
        }
        return prop;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /** Dùng để kiểm tra kết nối khi khởi động */
    public void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Kết nối database thành công! URL: " + url);
        } catch (SQLException e) {
            System.err.println("❌ Kết nối database thất bại: " + e.getMessage());
            System.err.println("   → Kiểm tra db.url, db.username, db.password trong application.properties");
        }
    }
}
