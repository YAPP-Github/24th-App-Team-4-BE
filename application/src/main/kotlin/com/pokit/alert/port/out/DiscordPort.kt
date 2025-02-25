package com.pokit.alert.port.out

import com.pokit.alert.dto.request.DiscordRequest

interface DiscordPort {
    fun sendReportedContent(request: DiscordRequest)
}
