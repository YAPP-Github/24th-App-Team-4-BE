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

data class CategorizeCommand(
    val contentIds: List<Long>,
    val categoryId: Long
)

fun ContentCommand.toDomain(userId: Long) = Content(
    categoryId = this.categoryId,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn,
    thumbNail = this.thumbNail,
    userId = userId,
)
