package com.example.lianxi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Spring Boot 启动类。
 * 扫描 com.example.lianxi.mapper 包下的 MyBatis Mapper。
 */
@SpringBootApplication
@MapperScan("com.example.lianxi.mapper")
@EnableCaching
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
