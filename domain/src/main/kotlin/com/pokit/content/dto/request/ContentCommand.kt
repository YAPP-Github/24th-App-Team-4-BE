package com.pokit.content.dto.request

import com.pokit.content.model.Content

data class ContentCommand(
    val data: String,
    val title: String,
    val categoryId: Long,
    val memo: String,
    val alertYn: String,
    val thumbNail: String?
)

fun ContentCommand.toDomain() = Content(
    categoryId = this.categoryId,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn,
    thumbNail = this.thumbNail
)
