// CustomUserDetailsService.java
package com.iZoneHub.demo.security;

import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("找不到使用者：" + email));
        return new CustomUserDetails(user); // 你先前建立的 CustomUserDetails 類別
    }
}
