package com.qlvanphongpham.usecase.interactor;

import com.qlvanphongpham.adapter.presenter.UserPresenter;
import com.qlvanphongpham.usecase.port.UserRepository;
import com.qlvanphongpham.usecase.port.PasswordHasher;
import com.qlvanphongpham.usecase.request.LoginRequest;
import com.qlvanphongpham.usecase.request.RegisterUserRequest;
import com.qlvanphongpham.usecase.response.LoginResponse;
import com.qlvanphongpham.usecase.response.UserResponse;
import com.qlvanphongpham.domain.User;
import com.qlvanphongpham.domain.valueobjects.Email;
import com.qlvanphongpham.domain.valueobjects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageUserInteractorTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordHasher passwordHasher;

    private ManageUserInteractor interactor;
    private UserPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter  = new UserPresenter();
        interactor = new ManageUserInteractor(userRepository, passwordHasher, presenter);
    }

    // ── Login Tests ───────────────────────────────────────────────

    @Test
    @DisplayName("Login thành công với credentials hợp lệ")
    void login_success() {
        User mockUser = buildUser("admin", "Admin User");
        when(userRepository.findByUsername("admin")).thenReturn(mockUser);
        when(passwordHasher.checkPassword("123456", "$2a$hash")).thenReturn(true);

        LoginResponse res = interactor.login(new LoginRequest("admin", "123456"));

        assertTrue(res.isSuccess());
        assertEquals("Đăng nhập thành công", res.getMessage());
        assertNotNull(res.getUser());
        assertEquals("admin", res.getUser().getUsername());
        // Verify OutputBoundary was called
        assertNotNull(presenter.getLoginResponse());
    }

    @Test
    @DisplayName("Login thất bại — user không tồn tại")
    void login_userNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        LoginResponse res = interactor.login(new LoginRequest("ghost", "pass"));

        assertFalse(res.isSuccess());
        assertNull(res.getUser());
    }

    @Test
    @DisplayName("Login thất bại — sai mật khẩu")
    void login_wrongPassword() {
        User mockUser = buildUser("admin", "Admin User");
        when(userRepository.findByUsername("admin")).thenReturn(mockUser);
        when(passwordHasher.checkPassword("wrong", "$2a$hash")).thenReturn(false);

        LoginResponse res = interactor.login(new LoginRequest("admin", "wrong"));

        assertFalse(res.isSuccess());
        assertNull(res.getUser());
    }

    @Test
    @DisplayName("Login thất bại — username null")
    void login_nullUsername() {
        LoginResponse res = interactor.login(new LoginRequest(null, "pass"));
        assertFalse(res.isSuccess());
    }

    // ── Register Tests ────────────────────────────────────────────

    @Test
    @DisplayName("Đăng ký user mới thành công")
    void register_success() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordHasher.hashPassword("123456")).thenReturn("$2a$hash");

        User savedUser = buildUser("newuser", "New User");
        savedUser.setId(10L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterUserRequest req = new RegisterUserRequest("newuser", "new@test.com", "123456", "New User");
        UserResponse result = interactor.registerUser(req);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
    }

    @Test
    @DisplayName("Đăng ký thất bại — username đã tồn tại")
    void register_duplicateUsername() {
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        RegisterUserRequest req = new RegisterUserRequest("admin", "x@x.com", "pass", "Name");
        UserResponse result = interactor.registerUser(req);

        assertNull(result);
        assertEquals("Username đã tồn tại", presenter.getLastError());
    }

    // ── Helper ────────────────────────────────────────────────────

    private User buildUser(String username, String fullName) {
        User u = new User();
        u.setId(1L);
        u.setUsername(username);
        u.setEmail(new Email(username + "@test.com"));
        u.setPassword(new Password("$2a$hash"));
        u.setFullName(fullName);
        u.setRole(User.Role.ADMIN);
        return u;
    }
}
