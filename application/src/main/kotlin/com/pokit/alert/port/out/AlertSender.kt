package com.pokit.alert.port.out

import com.pokit.alert.model.Alert

interface AlertSender {
    fun sendMessage(tokens: List<String>, alert: Alert)
}
