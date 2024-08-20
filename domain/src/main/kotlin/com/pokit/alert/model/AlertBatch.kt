package com.pokit.alert.model

import java.time.LocalDateTime

data class AlertBatch(
    val id: Long = 0L,
    val userId: Long,
    val contentId: Long,
    val contentThumbNail: String,
    val title: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
