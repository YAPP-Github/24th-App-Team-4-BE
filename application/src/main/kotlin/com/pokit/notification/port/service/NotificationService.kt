package com.pokit.notification.port.service

import com.pokit.common.exception.NotFoundCustomException
import com.pokit.notification.exception.NotificationErrorCode
import com.pokit.notification.model.Notification
import com.pokit.notification.port.`in`.NotificationUseCase
import com.pokit.notification.port.out.NotificationPort
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationPort: NotificationPort
) : NotificationUseCase {

    override fun getNotifications(userId: Long, pageable: Pageable): Slice<Notification> {
        return notificationPort.loadAllByUserId(userId, pageable)
    }

    @Transactional
    override fun markAsRead(userId: Long, notificationId: Long) {
        val notification = notificationPort.loadByIdAndUserId(notificationId, userId)
            ?: throw NotFoundCustomException(NotificationErrorCode.NOT_FOUND_NOTIFICATION)
        notificationPort.persist(notification.markAsRead())
    }

    @Transactional
    override fun deleteNotification(userId: Long, notificationId: Long) {
        val notification = notificationPort.loadByIdAndUserId(notificationId, userId)
            ?: throw NotFoundCustomException(NotificationErrorCode.NOT_FOUND_NOTIFICATION)
        notificationPort.delete(notification)
    }

    override fun getUnreadCount(userId: Long): Long {
        return notificationPort.countUnreadByUserId(userId)
    }
}
