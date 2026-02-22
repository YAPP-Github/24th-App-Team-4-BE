package com.pokit.out.persistence.notification.persist

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<NotificationEntity, Long> {
    fun findAllByUserIdAndDeleted(userId: Long, deleted: Boolean, pageable: Pageable): Slice<NotificationEntity>

    fun findByIdAndUserIdAndDeleted(id: Long, userId: Long, deleted: Boolean): NotificationEntity?

    fun countByUserIdAndIsReadAndDeleted(userId: Long, isRead: Boolean, isDeleted: Boolean): Long
}
