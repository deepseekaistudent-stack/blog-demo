package com.example.lianxi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 应用关闭时自动清理 Redis 缓存，避免残留脏数据。
 * 通过 @PreDestroy 钩子在 JVM 关闭前触发。
 */
@Component
public class CacheCleanupConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheCleanupConfig.class);

    private final CacheManager cacheManager;

    @Autowired
    public CacheCleanupConfig(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PreDestroy
    public void cleanup() {
        try {
            logger.info("Starting cache cleanup on application shutdown...");
            
            String[] cacheNames = {"user", "users", "blogArticle", "blogCategories"};
            for (String cacheName : cacheNames) {
                try {
                    cacheManager.getCache(cacheName).clear();
                    logger.info("Cleared cache: {}", cacheName);
                } catch (Exception e) {
                    logger.warn("Failed to clear cache: {}", cacheName, e);
                }
            }
            
            logger.info("Cache cleanup completed successfully");
        } catch (Exception e) {
            logger.error("Error during cache cleanup", e);
        }
    }
}
