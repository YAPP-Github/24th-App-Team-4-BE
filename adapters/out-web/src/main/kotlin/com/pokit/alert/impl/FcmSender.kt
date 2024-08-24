package com.pokit.alert.impl

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.pokit.alert.model.AlertBatch
import com.pokit.alert.port.out.AlertSender
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class FcmSender : AlertSender {
    private val logger = KotlinLogging.logger { }

    companion object {
        const val IMAGE_PATH = "https://pokit-storage.s3.ap-northeast-2.amazonaws.com/logo/pokit.png" // 앱 로고
        const val title = "잊지 말고 링크를 확인하세요!"
        const val body = "저장하신 링크가 기다리고 있어요.\n잊지 말고 링크를 확인하세요!"
    }

    override fun send(tokens: List<String>, alertBatch: AlertBatch) {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .setImage(IMAGE_PATH)
            .build()
        val messages = tokens.map {
            Message.builder()
                .setNotification(notification)
                .setToken(it)
                .build()
        }

        try {
            messages.forEach {
                FirebaseMessaging.getInstance().sendAsync(it)
            }
        } catch (e: FirebaseMessagingException) {
            logger.warn { "Failed To Send Message : ${e.message}" }
        }
    }
}
