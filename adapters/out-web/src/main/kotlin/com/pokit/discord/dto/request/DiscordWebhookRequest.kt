package com.pokit.discord.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.pokit.alert.dto.request.DiscordRequest

data class DiscordWebhookRequest(
    @JsonProperty("content")
    val content: String = "🚨 신고 발생 🚨",
    @JsonProperty("embeds")
    val embeds: List<DiscordEmbed>
)

data class DiscordEmbed(
    val description: String // 신고 상세 내용
)

internal fun DiscordRequest.toWebhookRequest(): DiscordWebhookRequest {
    val embed = DiscordEmbed(
        description = """
            신고된 링크 ID : ${this.reportedContentId}
            신고자 ID : ${this.reporterId}
            신고된 링크 작성자 ID : ${this.contentsUserId}
            신고된 링크 : ${this.data}
            신고 사유 : ${this.reportReason.description}
        """.trimIndent()
    )

    return DiscordWebhookRequest(
        content = "🚨 신고 발생 🚨",
        embeds = listOf(embed)
    )
}
