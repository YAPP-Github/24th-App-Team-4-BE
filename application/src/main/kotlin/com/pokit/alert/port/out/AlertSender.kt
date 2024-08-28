package com.pokit.alert.port.out

interface AlertSender {
    fun send(tokens: List<String>)
}
