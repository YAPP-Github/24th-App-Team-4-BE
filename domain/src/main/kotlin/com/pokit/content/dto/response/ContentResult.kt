package com.pokit.content.dto.response

import com.pokit.category.model.Category
import com.pokit.content.model.CategoryInfo
import com.pokit.content.model.Content
import com.pokit.user.model.User
import java.time.LocalDateTime

data class ContentResult(
    val contentId: Long,
    val category: CategoryInfo,
    val data: String,
    val title: String,
    val memo: String,
    val createdAt: LocalDateTime,
    val favorites: Boolean = false,
    val keyword: String,
    val userNickname: String,
)

fun Content.toGetContentResult(favorites: Boolean, category: Category, user: User): ContentResult {
    return ContentResult(
        contentId = this.id,
        category = CategoryInfo(category.categoryId, category.categoryName),
        data = this.data,
        title = this.title,
        memo = this.memo,
        createdAt = this.createdAt,
        favorites = favorites,
        keyword = category.keyword.kor,
        userNickname = user.nickName
    )
}
