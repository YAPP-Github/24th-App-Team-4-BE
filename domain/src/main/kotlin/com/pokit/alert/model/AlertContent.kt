package com.pokit.alert.model

data class AlertContent(
    val id: Long = 0L,
    val alertBatchId: Long,
    val userId: Long,
    val contentId: Long,
    val contentThumbNail: String,
    val title: String
)
