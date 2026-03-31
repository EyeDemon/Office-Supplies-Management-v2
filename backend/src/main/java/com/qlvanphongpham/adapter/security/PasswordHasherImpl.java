package com.qlvanphongpham.adapter.security;

import com.qlvanphongpham.usecase.port.PasswordHasher;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasherImpl implements PasswordHasher {
    
    @Override
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        try {

            System.out.println("👉 Chuỗi nhập vào: [" + plainPassword + "]");
        System.out.println("👉 Chuỗi từ DB  : [" + hashedPassword + "]");
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.out.println("❌ Password check error: " + e.getMessage());
            return false;
        }
    }
    public static void main(String[] args) {
        // Tạo mã hash thật sự cho mật khẩu "123456"
        String realHash = org.mindrot.jbcrypt.BCrypt.hashpw("123456", org.mindrot.jbcrypt.BCrypt.gensalt());
        System.out.println("Mã Hash THẬT của 123456 là: " + realHash);
    }
}
