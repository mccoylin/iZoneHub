package com.iZoneHub.demo.model;

// User 類包含了使用者的基本資訊。
// 建出 Mysql DataTable User

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;


@Data                   // Data：自動生成所有字段的 getter、setter 方法，以及 toString()、equals() 和 hashCode() 方法。
// @Builder                // Builder：為 class 生成建造者模式（Builder Pattern）的支持，允許通過流式 API 創建對象。
@AllArgsConstructor     // AllArgsConstructor：生成包含所有字段的參數化構造函數。
@NoArgsConstructor      // @NoArgsConstructor：生成無參構造函數。
@Entity                 // JPA 的 Entity 註解，用於標識這是一個 JPA 實體類，將映射到資料庫中的一個表。
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // JPA 的 GeneratedValue 註解，用於指定主鍵的生成策略。
    private Long id;                // primary key, auto-generated
    // 依照前端的程式碼，建出欄位。如果 column name 需要另行指定，需要使用 @Column 註解。
    // @Column("user_name")
    private String name;            // user's name
    private String email;           // user's email
    private String password;        // user's password

    private LocalDate createDate;   // create date
    private LocalDate modifyDate;   // modify date

    public User(String name, String email, String password)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createDate = LocalDate.now(); // 設定建立日期為當前日期
        this.modifyDate = LocalDate.now(); // 設定修改日期為當前日期
    }

}
