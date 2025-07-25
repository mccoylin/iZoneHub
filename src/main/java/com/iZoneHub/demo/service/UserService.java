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
        User newUser = new User(name, email, password);

        // 3. 儲存到資料庫
        userRepository.save(newUser);
    }


    /**
     * 認證使用者登入
     *
     * @param email 使用者 Email
     * @param rawPassword 使用者密碼
     * @return 認證成功的 User 物件
     * @throws IllegalStateException 如果認證失敗或使用者不存在
     */
    public User authenticateUser(String email, String rawPassword)
    {
        try
        {
            // 1. 根據 email 尋找使用者, 如果不存在則拋出例外
            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("認證失敗：使用者不存在。"));

            // 2. 比對密碼
            if (! user.getPassword().equals(rawPassword))
            {
                throw new IllegalStateException("認證失敗：Email 或密碼錯誤。");
            }

            // 3. 認證成功，回傳使用者物件
            return user;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("認證失敗：Email 或密碼錯誤。");
        }
    }

}
