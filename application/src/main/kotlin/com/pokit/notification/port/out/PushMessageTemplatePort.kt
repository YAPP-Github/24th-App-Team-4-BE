package com.pokit.notification.port.out

import com.pokit.notification.model.NotificationType
import com.pokit.notification.model.PushMessageTemplate

interface PushMessageTemplatePort {
    fun loadByType(type: NotificationType): PushMessageTemplate?
}
