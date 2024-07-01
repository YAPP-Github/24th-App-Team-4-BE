package com.pokit.auth.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperty(
    val clientSecret: String,
    val accessExpiryTime: Long,
    val refreshExpiryTime: Long,
)
