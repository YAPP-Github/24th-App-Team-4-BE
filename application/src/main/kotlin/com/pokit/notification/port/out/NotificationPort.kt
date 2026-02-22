package com.pokit.notification.port.out

import com.pokit.notification.model.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface NotificationPort {
    fun loadAllByUserId(userId: Long, pageable: Pageable): Slice<Notification>

    fun loadByIdAndUserId(id: Long, userId: Long): Notification?

    fun persist(notification: Notification): Notification

    fun delete(notification: Notification)

    fun countUnreadByUserId(userId: Long): Long
}
