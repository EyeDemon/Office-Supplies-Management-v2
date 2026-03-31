package com.qlvanphongpham.adapter.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import com.google.gson.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.qlvanphongpham.usecase.boundary.input.GetUserInputBoundary;
import com.qlvanphongpham.usecase.boundary.input.ManageUserInputBoundary;
import com.qlvanphongpham.usecase.interactor.GetUserInteractor;
import com.qlvanphongpham.usecase.interactor.ManageUserInteractor;
import com.qlvanphongpham.usecase.request.*;
import com.qlvanphongpham.usecase.response.*;
import com.qlvanphongpham.adapter.persistence.UserRepositoryImpl;
import com.qlvanphongpham.adapter.security.PasswordHasherImpl;

public class UserController extends HttpServlet {

    private ManageUserInputBoundary manageUserInput;
    private GetUserInputBoundary    getUserInput;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        UserRepositoryImpl repo = new UserRepositoryImpl();
        this.manageUserInput = new ManageUserInteractor(repo, new PasswordHasherImpl());
        this.getUserInput    = new GetUserInteractor(repo);
        this.gson            = AuthController.buildGson(); // reuse Gson with LocalDateTime adapter
    }

    // ── GET /api/users  or  GET /api/users/{id} ───────────────────

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            String path = request.getPathInfo();

            if (path == null || path.equals("/")) {
                UserListResponse list = getUserInput.getAllUsers();
                ok(response, "Lấy danh sách user thành công", list);
            } else {
                long userId = parseId(path);
                UserResponse user = getUserInput.getUserById(new GetUserRequest(userId));
                if (user != null) ok(response, "Lấy thông tin user thành công", user);
                else              err(response, "User không tồn tại", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            err(response, "ID user không hợp lệ", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            err(response, "Lỗi server: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // ── POST /api/users ───────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            RegisterUserRequest req = gson.fromJson(request.getReader(), RegisterUserRequest.class);
            if (req == null) { err(response, "Dữ liệu không hợp lệ", HttpServletResponse.SC_BAD_REQUEST); return; }

            UserResponse result = manageUserInput.registerUser(req);
            if (result != null) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write(gson.toJson(new ApiResponse(true, "Tạo user thành công", result)));
            } else {
                err(response, "Tạo user thất bại (username hoặc email đã tồn tại)", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            err(response, "Lỗi server: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // ── PUT /api/users/{id} ───────────────────────────────────────

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            long userId = parseId(request.getPathInfo());
            UpdateUserRequest req = gson.fromJson(request.getReader(), UpdateUserRequest.class);
            if (req == null) { err(response, "Dữ liệu không hợp lệ", HttpServletResponse.SC_BAD_REQUEST); return; }

            req.setId(userId);
            UserResponse result = manageUserInput.updateUser(req);
            if (result != null) ok(response, "Cập nhật user thành công", result);
            else                err(response, "Cập nhật user thất bại", HttpServletResponse.SC_BAD_REQUEST);

        } catch (NumberFormatException e) {
            err(response, "ID user không hợp lệ", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            err(response, "Lỗi server: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // ── DELETE /api/users/{id} ────────────────────────────────────

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            long userId = parseId(request.getPathInfo());
            boolean success = manageUserInput.deleteUser(new DeleteUserRequest(userId));
            if (success) ok(response, "Xóa user thành công", null);
            else         err(response, "Xóa user thất bại", HttpServletResponse.SC_BAD_REQUEST);

        } catch (NumberFormatException e) {
            err(response, "ID user không hợp lệ", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            err(response, "Lỗi server: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setStatus(HttpServletResponse.SC_OK);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private long parseId(String path) {
        if (path == null) throw new NumberFormatException("null path");
        String[] parts = path.split("/");
        if (parts.length < 2) throw new NumberFormatException("no id segment");
        return Long.parseLong(parts[1]);
    }

    private void ok(HttpServletResponse res, String msg, Object data) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(gson.toJson(new ApiResponse(true, msg, data)));
    }

    private void err(HttpServletResponse res, String msg, int status) throws IOException {
        res.setStatus(status);
        res.getWriter().write(gson.toJson(new ApiResponse(false, msg, null)));
    }
}
