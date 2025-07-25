package com.iZoneHub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.repository.RoomRepository;

@Controller
public class RoomController
{
    @Autowired
    private RoomRepository roomRepository;

    // 簡單來說，@GetMapping("/") 可以 引用到 index.html，但它和你想像的「直接打開檔案」的方式完全不同。
    // 問題的關鍵在於 Spring Boot 中有兩個處理網頁內容的世界：
    // 1.動態模板的世界 (Dynamic Templates)
    // 2.靜態資源的世界 (Static Resources)
    //
    // RoomController 活在第一個世界裡。
    //
    // 1. 動態模板的世界 (你的 RoomController 所在之處)
    //   當你使用 @Controller (而不是 @RestController) 時，你告訴 Spring Boot：「這個類別的方法回傳的不是資料，而是一個視圖 (View) 的名字。
    // 請用一個叫做『視圖解析器 (ViewResolver)』的東西去找到對應的模板檔案，把資料填進去，然後生成最終的 HTML 給使用者。」
    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("rooms", roomRepository.findAll());
        return "index";
    }
    // 上述的程式碼可以這樣理解：
    // 1.當使用者訪問網站根目錄 (/) 時，執行 home 方法。
    // 2.從資料庫裡拿出所有的房間資料 (roomRepository.findAll())。
    // 3.把這些資料放進一個叫做 model 的袋子裡，並給它貼上一個標籤 "rooms"。
    // 4.最後，回傳一個視圖的名字 "index"。
    // Spring Boot 接下來會拿著這個名字 "index"，去預設的模板資料夾裡尋找對應的檔案。
    // 這個資料夾通常是：
    //     •src/main/resources/templates/
    // 如果你的專案使用了像 Thymeleaf 這樣的模板引擎，Spring Boot 就會去找 src/main/resources/templates/index.html，並用你放進 model 的 rooms 資料來渲染這個頁面。
    //
    // 2. 靜態資源的世界 (Spring Boot 的自動配置)
    //     Spring Boot 有一個非常方便的自動配置功能： 它會自動將某個資料夾下的所有檔案當作靜態資源來提供服務。
    //     這個資料夾通常是：
    //         •src/main/resources/static/
    //     如果你把一個 index.html 檔案直接放在 static 資料夾下，那麼你根本不需要任何 Controller。
    //     當使用者訪問網站根目錄 (/) 時，Spring Boot 會自動找到並回傳 static/index.html。

}

