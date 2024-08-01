package com.pokit.content.dto

import com.pokit.content.model.Content
import java.time.format.DateTimeFormatter

data class ContentsResponse(
    val contentId: Long,
    val categoryId: Long,
    val categoryName: String,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val alertYn: String,
    val createdAt: String,
    val isRead: Boolean
) {
    companion object {
        fun of(content: Content, categoryName: String, isRead: Long): ContentsResponse {
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            return ContentsResponse(
                contentId = content.id,
                categoryId = content.categoryId,
                categoryName = categoryName,
                data = content.data,
                domain = content.domain,
                title = content.title,
                memo = content.memo,
                alertYn = content.alertYn,
                createdAt = content.createdAt.format(formatter),
                isRead = isRead > 0
            )
        }
    }
}
