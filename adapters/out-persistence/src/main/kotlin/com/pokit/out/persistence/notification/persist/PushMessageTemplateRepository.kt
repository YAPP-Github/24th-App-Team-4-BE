package com.pokit.out.persistence.notification.persist

import com.pokit.notification.model.NotificationType
import org.springframework.data.jpa.repository.JpaRepository

interface PushMessageTemplateRepository : JpaRepository<PushMessageTemplateEntity, Long> {
    fun findByNotificationType(notificationType: NotificationType): PushMessageTemplateEntity?
}
