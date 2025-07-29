package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.security.CustomUserDetails;
import com.iZoneHub.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        log.info("接收到 JSON 註冊請求，Email: {}", user.getEmail());

        try {
            userService.registerNewUser(user.getName(), user.getEmail(), user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "使用者 '" + user.getName() + "' 註冊成功!"));
        } catch (IllegalStateException e) {
            log.warn("註冊失敗，Email {} 已存在: {}", user.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("註冊過程中發生未預期錯誤，Email: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "伺服器內部錯誤，請稍後再試。"));
        }
    }



    @GetMapping("/auth/status")
    public ResponseEntity<?> getAuthStatus(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            Map<String, Object> userMap = Map.of(
                    "name", userDetails.getName(),
                    "email", userDetails.getUsername()
            );

            return ResponseEntity.ok(Map.of("loggedIn", true, "user", userMap));
        } else {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
    }
}
