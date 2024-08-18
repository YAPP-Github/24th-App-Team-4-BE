package com.pokit.content.dto.response

import com.pokit.content.model.Content
import com.pokit.content.model.ContentDefault
import java.time.LocalDateTime

data class SharedContentResult (
    val contentId: Long,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val createdAt: LocalDateTime,
    val thumbNail: String,
) {
    companion object {
        fun of(content: Content): SharedContentResult {
            return SharedContentResult(
                contentId = content.id,
                data = content.data,
                domain = content.domain,
                title = content.title,
                memo = content.memo,
                createdAt = content.createdAt,
                thumbNail = content.thumbNail ?: ContentDefault.THUMB_NAIL
            )
        }
    }
}
