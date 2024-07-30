package com.pokit.content.dto.response

import com.pokit.category.model.RemindCategory
import com.pokit.content.model.Content
import java.time.LocalDateTime

data class RemindContentResult(
    val contentId: Long,
    val category: RemindCategory,
    val title: String,
    val data: String,
    val createdAt: LocalDateTime,
//    val domain: String,
    val isRead: Boolean,
)


fun Content.toRemindContentResult(isRead: Boolean, category: RemindCategory): RemindContentResult {
    return RemindContentResult(
        contentId = this.id,
        category = category,
        data = this.data,
        title = this.title,
        createdAt = this.createdAt,
        isRead = isRead,
    )
}
