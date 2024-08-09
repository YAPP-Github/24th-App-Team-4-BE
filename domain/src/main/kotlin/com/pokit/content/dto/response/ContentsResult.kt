package com.pokit.content.dto.response

import com.pokit.category.model.RemindCategory
import com.pokit.content.model.Content
import com.pokit.content.model.ContentDefault
import java.time.LocalDateTime

data class ContentsResult(
    val contentId: Long,
    val category: RemindCategory,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
    val thumbNail: String
) {
    companion object {
        fun of(content: Content, categoryName: String, isRead: Long): ContentsResult {
            return ContentsResult(
                contentId = content.id,
                category = RemindCategory(content.categoryId, categoryName),
                data = content.data,
                domain = content.domain,
                title = content.title,
                memo = content.memo,
                alertYn = content.alertYn,
                createdAt = content.createdAt,
                isRead = isRead > 0,
                thumbNail = content.thumbNail ?: ContentDefault.THUMB_NAIL
            )
        }
    }
}
