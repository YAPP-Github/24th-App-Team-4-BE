package com.pokit.alert.model

import com.pokit.content.model.ContentInfo

data class Alert(
    val id: Long,
    val userId: Long,
    val content: ContentInfo,
    val title: String,
    val body: String
)
