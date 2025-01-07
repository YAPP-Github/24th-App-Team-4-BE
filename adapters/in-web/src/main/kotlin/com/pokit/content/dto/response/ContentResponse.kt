package com.pokit.content.dto.response

import com.pokit.content.model.CategoryInfo
import java.time.format.DateTimeFormatter


data class ContentResponse(
    val contentId: Long,
    val category: CategoryInfo,
    val data: String,
    val title: String,
    val memo: String,
    val createdAt: String,
    val favorites: Boolean = false,
    val keyword: String,
    val userNickname: String,
)

fun ContentResult.toResponse(): ContentResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return ContentResponse(
        contentId = this.contentId,
        category = this.category,
        data = this.data,
        title = this.title,
        memo = this.memo,
        createdAt = this.createdAt.format(formatter),
        favorites = this.favorites,
        keyword = this.keyword,
        userNickname = this.userNickname,
    )
}
