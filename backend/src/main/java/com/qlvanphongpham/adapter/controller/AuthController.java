package com.qlvanphongpham.adapter.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.qlvanphongpham.usecase.boundary.input.ManageUserInputBoundary;
import com.qlvanphongpham.usecase.interactor.ManageUserInteractor;
import com.qlvanphongpham.usecase.request.LoginRequest;
import com.qlvanphongpham.usecase.response.LoginResponse;
import com.qlvanphongpham.usecase.response.UserResponse;
import com.qlvanphongpham.adapter.persistence.UserRepositoryImpl;
import com.qlvanphongpham.adapter.security.PasswordHasherImpl;

public class AuthController extends HttpServlet {

    private ManageUserInputBoundary userInputBoundary;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userInputBoundary = new ManageUserInteractor(
            new UserRepositoryImpl(),
            new PasswordHasherImpl()
        );
        this.gson = buildGson();
    }

    /** Gson với LocalDateTime adapter — tránh trả về {} cho date fields */
    static Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                (src, typeOfSrc, ctx) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                (json, typeOfT, ctx) ->
                    LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String path = request.getPathInfo();
        System.out.println("🔍 AuthController path: " + path);

        if (path == null) {
            sendError(response, "Endpoint không hợp lệ", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            switch (path) {
                case "/login":  handleLogin(request, response);  break;
                case "/logout": handleLogout(request, response); break;
                default:        sendError(response, "Endpoint không tồn tại", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Lỗi server: " + e.getMessage(),
                      HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        LoginRequest loginRequest = gson.fromJson(request.getReader(), LoginRequest.class);

        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            sendError(response, "Username và password là bắt buộc", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        System.out.println("🔐 Login attempt: " + loginRequest.getUsername());
        LoginResponse loginResponse = userInputBoundary.login(loginRequest);

        if (loginResponse.isSuccess()) {
            // loginResponse.getUser() bây giờ là UserResponse (DTO), không phải domain User
            UserResponse userDTO = loginResponse.getUser();

            HttpSession session = request.getSession(true);
            session.setAttribute("userId",   userDTO.getId());
            session.setAttribute("username", userDTO.getUsername());
            session.setAttribute("role",     userDTO.getRole());
            session.setMaxInactiveInterval(30 * 60);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(new AuthResponse(
                true,
                loginResponse.getMessage(),
                userDTO,
                "session-" + session.getId()
            )));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(gson.toJson(
                new AuthResponse(false, loginResponse.getMessage(), null, null)));
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(
            new AuthResponse(true, "Đăng xuất thành công", null, null)));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendError(HttpServletResponse response, String message, int status)
            throws IOException {
        response.setStatus(status);
        response.getWriter().write(gson.toJson(new AuthResponse(false, message, null, null)));
    }

    // ── Inner DTO ─────────────────────────────────────────────────
    public static class AuthResponse {
        private final boolean success;
        private final String  message;
        private final Object  user;
        private final String  token;

        public AuthResponse(boolean success, String message, Object user, String token) {
            this.success = success;
            this.message = message;
            this.user    = user;
            this.token   = token;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getUser()    { return user; }
        public String getToken()   { return token; }
    }
}
