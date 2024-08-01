package com.pokit.content.model

import com.pokit.content.dto.ContentCommand

import java.net.URI
import java.time.LocalDateTime

data class Content(
    val id: Long = 0L,
    var categoryId: Long,
    var type: ContentType = ContentType.LINK,
    var data: String,
    var title: String,
    var memo: String,
    var alertYn: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var domain: String = data
) {
    fun modify(contetCommand: ContentCommand) {
        this.categoryId = contetCommand.categoryId
        this.data = contetCommand.data
        this.title = contetCommand.title
        this.memo = contetCommand.memo
        this.alertYn = contetCommand.alertYn
    }

    fun parseDomain() {
        val domain = URI(this.data).host
        val parts = domain.split(".")
        this.domain = when {
            domain.contains("youtube") || domain == "youtu.be" -> "youtube"
            parts.size > 2 && parts[parts.size - 2] == "co" -> parts[parts.size - 3] // ex) www.pokit.co.kr
            parts.size > 2 -> parts[parts.size - 2] // ex) www.pokit.com
            parts.size == 2 -> parts[0]
            else -> domain
        }
    }
}
