package com.pokit.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "spring.data.redis")
@Configuration
class RedisProperties {
    lateinit var host: String
    lateinit var port: String
}
