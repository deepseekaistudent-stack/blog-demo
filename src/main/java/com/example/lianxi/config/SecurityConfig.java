package com.example.lianxi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全配置：向容器注入 BCryptPasswordEncoder 用于密码加密与校验。
 * 仅提供加密器 Bean，不启用 Spring Security 过滤器链。
 */
@Configuration
public class SecurityConfig {

    /**
     * 创建 BCrypt 密码编码器 Bean
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
