package com.iZoneHub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iZoneHub.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * 檢查是否存在指定的 Email
     * @param email 要檢查的 Email
    */
    boolean existsByEmail(String email);


    /**
     * 根據 Email 查找 User
     * @param email
     * @return User
     */
    User findByEmail(String email);
}



