package com.pokit.notification.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "안읽은 알림 개수 응답")
data class UnreadCountResponse(
    @Schema(description = "안읽은 알림 개수")
    val count: Long
)
