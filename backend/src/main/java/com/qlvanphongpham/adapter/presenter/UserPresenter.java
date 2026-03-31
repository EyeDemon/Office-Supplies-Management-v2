package com.qlvanphongpham.adapter.presenter;

import com.qlvanphongpham.usecase.boundary.output.ManageUserOutputBoundary;
import com.qlvanphongpham.usecase.response.LoginResponse;
import com.qlvanphongpham.usecase.response.UserResponse;
import com.qlvanphongpham.usecase.response.UserListResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * UserPresenter — Adapter Layer (Clean Architecture).
 *
 * Đây là class cụ thể triển khai OutputBoundary.
 * Đặt trong adapter layer vì nó chuyển đổi use-case response
 * sang định dạng phù hợp cho UI (Swing, CLI, test, …).
 *
 * KHÔNG được đặt trong usecase/boundary/output (đó chỉ dành cho interfaces).
 */
public class UserPresenter implements ManageUserOutputBoundary {

    private UserResponse  lastUser;
    private LoginResponse loginResponse;
    private List<UserResponse> userList = new ArrayList<>();
    private String lastError;
    private String lastSuccess;

    @Override
    public void presentLoginResult(LoginResponse response) {
        this.loginResponse = response;
    }

    @Override
    public void presentUserResult(UserResponse response) {
        this.lastUser = response;
    }

    @Override
    public void presentUser(UserResponse user) {
        this.lastUser = user;
    }

    @Override
    public void presentSuccess(String message) {
        this.lastSuccess = message;
    }

    @Override
    public void presentError(String message) {
        this.lastError = message;
    }

    public void presentUserList(List<UserResponse> users) {
        this.userList = new ArrayList<>(users);
    }

    // ── Getters cho test / controller ─────────────────────────────
    public UserResponse      getLastUser()      { return lastUser; }
    public LoginResponse     getLoginResponse() { return loginResponse; }
    public List<UserResponse> getUserList()     { return new ArrayList<>(userList); }
    public String            getLastError()     { return lastError; }
    public String            getLastSuccess()   { return lastSuccess; }

    public void clear() {
        lastUser      = null;
        loginResponse = null;
        userList.clear();
        lastError     = null;
        lastSuccess   = null;
    }
}
