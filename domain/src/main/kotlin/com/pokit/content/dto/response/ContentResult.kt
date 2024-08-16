package com.pokit.content.dto.response

import com.pokit.category.model.Category
import com.pokit.content.model.CategoryInfo
import com.pokit.content.model.Content
import java.time.LocalDateTime

data class ContentResult(
    val contentId: Long,
    val category: CategoryInfo,
    val data: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: LocalDateTime,
    val favorites: Boolean = false
)

fun Content.toGetContentResult(favorites: Boolean, category: Category): ContentResult {
    return ContentResult(
        contentId = this.id,
        category = CategoryInfo(category.categoryId, category.categoryName),
        data = this.data,
        title = this.title,
        memo = this.memo,
        alertYn = this.alertYn,
        createdAt = this.createdAt,
        favorites = favorites
    )
}
