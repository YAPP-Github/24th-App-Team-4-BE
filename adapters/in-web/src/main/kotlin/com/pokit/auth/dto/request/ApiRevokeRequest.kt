package com.pokit.auth.dto.request

import com.pokit.token.dto.request.RevokeRequest
import com.pokit.token.model.AuthPlatform

data class ApiRevokeRequest(
    val authPlatform: String
)

internal fun ApiRevokeRequest.toDto() = RevokeRequest(
    authPlatform = AuthPlatform.of(this.authPlatform)
)
