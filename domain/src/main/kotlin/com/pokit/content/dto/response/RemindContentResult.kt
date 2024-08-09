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
    val domain: String,
    val isRead: Boolean,
    val thumbNail: String
)

fun Content.toRemindContentResult(isRead: Boolean, category: RemindCategory): RemindContentResult {
    val defaultThumbNail = "https://pokit-storage.s3.ap-northeast-2.amazonaws.com/logo/pokit.png"

    return RemindContentResult(
        contentId = this.id,
        category = category,
        data = this.data,
        title = this.title,
        createdAt = this.createdAt,
        isRead = isRead,
        domain = this.domain,
        thumbNail = this.thumbNail ?: defaultThumbNail
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
