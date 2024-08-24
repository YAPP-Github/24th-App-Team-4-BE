package com.pokit.content.port.out

import com.pokit.content.dto.response.RemindContentResult

interface DailyContentPort {
    fun loadByUserId(userId: Long): List<RemindContentResult>
    fun fetchContentIdsByUserId(userId: Long): List<Long>
    fun persist(userId: Long, ids: List<Long>)
    fun deleteAll()
}
