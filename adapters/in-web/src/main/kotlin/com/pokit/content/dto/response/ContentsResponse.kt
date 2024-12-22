package com.pokit.content.dto.response


import com.pokit.category.model.RemindCategory
import java.time.format.DateTimeFormatter

data class ContentsResponse(
    val contentId: Long,
    val category: RemindCategory,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: String,
    val isRead: Boolean,
    val thumbNail: String,
    val isFavorite: Boolean,
    val keyword: String,
    val memoExists: Boolean,
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
        thumbNail = this.thumbNail,
        isFavorite = this.isFavorite,
        keyword = this.keyword,
        memoExists = this.memoExists,
    )
}
