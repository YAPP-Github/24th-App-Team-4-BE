package com.pokit.token.model

import com.pokit.common.exception.ClientValidationException
import com.pokit.token.exception.AuthErrorCode

enum class AuthPlatform(
    val platform: String,
) {
    GOOGLE("구글"),
    APPLE("애플"),
    ;

    companion object {
        fun of(input: String): AuthPlatform {
            return entries
                .firstOrNull { it.platform == input }
                ?: throw ClientValidationException(AuthErrorCode.INVALID_PLATFORM)
        }
    }
}
