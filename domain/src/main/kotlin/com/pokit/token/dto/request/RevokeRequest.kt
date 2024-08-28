package com.pokit.token.dto.request

import com.pokit.token.model.AuthPlatform

data class RevokeRequest(
    val authPlatform: AuthPlatform
)
