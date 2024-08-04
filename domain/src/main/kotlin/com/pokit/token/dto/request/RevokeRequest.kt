package com.pokit.token.dto.request

import com.pokit.token.model.AuthPlatform

data class RevokeRequest(
    val refreshToken: String,
    val authPlatform: AuthPlatform
)
