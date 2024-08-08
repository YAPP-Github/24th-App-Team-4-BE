package com.pokit.content.dto.response

import com.pokit.content.model.CategoryInfo
import java.time.format.DateTimeFormatter

data class ContentsResponse(
    val contentId: Long,
    val category: CategoryInfo,
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
        category = this.category,
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
