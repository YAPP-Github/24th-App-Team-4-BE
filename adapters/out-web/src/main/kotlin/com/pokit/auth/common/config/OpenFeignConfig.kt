package com.pokit.auth.common.config

import feign.Logger
import feign.RequestInterceptor
import feign.RequestTemplate
import feign.form.ContentProcessor.CONTENT_TYPE_HEADER
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType

@Configuration
@EnableFeignClients(basePackages = ["com.pokit"])
class OpenFeignConfig {
    @Bean
    fun feignLoggerLevel() = Logger.Level.FULL

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template: RequestTemplate ->
            template.header(
                CONTENT_TYPE_HEADER,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE
            )
        }
    }
}
