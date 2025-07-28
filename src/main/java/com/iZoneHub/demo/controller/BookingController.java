package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class BookingController {

    private final RoomService roomService;

    // 使用建構子注入 (Constructor Injection) 是 Spring 推薦的最佳實踐
    public BookingController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * 處理並顯示特定房間的預約頁面。
     *
     * @param roomId 從 URL 路徑中獲取的房間 ID (例如 /booking/5 中的 5)
     * @param model  用於將房間資料傳遞給 Thymeleaf 模板
     * @param redirectAttributes 用於在發生錯誤並重導向時，傳遞提示訊息
     * @return 如果找到房間，則返回 "booking" 模板；如果找不到，則重導向回首頁
     */
    @GetMapping("/booking/{roomId}")
    public String showBookingPage(@PathVariable("roomId") Long roomId, Model model, RedirectAttributes redirectAttributes) {

        // 透過 RoomService 根據 ID 查找房間
        // 這裡我們使用 Optional 來優雅地處理房間可能不存在的情況
        Optional<Rooms> roomOptional = roomService.findRoomById(roomId);

        if (roomOptional.isPresent()) {
            // 如果成功找到房間
            Rooms room = roomOptional.get();
            model.addAttribute("room", room); // 將房間物件加入 model，供 booking.html 使用
            return "booking"; // 返回視圖名稱，Spring Boot 會去 templates/booking.html 找對應的模板
        } else {
            // 如果根據 ID 找不到房間
            // 使用 RedirectAttributes 加入一個 "快閃屬性"，這個屬性在重導向後依然存在，但只會顯示一次
            redirectAttributes.addFlashAttribute("errorMessage", "找不到指定的房間，請重新選擇。");
            return "redirect:/"; // 重導向回首頁
        }
    }
}
