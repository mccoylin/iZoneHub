package com.iZoneHub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.service.UserService;

@RestController
@RequestMapping("/api")     // 定義 API 的基礎路徑, 所有的 API 路徑都會以 /api 開頭
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 註冊新使用者，從 JSON 請求體中接收使用者資料。
     * @param user 包含使用者名稱、Email 和密碼的 User 物件。
     * @return 成功時回傳 201 Created 狀態碼和成功訊息；失敗時回傳相應的錯誤狀態碼和訊息。
     */
    @PostMapping("/register") // This can be the same endpoint as the form post
    public ResponseEntity<?> registerUserFromJson(@RequestBody User user)
    {
        log.info("接收到 JSON 註冊 email: {}", user.getEmail());

        try
        {
            // 將註冊邏輯委託給 UserService, 直接使用 User 物件
            userService.registerNewUser(user.getName(), user.getEmail(), user.getPassword());

            // 傳回狀態碼 201 (Created) 和訊息
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "使用者 '" + user.getName() + "' 註冊成功!"));
        }
        catch (IllegalStateException e)
        {
            // Handle known business logic errors (e.g., email already exists)
            log.warn("註冊新使用者失敗，email {} 已存在: {}", user.getEmail(), e.getMessage());
            // Return 409 Conflict status
            return ResponseEntity.status(HttpStatus.CONFLICT) .body(Map.of("message", e.getMessage()));
        }
        catch (Exception e)
        {
            // Handle unexpected server errors
            log.error("An unexpected error occurred during registration for email: {}", user.getEmail(), e);
            // Return 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An internal server error occurred."));
        }
    }


    /**
     * 處理使用者登入請求，從 JSON 請求體中接收資料並存入 Map。
     * @param loginData 一個包含 "email" 和 "password" 鍵的 Map。
     * @return 成功時回傳 200 OK 和成功訊息；失敗時回傳 401 Unauthorized。
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData)
    {
        log.info("接收到登入請求，Email: {}, Pwd: {}", loginData.get("email"), loginData.get("password"));

        // 從 Map 中取出 email 和 password
        String email = loginData.get("email");
        String password = loginData.get("password");

        // 檢查 email 和 password 是否為空
        if (email == null || password == null)
        {
            return ResponseEntity.badRequest().body(Map.of("message", "請求中缺少 'email' 或 'password'。"));
        }

        try
        {
            // 將認證邏輯委託給 UserService
            userService.authenticateUser(email, password);

            // 認證成功
            log.info("使用者 {} {} 登入成功！", email, password);
            return ResponseEntity.ok(Map.of("message", "使用者 " + email + " 登入成功！"));
        }
        catch (IllegalStateException e)
        {
            // 處理認證失敗 (例如，密碼錯誤或使用者不存在)
            log.warn("登入失敗，Email: {}，原因: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "登入失敗：Email 或密碼錯誤。"));
        }
        catch (Exception e)
        {
            // 處理其他未預期的伺服器錯誤
            log.error("登入過程中發生未預期錯誤，Email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "伺服器內部錯誤，請稍後再試。"));
        }
    }


    // @PostMapping("/register")
    // public ResponseEntity<?> registerUser(@RequestParam String name, @RequestParam String email, @RequestParam String password)
    // {
    //     log.info("收到新的註冊請求，Email: {}", email);

    //     try
    //     {
    //         // 將註冊邏輯委託給 UserService
    //         userService.registerNewUser(name, email, password);
    //         // 註冊成功，回傳 201 Created 狀態碼和成功訊息
    //         return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "使用者 " + name + " 註冊成功！"));
    //     }
    //     catch (IllegalStateException e)
    //     {
    //         // 處理業務邏輯錯誤，例如 Email 已存在
    //         log.warn("註冊失敗，Email {} 已被使用。", email);
    //         // 回傳 409 Conflict 狀態碼，表示請求與當前伺服器狀態衝突
    //         return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    //     }
    //     catch (Exception e)
    //     {
    //         // 處理其他未預期的伺服器錯誤
    //         log.error("註冊過程中發生未預期錯誤，Email: {}", email, e);
    //         // 回傳 500 Internal Server Error 狀態碼
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "伺服器內部錯誤，請稍後再試。"));
    //     }
    // }

}

