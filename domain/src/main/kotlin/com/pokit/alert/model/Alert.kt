package com.pokit.alert.model

import java.time.LocalDateTime

data class Alert(
    val id: Long = 0L,
    val userId: Long,
    val contentId: Long,
    val contentThumbNail: String,
    val title: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

object AlertDefault {
    const val body = "저장하신 링크가 기다리고 있어요. 잊지 말고 링크를 확인하세요!"
}
