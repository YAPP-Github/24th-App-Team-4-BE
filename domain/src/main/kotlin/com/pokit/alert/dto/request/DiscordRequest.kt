package com.pokit.alert.dto.request

data class DiscordRequest(
    val reportedContentId: Long,
    val reporterId: Long,
    val contentsUserId: Long,
    val data: String,
    val createdAt: String,
)
