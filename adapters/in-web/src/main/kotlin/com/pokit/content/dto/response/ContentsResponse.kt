package com.pokit.content.dto.response

import java.time.format.DateTimeFormatter

data class ContentsResponse(
    val contentId: Long,
    val categoryId: Long,
    val categoryName: String,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: String,
    val isRead: Boolean,
    val thumbNail: String,
)

fun ContentsResult.toResponse(): ContentsResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return ContentsResponse(
        contentId = this.contentId,
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        data = this.data,
        domain = this.domain,
        title = this.title,
        memo = this.memo,
        alertYn = this.alertYn,
        createdAt = this.createdAt.format(formatter),
        isRead = this.isRead,
        thumbNail = this.thumbNail
    )
}
