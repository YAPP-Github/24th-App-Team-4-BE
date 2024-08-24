package com.pokit.content.port.`in`

import com.pokit.content.dto.response.RemindContentResult

interface DailyContentUseCase {
    fun registerDailyContent(userId: Long)
    fun deleteAll()
    fun getDailyContents(userId: Long): List<RemindContentResult>
}
