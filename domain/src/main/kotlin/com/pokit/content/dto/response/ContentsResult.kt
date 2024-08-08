package com.pokit.content.dto.response

import com.pokit.content.model.Content
import com.pokit.content.model.CategoryInfo
import java.time.LocalDateTime

data class ContentsResult(
    val contentId: Long,
    val category: CategoryInfo,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean,
    val thumbNail: String = "https://pokit-storage.s3.ap-northeast-2.amazonaws.com/category-image/-3+1.png" // TODO 추가 예정
) {
    companion object {
        fun of(content: Content, categoryName: String, isRead: Long): ContentsResult {
            return ContentsResult(
                contentId = content.id,
                category = CategoryInfo(content.categoryId, categoryName),
                data = content.data,
                domain = content.domain,
                title = content.title,
                memo = content.memo,
                alertYn = content.alertYn,
                createdAt = content.createdAt,
                isRead = isRead > 0
            )
        }
    }
}
