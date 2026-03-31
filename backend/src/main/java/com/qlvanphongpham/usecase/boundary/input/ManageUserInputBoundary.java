package com.qlvanphongpham.usecase.boundary.input;

import com.qlvanphongpham.usecase.request.*;
import com.qlvanphongpham.usecase.response.*;

/**
 * Input Boundary cho các use case quản lý người dùng.
 * Chỉ dùng DTO (Request/Response), không expose domain entities.
 */
public interface ManageUserInputBoundary {
    LoginResponse login(LoginRequest request);
    UserResponse  registerUser(RegisterUserRequest request);
    UserResponse  updateUser(UpdateUserRequest request);
    boolean       deleteUser(DeleteUserRequest request);
}
