package com.qlvanphongpham.adapter.presenter;

import com.qlvanphongpham.usecase.response.LoginResponse;
import com.qlvanphongpham.usecase.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class UserPresenterTest {

    private UserPresenter presenter;

    @BeforeEach
    void setUp() { presenter = new UserPresenter(); }

    @Test
    @DisplayName("presentLoginResult lưu response đúng")
    void presentLoginResult() {
        UserResponse user = new UserResponse();
        user.setUsername("admin");
        LoginResponse lr = new LoginResponse(true, "OK", user);

        presenter.presentLoginResult(lr);

        assertNotNull(presenter.getLoginResponse());
        assertTrue(presenter.getLoginResponse().isSuccess());
    }

    @Test
    @DisplayName("presentError lưu message đúng")
    void presentError() {
        presenter.presentError("Lỗi test");
        assertEquals("Lỗi test", presenter.getLastError());
    }

    @Test
    @DisplayName("presentSuccess lưu message đúng")
    void presentSuccess() {
        presenter.presentSuccess("Thành công");
        assertEquals("Thành công", presenter.getLastSuccess());
    }

    @Test
    @DisplayName("clear() reset toàn bộ state")
    void clear() {
        presenter.presentError("error");
        presenter.presentSuccess("ok");
        presenter.clear();

        assertNull(presenter.getLastError());
        assertNull(presenter.getLastSuccess());
        assertNull(presenter.getLoginResponse());
    }
}
