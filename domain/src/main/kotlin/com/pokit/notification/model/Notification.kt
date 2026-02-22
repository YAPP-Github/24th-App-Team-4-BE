package com.pokit.notification.model

import java.time.LocalDateTime

data class Notification(
    val id: Long = 0L,
    val userId: Long,
    val notificationType: NotificationType,
    val title: String,
    val body: String,
    val thumbnailUrl: String? = null,
    val isRead: Boolean = false,
    val navigationType: NavigationType = NavigationType.NONE,
    val deepLink: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun markAsRead(): Notification = copy(isRead = true)

    fun isNavigable(): Boolean = navigationType != NavigationType.NONE
}
