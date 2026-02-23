package com.pokit.notification.port.out

import com.pokit.notification.model.Notification

interface NotificationSender {
    fun send(notification: Notification, tokens: List<String>)
}
