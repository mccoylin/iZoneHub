package com.iZoneHub.demo.controller;

// 這測試會涵蓋成功、失敗 (如 Email 衝突、認證失敗) 以及伺服器內部錯誤等多種情境。
// 測試同樣使用 MockMvc 來發送請求，並用 Mockito 來模擬 UserService 的行為，以確保我們只專注於測試 Controller 層的邏輯。
//
// 由於請求和回應都是 JSON 格式，測試中會使用 ObjectMapper 來序列化請求物件，並用 jsonPath 來驗證回應的 JSON 內容。


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.service.UserService;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.TestConfig.class)
class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Configuration
    static class TestConfig
    {
        @Bean
        public UserService userService()
        {
            return Mockito.mock(UserService.class);
        }
    }

    // --- 測試註冊 API ---

    @Test
    void whenRegisterUser_withValidData_thenReturnsCreated() throws Exception
    {
        // Arrange
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // 模擬 userService.registerNewUser 成功執行
        doNothing().when(userService).registerNewUser(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated()) // 驗證 HTTP 狀態碼為 201
            .andExpect(jsonPath("$.message").value("使用者 'Test User' 註冊成功!"));
    }

    @Test
    void whenRegisterUser_withExistingEmail_thenReturnsConflict() throws Exception
    {
        // Arrange
        User user = new User();
        user.setName("Test User");
        user.setEmail("existing@example.com");
        user.setPassword("password123");

        // 模擬 userService.registerNewUser 因 Email 已存在而拋出異常
        doThrow(new IllegalStateException("Email 已存在")).when(userService).registerNewUser(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isConflict()) // 驗證 HTTP 狀態碼為 409
            .andExpect(jsonPath("$.message").value("Email 已存在"));
    }

    // --- 測試登入 API ---

    @Test
    void whenLoginUser_withValidCredentials_thenReturnsOk() throws Exception
    {
        // Arrange
        Map<String, String> loginData = Map.of("email", "test@example.com", "password", "password123");
        User authenticatedUser = new User();
        authenticatedUser.setName("Test User");
        authenticatedUser.setEmail("test@example.com");

        // 模擬 userService.authenticateUser 成功並回傳使用者物件
        when(userService.authenticateUser("test@example.com", "password123")).thenReturn(authenticatedUser);

        // Act & Assert
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginData)))
            .andExpect(status().isOk()) // 驗證 HTTP 狀態碼為 200
            .andExpect(jsonPath("$.message").value("登入成功！"))
            .andExpect(jsonPath("$.userName").value("Test User"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void whenLoginUser_withInvalidCredentials_thenReturnsUnauthorized() throws Exception
    {
        // Arrange
        Map<String, String> loginData = Map.of("email", "test@example.com", "password", "wrongpassword");

        // 模擬 userService.authenticateUser 因憑證錯誤而拋出異常
        when(userService.authenticateUser(anyString(), anyString())).thenThrow(new IllegalStateException("認證失敗"));

        // Act & Assert
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginData)))
            .andExpect(status().isUnauthorized()) // 驗證 HTTP 狀態碼為 401
            .andExpect(jsonPath("$.message").value("登入失敗：Email 或密碼錯誤。"));
    }

    @Test
    void whenLoginUser_withMissingData_thenReturnsBadRequest() throws Exception
    {
        // Arrange
        Map<String, String> loginData = Map.of("email", "test@example.com"); // 缺少 password

        // Act & Assert
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginData)))
            .andExpect(status().isBadRequest()) // 驗證 HTTP 狀態碼為 400
            .andExpect(jsonPath("$.message").value("請求中缺少 'email' 或 'password'。"));
    }
}


/*

Mockito 是一個在 Java 中非常流行的「模擬 (Mocking)」框架，專門用於單元測試。

簡單來說，它讓您能夠建立一個物件的「替身」或「假物件」(Mock Object)。在進行單元測試時，您可以使用這個假物件來代替真實的依賴物件 (Dependency)。

### 為什麼要使用 Mockito？

在 `BookingControllerTest` 範例中：

1.  **測試目標**：我們想測試 `BookingController` 的邏輯是否正確。
2.  **依賴物件**：`BookingController` 依賴 `RoomService` 來從資料庫獲取資料。
3.  **問題**：在單元測試中，我們不希望真的去連接資料庫，因為那會讓測試變慢、不穩定，且不再是「單元」測試（而是整合測試）。

### Mockito 如何解決這個問題？

Mockito 讓我們可以建立一個假的 `RoomService`：

*   **`@MockBean`**: 這個 Spring Boot 的註解會建立一個 `RoomService` 的模擬物件，並將它注入到測試環境中。這個模擬物件是空的，沒有任何實際功能。
*   **`when(...).thenReturn(...)`**: 這是 Mockito 的核心語法。我們用它來**設定規則**，告訴這個假的 `RoomService` 該如何表現。

*   在測試中，`when(roomService.getRoomById(roomId)).thenReturn(mockRoom);` 的意思就是：「當這個假的 `roomService` 的 `getRoomById` 方法被呼叫，並且傳入的參數是 `roomId` 時，**不要真的去執行**，而是直接回傳我們預先準備好的 `mockRoom` 物件。」

總結來說，Mockito 讓您可以**隔離**被測試的類別 (`BookingController`)，並**控制**其依賴項 (`RoomService`) 的行為，從而確保您的單元測試只專注於驗證 `BookingController` 自身的邏輯是否正確，而不會受到外部依賴（如資料庫）的影響。

========================================

在 IntelliJ IDEA 中執行這個單元測試非常直觀。IDE 提供了多種簡單的方式來啟動測試。

您有以下幾種方式可以執行測試：

1.  **執行整個測試類別**：
    *   在 `public class UserControllerTest` 這一行的左側，您會看到一個綠色的「播放」按鈕。點擊它，然後選擇 "Run 'UserControllerTest'"。這將會執行這個檔案中的所有測試方法。

2.  **執行單一測試方法**：
    *   在每一個 `@Test` 註解的方法（例如 `whenRegisterUser_withValidData_thenReturnsCreated`）旁邊，同樣有一個綠色的「播放」按鈕。點擊它可以只執行那一個特定的測試案例。

3.  **從專案視窗執行**：
    *   在左側的專案檔案總管中，找到 `UserControllerTest.java` 檔案，在上面按右鍵，然後選擇 "Run 'UserControllerTest'"。

執行後，IntelliJ IDEA 會自動開啟底部的「Run」視窗，顯示測試進度和結果。綠色的勾表示測試通過，紅色的叉表示測試失敗。

 */
