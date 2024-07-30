package com.pokit.content.model

import com.pokit.content.dto.ContentCommand
import java.time.LocalDateTime

data class Content(
    val id: Long = 0L,
    var categoryId: Long,
    var type: ContentType = ContentType.LINK,
    var data: String,
    var title: String,
    var memo: String,
    var alertYn: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun modify(contentCommand: ContentCommand) {
        this.categoryId = contentCommand.categoryId
        this.data = contentCommand.data
        this.title = contentCommand.title
        this.memo = contentCommand.memo
        this.alertYn = contentCommand.alertYn
    }
}
