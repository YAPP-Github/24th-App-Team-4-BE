package com.pokit.content.dto.response

import com.pokit.category.model.RemindCategory
import com.pokit.content.model.Content
import com.pokit.content.model.ContentDefault
import java.time.LocalDateTime

data class RemindContentResult(
    val contentId: Long,
    val category: RemindCategory,
    val title: String,
    val data: String,
    val createdAt: LocalDateTime,
    val domain: String,
    val isRead: Boolean?,
    val thumbNail: String
) {
    companion object {
        fun of(content: Content, categoryName: String): RemindContentResult {
            return RemindContentResult(
                contentId = content.id,
                category = RemindCategory(content.categoryId, categoryName),
                data = content.data,
                domain = content.domain,
                title = content.title,
                createdAt = content.createdAt,
                isRead = null,
                thumbNail = content.thumbNail ?: ContentDefault.THUMB_NAIL
            )
        }
    }
}

fun Content.toRemindContentResult(isRead: Boolean, category: RemindCategory): RemindContentResult {
    return RemindContentResult(
        contentId = this.id,
        category = category,
        data = this.data,
        title = this.title,
        createdAt = this.createdAt,
        isRead = isRead,
        domain = this.domain,
        thumbNail = this.thumbNail ?: ContentDefault.THUMB_NAIL
    )
}

fun ContentsResult.toRemindContentResult(): RemindContentResult {
    return RemindContentResult(
        contentId = this.contentId,
        category = this.category,
        data = this.data,
        title = this.title,
        createdAt = this.createdAt,
        domain = this.domain,
        isRead = this.isRead,
        thumbNail = this.thumbNail
    )
}
