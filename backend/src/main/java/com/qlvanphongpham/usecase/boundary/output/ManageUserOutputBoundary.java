package com.qlvanphongpham.usecase.boundary.output;

import com.qlvanphongpham.usecase.response.LoginResponse;
import com.qlvanphongpham.usecase.response.UserResponse;

/**
 * Output Boundary — interface thuần túy trong use case layer.
 * Chỉ phụ thuộc vào DTOs, không phụ thuộc framework hay UI.
 */
public interface ManageUserOutputBoundary {
    void presentLoginResult(LoginResponse response);
    void presentUserResult(UserResponse response);
    void presentUser(UserResponse user);
    void presentSuccess(String message);
    void presentError(String message);
}
