package com.evehunt.evehunt.global.infra.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory

@Configuration
@Profile("dev")
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    lateinit var host: String
    @Value("\${spring.data.redis.port}")
    var port: Int = 0

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient{
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://$host:$port")
            .setDnsMonitoringInterval(-1)
        return Redisson.create(config)
    }

    @Bean
    fun redisConnectionFactory(redissonClient: RedissonClient): RedisConnectionFactory{
        return RedissonConnectionFactory(redissonClient)
    }
}