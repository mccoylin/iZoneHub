package com.iZoneHub.demo.security; // 您可以依專案結構調整 package 路徑

import com.iZoneHub.demo.model.User; // 導入您自己的 User Model
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user; // 包含您應用程式中的 User 物件

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // [重要] 新增這個方法，讓 Controller 可以取得使用者姓名
    public String getName() {
        return user.getName(); // 假設您的 User class 有 getName() 方法
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 這裡可以定義使用者的角色權限，為了簡化，我們先給予所有使用者 "ROLE_USER" 權限
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 從您的 User 物件取得密碼
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 我們使用 Email 作為 Spring Security 的 username
    }

    // 以下方法可根據您的需求決定是否實作更複雜的邏輯
    // 為了簡化，我們先全部回傳 true，表示帳號皆為可用狀態
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return user.getId();
    }

}