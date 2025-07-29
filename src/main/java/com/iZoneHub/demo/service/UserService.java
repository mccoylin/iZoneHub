package com.iZoneHub.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ 注入加密工具

    /**
     * 註冊新使用者
     */
    public void registerNewUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("註冊失敗：此 Email '" + email + "' 已經被註冊。");
        }

        // ✅ 密碼加密
        String encodedPassword = passwordEncoder.encode(password);

        // 建立 User 並儲存
        User newUser = new User(name, email, encodedPassword);
        userRepository.save(newUser);
    }

    /**
     * 認證使用者登入
     */
    public User authenticateUser(String email, String rawPassword) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("認證失敗：使用者不存在。"));

            // ✅ 使用 Spring Security 的密碼比對
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new IllegalStateException("認證失敗：Email 或密碼錯誤。");
            }

            return user;
        } catch (Exception e) {
            throw new IllegalStateException("認證失敗：Email 或密碼錯誤。");
        }
    }
}
