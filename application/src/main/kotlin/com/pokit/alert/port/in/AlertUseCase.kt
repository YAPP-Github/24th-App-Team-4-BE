package com.pokit.alert.port.`in`

import com.pokit.alert.dto.response.AlertsResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface AlertUseCase {
    fun getAlerts(userId: Long, pageable: Pageable): Slice<AlertsResponse>

    fun delete(userId: Long, alertId: Long)
}
