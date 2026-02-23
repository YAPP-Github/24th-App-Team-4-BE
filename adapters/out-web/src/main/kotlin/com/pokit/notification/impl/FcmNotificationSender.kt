package com.pokit.notification.impl

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.pokit.notification.model.Notification
import com.pokit.notification.port.out.NotificationSender
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import com.google.firebase.messaging.Notification as FirebaseNotification

@Component
class FcmNotificationSender : NotificationSender {
    @PostConstruct
    fun init() {
        if (FirebaseApp.getApps().isEmpty()) {
            val firebaseCredentials = ClassPathResource("/firebase/push-account-key.json").inputStream
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseCredentials))
                .build()
            FirebaseApp.initializeApp(options)
        }
    }

    private val logger = KotlinLogging.logger { }

    companion object {
        const val IMAGE_PATH = "https://pokit-storage.s3.ap-northeast-2.amazonaws.com/logo/pokit.png" // 앱 로고
    }

    override fun send(notification: Notification, tokens: List<String>) {
        val fcmNotification = FirebaseNotification.builder()
            .setTitle(notification.title)
            .setBody(notification.body)
            .setImage(IMAGE_PATH)
            .build()

        val messages = tokens.map { token ->
            Message.builder()
                .setNotification(fcmNotification)
                .putData("deepLink", notification.deepLink)
                .setToken(token)
                .build()
        }

        try {
            messages.forEach {
                FirebaseMessaging.getInstance().sendAsync(it)
            }
        } catch (e: FirebaseMessagingException) {
            logger.warn { "Failed to send notification: ${e.message}" }
        }
    }
}
