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
    val thumbNail: String,
    val isFavorite: Boolean,
    val keyword: String,
    val memoExists: Boolean,
) {
    companion object {
        fun of(
            content: Content,
            categoryName: String,
            isRead: Long,
            isFavorite: Long,
            keyword: String = "default"
        ): ContentsResult {
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
                thumbNail = content.thumbNail ?: ContentDefault.THUMB_NAIL,
                isFavorite = isFavorite > 0,
                keyword = keyword,
                memoExists = content.memo.isNotBlank(),
            )
        }
    }
}
