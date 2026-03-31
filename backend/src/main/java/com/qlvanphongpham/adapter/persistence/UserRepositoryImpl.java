package com.qlvanphongpham.adapter.persistence;

import com.qlvanphongpham.usecase.port.UserRepository;
import com.qlvanphongpham.domain.User;
import com.qlvanphongpham.domain.valueobjects.Email;
import com.qlvanphongpham.domain.valueobjects.Password;
import com.qlvanphongpham.domain.valueobjects.PhoneNumber;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserRepositoryImpl — Adapter (Persistence).
 * Implements the UserRepository port defined in the use-case layer.
 * Sử dụng JDBC thuần; mỗi method lấy 1 connection từ pool (get + close).
 */
public class UserRepositoryImpl implements UserRepository {

    private final DatabaseConnection db;

    public UserRepositoryImpl() {
        this.db = new DatabaseConnection();
        db.testConnection();
    }

    // ── Queries ───────────────────────────────────────────────────

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND deleted = false";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ findByUsername error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ? AND deleted = false";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ findById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE deleted = false ORDER BY created_at DESC";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(map(rs));
        } catch (SQLException e) {
            System.err.println("❌ findAll error: " + e.getMessage());
        }
        return users;
    }

    // ── Mutations ─────────────────────────────────────────────────

    @Override
    public User save(User user) {
        return user.getId() == null ? insert(user) : update(user);
    }

    private User insert(User user) {
        String sql = "INSERT INTO users (username, email, password, full_name, phone_number, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail().getValue());
            ps.setString(3, user.getPassword().getValue());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhoneNumber() != null ? user.getPhoneNumber().getValue() : null);
            ps.setString(6, user.getRole().name());

            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getLong(1));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ insert error: " + e.getMessage());
        }
        return null;
    }

    private User update(User user) {
        String sql = "UPDATE users SET email=?, full_name=?, phone_number=?, role=?, " +
                     "password=?, updated_at=NOW() WHERE id=? AND deleted=false";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getEmail().getValue());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPhoneNumber() != null ? user.getPhoneNumber().getValue() : null);
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getPassword().getValue());
            ps.setLong(6, user.getId());

            return ps.executeUpdate() > 0 ? user : null;
        } catch (SQLException e) {
            System.err.println("❌ update error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE users SET deleted=true, updated_at=NOW() WHERE id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ delete error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean existsByUsername(String username) {
        return count("SELECT COUNT(*) FROM users WHERE username=? AND deleted=false", username) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        return count("SELECT COUNT(*) FROM users WHERE email=? AND deleted=false", email) > 0;
    }

    // ── Helpers ───────────────────────────────────────────────────

    private int count(String sql, String param) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ count error: " + e.getMessage());
        }
        return 0;
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(new Email(rs.getString("email")));
        u.setPassword(new Password(rs.getString("password")));
        u.setFullName(rs.getString("full_name"));

        String phone = rs.getString("phone_number");
        if (phone != null && !phone.isBlank()) u.setPhoneNumber(new PhoneNumber(phone));

        u.setRole(User.Role.valueOf(rs.getString("role")));
        u.setDeleted(rs.getBoolean("deleted"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) u.setCreatedAt(created.toLocalDateTime());

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) u.setUpdatedAt(updated.toLocalDateTime());

        return u;
    }
}
