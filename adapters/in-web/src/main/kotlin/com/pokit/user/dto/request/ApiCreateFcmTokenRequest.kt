package com.pokit.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class ApiCreateFcmTokenRequest(
    @field:Schema(description = "사용자 기기 토큰(FCM 토큰)")
    @field:NotNull
    val token: String
)

internal fun ApiCreateFcmTokenRequest.toDto() = CreateFcmTokenRequest(
    token = this.token
)
