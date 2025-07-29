package com.iZoneHub.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iZoneHub.demo.security.CustomUserDetails;
// import com.iZoneHub.demo.security.CustomUserDetailsService; // 不再需要，可以移除
import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Autowired; // 不再需要，可以移除
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // [重要] 移除這個未使用的舊版 import
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // [修正] 移除這個 @Autowired 欄位。
    // Spring Security 會自動尋找並使用您定義的 CustomUserDetailsService Bean，
    // 在此處注入是不必要的，且有時會導致初始化順序問題。
    // @Autowired
    // private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 您的 filterChain 設定看起來是正確且現代化的，無需更動
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/assets/**",
                                "/api/register",
                                "/api/login",
                                "/api/auth/status"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                            Map<String, Object> data = new HashMap<>();
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("name", userDetails.getName());
                            userMap.put("email", userDetails.getUsername());
                            data.put("user", userMap);

                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            Map<String, String> data = new HashMap<>();
                            data.put("message", "登入失敗，請檢查您的帳號或密碼。");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            Map<String, String> data = new HashMap<>();
                            data.put("message", "登出成功");
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
                        })
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            Map<String, String> data = new HashMap<>();
                            data.put("message", "需要認證才能訪問此資源");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}