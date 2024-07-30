package com.pokit.content.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.pokit.content.model.Content

data class ContentResponse(
    val contentId: Long,
    val categoryId: Long,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:")
    val createdAt: String,
    val favorites: Boolean = false
)

data class RecentSearchResponse(
    val keywords: List<String?>
)

fun List<String?>.toResponse() = RecentSearchResponse(
    keywords = this
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

fun GetContentResponse.toResponse() = ContentResponse(
    contentId = this.contentId,
    categoryId = this.categoryId,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn,
    createdAt = this.createdAt.toString(),
    favorites = this.favorites
)
