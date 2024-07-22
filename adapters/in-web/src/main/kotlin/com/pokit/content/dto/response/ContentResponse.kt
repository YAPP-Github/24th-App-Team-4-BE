package com.pokit.content.dto.response

import com.pokit.content.model.Content

data class ContentResponse(
    val contentId: Long,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String
)

fun Content.toResponse() = ContentResponse(
    contentId = this.id,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn
)
