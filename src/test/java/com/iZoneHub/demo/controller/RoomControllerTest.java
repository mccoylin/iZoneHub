package com.iZoneHub.demo.controller;

// 這個測試將會驗證 home 方法是否：
//
// 1. 成功呼叫 roomRepository.findAll()。
// 2. 將從 Repository 獲取的房間列表正確地加入到 Model 中。
// 3. 回傳正確的視圖名稱 index。

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.repository.RoomRepository;

@WebMvcTest(RoomController.class)
@Import(RoomControllerTest.TestConfig.class)
class RoomControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Configuration
    static class TestConfig
    {
        @Bean
        public RoomRepository roomRepository()
        {
            return Mockito.mock(RoomRepository.class);
        }
    }


    @Test
    void whenHome_thenReturnsIndexViewWithRooms() throws Exception
    {
        // Arrange: 準備測試資料和模擬行為
        Rooms room1 = new Rooms();
        room1.setId(1L);
        room1.setName("Single Room");

        Rooms room2 = new Rooms();
        room2.setId(2L);
        room2.setName("Double Room");

        List<Rooms> mockRooms = Arrays.asList(room1, room2);

        // 當 roomRepository.findAll() 被呼叫時，回傳我們準備好的 mockRooms 列表
        when(roomRepository.findAll()).thenReturn(mockRooms);

        // Act & Assert: 執行請求並驗證結果
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) // 驗證 HTTP 狀態碼為 200
                .andExpect(view().name("index")) // 驗證返回的視圖名稱為 "index"
                .andExpect(model().attributeExists("rooms")) // 驗證模型中存在名為 "rooms" 的屬性
                .andExpect(model().attribute("rooms", mockRooms)); // 驗證 "rooms" 屬性的值與我們預期的一致
    }

}
