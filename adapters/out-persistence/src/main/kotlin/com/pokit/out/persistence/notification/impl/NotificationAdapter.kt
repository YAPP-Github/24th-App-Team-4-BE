package com.pokit.out.persistence.notification.impl

import com.pokit.notification.model.Notification
import com.pokit.notification.port.out.NotificationPort
import com.pokit.out.persistence.notification.persist.NotificationEntity
import com.pokit.out.persistence.notification.persist.NotificationRepository
import com.pokit.out.persistence.notification.persist.toDomain
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository

@Repository
class NotificationAdapter(
    private val notificationRepository: NotificationRepository
) : NotificationPort {

    override fun loadAllByUserId(userId: Long, pageable: Pageable): Slice<Notification> {
        return notificationRepository.findAllByUserIdAndDeleted(userId, false, pageable)
            .map { it.toDomain() }
    }

    override fun loadByIdAndUserId(id: Long, userId: Long): Notification? {
        return notificationRepository.findByIdAndUserIdAndDeleted(id, userId, false)
            ?.toDomain()
    }

    override fun persist(notification: Notification): Notification {
        return notificationRepository.save(NotificationEntity.of(notification)).toDomain()
    }

    override fun delete(notification: Notification) {
        notificationRepository.save(NotificationEntity.of(notification.copy(isDeleted = true)))
    }

    override fun countUnreadByUserId(userId: Long): Long {
        return notificationRepository.countByUserIdAndIsReadAndDeleted(
            userId = userId,
            isRead = false,
            isDeleted = false
        )
    }
}
