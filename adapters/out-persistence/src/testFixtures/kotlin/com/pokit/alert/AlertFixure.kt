package com.pokit.alert

import com.pokit.alert.model.Alert
import java.time.LocalDateTime

class AlertFixure {
    companion object {
        fun getAlert(userId: Long, title: String, createdAt: LocalDateTime) = Alert(
            userId = userId,
            contentId = 1L,
            contentThumbNail = "www.imageThumbnail.com",
            title = title,
            createdAt = createdAt
        )
    }
}
