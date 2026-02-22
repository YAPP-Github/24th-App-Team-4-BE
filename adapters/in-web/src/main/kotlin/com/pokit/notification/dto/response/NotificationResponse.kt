package com.pokit.notification.dto.response

import com.pokit.notification.model.NavigationType
import com.pokit.notification.model.Notification
import com.pokit.notification.model.NotificationType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "알림 응답")
data class NotificationResponse(
    @Schema(description = "알림 ID")
    val id: Long,

    @Schema(description = "알림 타입", example = "LINK_ADDED")
    val notificationType: NotificationType,

    @Schema(description = "알림 제목", example = "'뜨개질' 포킷에 링크가 추가되었어요")
    val title: String,

    @Schema(description = "알림 본문", example = "OO님이 추가한 링크를 지금 확인해보세요")
    val body: String,

    @Schema(description = "썸네일 URL (없을 수 있음)", nullable = true)
    val thumbnailUrl: String?,

    @Schema(description = "읽음 여부")
    val isRead: Boolean,

    @Schema(description = "네비게이션 타입", example = "CATEGORY_DETAIL")
    val navigationType: NavigationType,

    @Schema(description = "딥링크 (NONE이면 null)", nullable = true, example = "pokit://shared?categoryId=42&contentId=123")
    val deepLink: String?,

    @Schema(description = "알림 생성 시점")
    val createdAt: LocalDateTime
)

internal fun Notification.toResponse() = NotificationResponse(
    id = this.id,
    notificationType = this.notificationType,
    title = this.title,
    body = this.body,
    thumbnailUrl = this.thumbnailUrl,
    isRead = this.isRead,
    navigationType = this.navigationType,
    deepLink = this.deepLink,
    createdAt = this.createdAt
)
