package com.pokit.auth.common.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "apple")
class AppleProperty(
    val clientId: String,
    val teamId: String,
    val keyId: String,
    val privateKey: String,
    val tokenUrl: String,
    val audience: String
) {
}
