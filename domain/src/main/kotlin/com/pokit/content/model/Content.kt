package com.pokit.content.model

data class Content(
    val categoryId: Long,
    val type: ContentType,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
)
