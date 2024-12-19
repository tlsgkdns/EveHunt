package com.evehunt.evehunt.global.infra.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@EnableCaching
@Configuration
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val caches = CacheType.entries.map {
            cache -> CaffeineCache(cache.cacheName,
            Caffeine.newBuilder()
                .expireAfterWrite(cache.expireTime, TimeUnit.SECONDS)
                .maximumSize(cache.maxSize)
                .build()
            )
        }
        val cacheManager = SimpleCacheManager()
        cacheManager.setCaches(caches)
        return cacheManager
    }
}