package com.pokit.content.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.pokit.category.model.RemindCategory
import java.time.format.DateTimeFormatter

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RemindContentResponse(
    val contentId: Long,
    val category: RemindCategory,
    val title: String,
    val data: String,
    val createdAt: String,
    val domain: String,
    val isRead: Boolean?,
    val thumbNail: String,
)

fun RemindContentResult.toResponse(): RemindContentResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return RemindContentResponse(
        contentId = this.contentId,
        category = this.category,
        data = this.data,
        title = this.title,
        createdAt = this.createdAt.format(formatter),
        domain = this.domain,
        isRead = this.isRead,
        thumbNail = this.thumbNail
    )
}
