package com.pokit.discord.impl

import com.pokit.alert.dto.request.DiscordRequest
import com.pokit.alert.port.out.DiscordPort
import com.pokit.discord.dto.request.toWebhookRequest
import com.pokit.discord.support.DiscordFeignClient
import org.springframework.stereotype.Component

@Component
class DiscordSender(
    private val discordFeignClient: DiscordFeignClient,
) : DiscordPort {

    override fun sendReportedContent(request: DiscordRequest) {
        val sendBody = request.toWebhookRequest()
        discordFeignClient.sendWebhook(sendBody)
    }
}
