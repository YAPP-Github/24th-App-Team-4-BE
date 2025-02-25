package com.pokit.config

import feign.Logger
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.pokit"])
class OpenFeignConfig {
    @Bean
    fun feignLoggerLevel() = Logger.Level.FULL
}
