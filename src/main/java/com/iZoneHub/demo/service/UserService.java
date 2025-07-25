package com.iZoneHub.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.repository.UserRepository;

/**
 * 使用者服務層
 * 處理使用者相關的業務邏輯，包含註冊、登入等功能
 */
@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;


    /**
     * 註冊新使用者
     *
     * @param name 使用者名稱
     * @param email 使用者 Email
     * @param password 使用者密碼
     * @throws IllegalStateException 如果 Email 已被註冊
     */
    public void registerNewUser(String name, String email, String password)
    {
        // 1. 檢查 Email 是否已被註冊
        if (userRepository.findByEmail(email).isPresent())
        {
            // 如果 Email 已存在，拋出一個明確的例外
            throw new IllegalStateException("註冊失敗：此 Email '" + email + "' 已經被註冊。");
        }

        // 2. 建立新的 User 物件
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);

        // 3. 儲存到資料庫
        userRepository.save(newUser);
    }

}
