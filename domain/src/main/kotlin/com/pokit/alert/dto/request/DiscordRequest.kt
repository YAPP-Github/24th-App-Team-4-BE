package com.pokit.alert.dto.request

import com.pokit.content.model.ReportReason

data class DiscordRequest(
    val reportedContentId: Long,
    val reporterId: Long,
    val contentsUserId: Long,
    val data: String,
    val reportReason: ReportReason,
    val createdAt: String,
)
