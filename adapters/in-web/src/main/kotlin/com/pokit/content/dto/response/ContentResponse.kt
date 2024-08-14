package com.pokit.content.dto.response

import com.pokit.content.model.CategoryInfo
import com.pokit.content.model.Content
import java.time.format.DateTimeFormatter

data class ContentResponse(
    val contentId: Long,
    val categoryId: Long,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: String,
    val favorites: Boolean = false
)

data class GetContentResponse(
    val contentId: Long,
    val category: CategoryInfo,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: String,
    val favorites: Boolean = false
)

fun Content.toResponse() = ContentResponse(
    contentId = this.id,
    categoryId = this.categoryId,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn,
    createdAt = this.createdAt.toString()
)

fun GetContentResult.toResponse(): GetContentResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return GetContentResponse(
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
