package com.pokit.alert.port.out

import com.pokit.alert.model.AlertBatch

interface AlertSender {
    fun send(tokens: List<String>, alertBatch: AlertBatch)
}
