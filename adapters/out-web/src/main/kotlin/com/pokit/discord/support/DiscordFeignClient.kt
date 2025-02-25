package com.pokit.discord.support

import com.pokit.config.OpenFeignConfig
import com.pokit.discord.dto.request.DiscordWebhookRequest
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "discordWebhook",
    url = "\${webhook.discord.url}",
    configuration = [OpenFeignConfig::class]
)
interface DiscordFeignClient {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Headers("Content-Type: application/json")
    fun sendWebhook(@RequestBody request: DiscordWebhookRequest): ResponseEntity<Void>
}
