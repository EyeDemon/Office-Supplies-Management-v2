package com.qlvanphongpham.usecase.interactor;

import com.qlvanphongpham.usecase.boundary.input.ManageUserInputBoundary;
import com.qlvanphongpham.usecase.boundary.output.ManageUserOutputBoundary;
import com.qlvanphongpham.usecase.port.UserRepository;
import com.qlvanphongpham.usecase.port.PasswordHasher;
import com.qlvanphongpham.usecase.request.*;
import com.qlvanphongpham.usecase.response.*;
import com.qlvanphongpham.domain.User;
import com.qlvanphongpham.domain.valueobjects.Email;
import com.qlvanphongpham.domain.valueobjects.Password;
import com.qlvanphongpham.domain.valueobjects.PhoneNumber;

/**
 * ManageUserInteractor — Use Case Interactor (Clean Architecture).
 *
 * Chỉ có 1 paradigm nhất quán: direct return (không trộn OutputBoundary).
 * OutputBoundary chỉ dùng khi có presenter cụ thể (Swing, CLI, …).
 */
public class ManageUserInteractor implements ManageUserInputBoundary {

    private final UserRepository           userRepository;
    private final PasswordHasher           passwordHasher;
    private final ManageUserOutputBoundary outputBoundary; // nullable

    public ManageUserInteractor(UserRepository userRepository,
                                PasswordHasher passwordHasher) {
        this(userRepository, passwordHasher, null);
    }

    public ManageUserInteractor(UserRepository userRepository,
                                PasswordHasher passwordHasher,
                                ManageUserOutputBoundary outputBoundary) {
        this.userRepository = userRepository;
        this.passwordHasher  = passwordHasher;
        this.outputBoundary  = outputBoundary;
    }

    // ─────────────────────────────────────────────────────────────
    // InputBoundary methods — trả về DTO, không lộ domain entity
    // ─────────────────────────────────────────────────────────────

    @Override
    public LoginResponse login(LoginRequest request) {
        System.out.println("🔐 Processing login for: " + request.getUsername());

        if (request.getUsername() == null || request.getPassword() == null ||
            request.getUsername().isBlank() || request.getPassword().isBlank()) {
            return new LoginResponse(false, "Username và password không được để trống", null);
        }

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            System.out.println("❌ User not found: " + request.getUsername());
            return new LoginResponse(false, "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }

        boolean valid = passwordHasher.checkPassword(
                request.getPassword(), user.getPassword().getValue());

        if (!valid) {
            System.out.println("❌ Invalid password for: " + request.getUsername());
            return new LoginResponse(false, "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }

        System.out.println("✅ Login successful: " + request.getUsername());
        UserResponse userResponse = UserResponse.fromUser(user);

        LoginResponse response = new LoginResponse(true, "Đăng nhập thành công", userResponse);
        if (outputBoundary != null) outputBoundary.presentLoginResult(response);
        return response;
    }

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {
        if (request.getUsername() == null || request.getEmail() == null ||
            request.getPassword() == null || request.getFullName() == null) {
            if (outputBoundary != null) outputBoundary.presentError("Thiếu thông tin bắt buộc");
            return null;
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            if (outputBoundary != null) outputBoundary.presentError("Username đã tồn tại");
            return null;
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            if (outputBoundary != null) outputBoundary.presentError("Email đã tồn tại");
            return null;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(new Email(request.getEmail()));
        user.setPassword(new Password(passwordHasher.hashPassword(request.getPassword())));
        user.setFullName(request.getFullName());

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(new PhoneNumber(request.getPhoneNumber()));
        }

        // Admin có thể chỉ định role; mặc định USER
        User.Role role = User.Role.USER;
        if (request.getRole() != null) {
            try { role = User.Role.valueOf(request.getRole().toUpperCase()); }
            catch (IllegalArgumentException ignored) {}
        }
        user.setRole(role);

        User saved = userRepository.save(user);
        if (saved == null) return null;

        UserResponse response = UserResponse.fromUser(saved);
        if (outputBoundary != null) outputBoundary.presentUserResult(response);
        return response;
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest request) {
        if (request.getId() == null) return null;

        User existing = userRepository.findById(request.getId());
        if (existing == null) return null;

        if (request.getEmail() != null)    existing.setEmail(new Email(request.getEmail()));
        if (request.getFullName() != null) existing.setFullName(request.getFullName());

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            existing.setPhoneNumber(new PhoneNumber(request.getPhoneNumber()));
        } else if (request.getPhoneNumber() != null) {
            existing.setPhoneNumber(null);
        }

        if (request.getRole() != null && !request.getRole().isBlank()) {
            try { existing.setRole(User.Role.valueOf(request.getRole().toUpperCase())); }
            catch (IllegalArgumentException e) {
                System.err.println("⚠️  Role không hợp lệ: " + request.getRole());
            }
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(new Password(passwordHasher.hashPassword(request.getPassword())));
        }

        User updated = userRepository.save(existing);
        if (updated == null) return null;

        UserResponse response = UserResponse.fromUser(updated);
        if (outputBoundary != null) outputBoundary.presentUserResult(response);
        return response;
    }

    @Override
    public boolean deleteUser(DeleteUserRequest request) {
        if (request.getId() == null) return false;
        return userRepository.delete(request.getId());
    }

    // ─────────────────────────────────────────────────────────────
    // Additional operations dùng OutputBoundary (cho CLI/Swing/test)
    // ─────────────────────────────────────────────────────────────

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId);
        if (user == null) {
            if (outputBoundary != null) outputBoundary.presentError("User không tìm thấy");
            return;
        }
        if (!passwordHasher.checkPassword(currentPassword, user.getPassword().getValue())) {
            if (outputBoundary != null) outputBoundary.presentError("Mật khẩu hiện tại không đúng");
            return;
        }
        user.setPassword(new Password(passwordHasher.hashPassword(newPassword)));
        userRepository.save(user);
        if (outputBoundary != null) outputBoundary.presentSuccess("Đổi mật khẩu thành công");
    }
}
