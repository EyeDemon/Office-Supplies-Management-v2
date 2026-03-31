package com.qlvanphongpham.usecase.response;

/**
 * LoginResponse — Use Case Response DTO.
 * KHÔNG chứa domain entity (User). Dùng UserResponse (DTO) để tránh
 * domain object bị lộ qua use case boundary (Clean Architecture).
 */
public class LoginResponse {
    private final boolean success;
    private final String message;
    private final UserResponse user;

    public LoginResponse(boolean success, String message, UserResponse user) {
        this.success = success;
        this.message = message;
        this.user    = user;
    }

    public boolean isSuccess()    { return success; }
    public String getMessage()    { return message; }
    public UserResponse getUser() { return user; }

    @Override
    public String toString() {
        return "LoginResponse{success=" + success + ", message='" + message + "', user=" +
               (user != null ? user.getUsername() : "null") + "}";
    }
}
