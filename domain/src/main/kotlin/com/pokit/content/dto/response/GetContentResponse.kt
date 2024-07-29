package com.pokit.content.dto.response

import com.pokit.bookmark.model.Bookmark
import com.pokit.content.model.Content
import java.time.LocalDateTime

data class GetContentResponse(
    val contentId: Long,
    val categoryId: Long,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: LocalDateTime,
    val favorites: Boolean = false
)

fun Content.toGetContentResponse(bookmark: Bookmark): GetContentResponse {
    return GetContentResponse(
        contentId = this.id,
        categoryId = this.categoryId,
        data = this.data,
        title = this.title,
        memo = this.memo,
        alertYn = this.alertYn,
        createdAt = this.createdAt,
        favorites = true
    )
}

fun Content.toGetContentResponse(): GetContentResponse {
    return GetContentResponse(
        contentId = this.id,
        categoryId = this.categoryId,
        data = this.data,
        title = this.title,
        memo = this.memo,
        alertYn = this.alertYn,
        createdAt = this.createdAt,
        favorites = false
    )
}
