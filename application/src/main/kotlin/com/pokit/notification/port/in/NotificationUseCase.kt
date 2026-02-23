package com.pokit.notification.port.`in`

import com.pokit.notification.model.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface NotificationUseCase {
    fun getNotifications(userId: Long, pageable: Pageable): Slice<Notification>

    fun markAsRead(userId: Long, notificationId: Long)

    fun deleteNotification(userId: Long, notificationId: Long)

    fun getUnreadCount(userId: Long): Long

    fun createAndSend(notification: Notification): Notification
}
