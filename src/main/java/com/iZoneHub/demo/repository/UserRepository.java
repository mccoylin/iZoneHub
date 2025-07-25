package com.iZoneHub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // 引入 Optional

import com.iZoneHub.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
    // Spring Data JPA 的查詢方法在設計上不直接回傳實體物件，而是回傳一個 Optional<T> 物件。
    // 為什麼是 Optional 而不是 User？
    // Optional 是 Java 8 引入的一個容器類別，它的目的是為了解決程式碼中無處不在的 NullPointerException（空指標例外）。
    // 它的設計理念是：
    //     •一個方法的回傳值可能存在，也可能不存在（例如，在資料庫中找不到對應 Email 的使用者）。
    //     •Optional 強迫開發者必須明確地處理「找不到」的情況，而不是忘記檢查 null 而導致程式在執行時崩潰。

    // Spring Data JPA 會自動根據方法名稱產生查詢
    // 這會產生一個 "SELECT * FROM user WHERE email = ?" 的查詢
    Optional<User> findByEmail(String email);

}
