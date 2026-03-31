package com.qlvanphongpham.domain;

import com.qlvanphongpham.domain.valueobjects.Email;
import com.qlvanphongpham.domain.valueobjects.Password;
import com.qlvanphongpham.domain.valueobjects.PhoneNumber;
import java.time.LocalDateTime;

/**
 * User — Domain Entity.
 * Đây là trung tâm của business logic, không phụ thuộc bất kỳ framework nào.
 */
public class User {
    private Long id;
    private String username;
    private Email email;
    private Password password;
    private String fullName;
    private PhoneNumber phoneNumber;
    private Role role;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Role {
        ADMIN, USER
    }

    public User() {}

    public User(String username, Email email, Password password, String fullName, Role role) {
        this.username  = username;
        this.email     = email;
        this.password  = password;
        this.fullName  = fullName;
        this.role      = role;
        this.deleted   = false;
        this.createdAt = LocalDateTime.now();
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public String getUsername()          { return username; }
    public void setUsername(String u)    { this.username = u; }

    public Email getEmail()              { return email; }
    public void setEmail(Email e)        { this.email = e; }

    public Password getPassword()        { return password; }
    public void setPassword(Password p)  { this.password = p; }

    public String getFullName()          { return fullName; }
    public void setFullName(String fn)   { this.fullName = fn; }

    public PhoneNumber getPhoneNumber()           { return phoneNumber; }
    public void setPhoneNumber(PhoneNumber phone) { this.phoneNumber = phone; }

    public Role getRole()                { return role; }
    public void setRole(Role role)       { this.role = role; }

    public boolean isDeleted()           { return deleted; }
    public void setDeleted(boolean d)    { this.deleted = d; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime dt)      { this.createdAt = dt; }

    public LocalDateTime getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(LocalDateTime dt)      { this.updatedAt = dt; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role=" + role + "}";
    }
}
