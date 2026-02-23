package com.pokit.out.persistence.notification.impl

import com.pokit.notification.model.NotificationType
import com.pokit.notification.model.PushMessageTemplate
import com.pokit.notification.port.out.PushMessageTemplatePort
import com.pokit.out.persistence.notification.persist.PushMessageTemplateRepository
import com.pokit.out.persistence.notification.persist.toDomain
import org.springframework.stereotype.Repository

@Repository
class PushMessageTemplateAdapter(
    private val pushMessageTemplateRepository: PushMessageTemplateRepository,
) : PushMessageTemplatePort {

    override fun loadByType(type: NotificationType): PushMessageTemplate? {
        return pushMessageTemplateRepository.findByNotificationType(type)?.toDomain()
    }
}
