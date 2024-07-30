package com.pokit.auth.common.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "google")
class GoogleProperty(
    val clientId: String,
    val clientSecret: String
)
