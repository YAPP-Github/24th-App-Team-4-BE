package com.pokit.content.model

data class ReportedContent(
    val id: Long = 0L,
    val reporterId: Long,
    val contentId: Long,
)
