package com.pokit.auth.dto.request

import com.pokit.token.dto.request.RevokeRequest
import com.pokit.token.model.AuthPlatform
import io.swagger.v3.oas.annotations.media.Schema

data class ApiRevokeRequest(
    @Schema(description = "플랫폼에서 받은 Refresh Token")
    val refreshToken: String,
    val authPlatform: String
)

internal fun ApiRevokeRequest.toDto() = RevokeRequest(
    refreshToken = this.refreshToken,
    authPlatform = AuthPlatform.of(this.authPlatform)
)
