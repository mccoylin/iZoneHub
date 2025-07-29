package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.service.UserService;
import jakarta.servlet.http.HttpSession; // 引入 HttpSession
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api") // 定義 API 的基礎路徑, 所有的 API 路徑都會以 /api 開頭
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * [最佳實踐] 使用建構子注入 (Constructor Injection)
     * Spring 會自動傳入 UserService 的實例。
     * 這比 @Autowired 更安全、更清晰。
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 註冊新使用者，從 JSON 請求體中接收使用者資料。
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        log.info("接收到 JSON 註冊請求，Email: {}", user.getEmail());

        try {
            // 將註冊邏輯委託給 UserService
            userService.registerNewUser(user.getName(), user.getEmail(), user.getPassword());

            // 傳回狀態碼 201 (Created) 和訊息
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "使用者 '" + user.getName() + "' 註冊成功!"));
        } catch (IllegalStateException e) {
            // 處理業務邏輯錯誤 (例如，Email 已存在)
            log.warn("註冊失敗，Email {} 已存在: {}", user.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // 處理未預期的伺服器錯誤
            log.error("註冊過程中發生未預期錯誤，Email: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "伺服器內部錯誤，請稍後再試。"));
        }
    }


    /**
     * 處理使用者登入請求，並在成功後建立 Session。
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData, HttpSession session) { // 注入 HttpSession
        String email = loginData.get("email");
        String password = loginData.get("password");

        // [安全性] 修正日誌，絕不紀錄明文密碼
        log.info("接收到登入請求，Email: {}", email);

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "請求中缺少 'email' 或 'password'。"));
        }

        try {
            // 將認證邏輯委託給 UserService
            User authenticatedUser = userService.authenticateUser(email, password);

            // [核心功能] 登入成功，將使用者資訊存入 Session
            // 後續的請求可以從 Session 中獲取此物件，來判斷使用者是否已登入
            session.setAttribute("loggedInUser", authenticatedUser);
            log.info("使用者 {} 登入成功，Session 已建立。", email);

            // 為了安全，只回傳部分使用者資訊給前端，避免洩漏密碼等敏感資訊
            Map<String, Object> userInfo = Map.of(
                    "id", authenticatedUser.getId(),
                    "name", authenticatedUser.getName(),
                    "email", authenticatedUser.getEmail()
            );

            return ResponseEntity.ok(Map.of("message", "登入成功！", "user", userInfo));
        } catch (IllegalStateException e) {
            // 處理認證失敗 (例如，密碼錯誤或使用者不存在)
            log.warn("登入失敗，Email: {}，原因: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "登入失敗：Email 或密碼錯誤。"));
        } catch (Exception e) {
            // 處理其他未預期的伺服器錯誤
            log.error("登入過程中發生未預期錯誤，Email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "伺服器內部錯誤，請稍後再試。"));
        }
    }
}
