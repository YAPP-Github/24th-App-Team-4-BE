package com.pokit.content.dto.response

import com.pokit.content.model.CategoryInfo
import java.time.format.DateTimeFormatter


data class ContentResponse(
    val contentId: Long,
    val category: CategoryInfo,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: String,
    val favorites: Boolean = false
)

fun ContentResult.toResponse(): ContentResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return ContentResponse(
        contentId = this.contentId,
        category = this.category,
        data = this.data,
        title = this.title,
        memo = this.memo,
        alertYn = this.alertYn,
        createdAt = this.createdAt.format(formatter),
        favorites = this.favorites
    )
}
