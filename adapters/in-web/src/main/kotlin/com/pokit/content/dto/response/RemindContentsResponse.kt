package com.pokit.content.dto.response

import com.pokit.category.model.RemindCategory
import java.time.format.DateTimeFormatter

data class RemindContentsResponse(
    val contentId: Long,
    val category: RemindCategory,
    val title: String,
    val data: String,
    val createdAt: String,
//    val domain: String,
    val isRead: Boolean,
)

fun RemindContentResult.toResponse(): RemindContentsResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return RemindContentsResponse(
        contentId = this.contentId,
        category = this.category,
        data = this.data,
        title = this.title,
        createdAt = this.createdAt.format(formatter),
        isRead = this.isRead
    )
}
