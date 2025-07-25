package com.iZoneHub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // 引入 Optional

import com.iZoneHub.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
    // Spring Data JPA 會自動根據方法名稱產生查詢
    // 這會產生一個 "SELECT * FROM user WHERE email = ?" 的查詢
    Optional<User> findByEmail(String email);


    // /**
    //  * 檢查是否存在指定的 Email
    //  * @param email 要檢查的 Email
    //  */
    // boolean existsByEmail(String email);


    // /**
    //  * 根據 Email 查找 User
    //  * @param email
    //  * @return User
    //  */
    // User findByEmail(String email);

}
