package com.pokit.alert.port.`in`

import com.pokit.alert.dto.response.AlertsResponse
import com.pokit.alert.model.AlertBatch
import com.pokit.alert.model.AlertContent
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface AlertUseCase {
    fun getAlerts(userId: Long, pageable: Pageable): Slice<AlertsResponse>

    fun delete(userId: Long, alertId: Long)

    fun loadAllAlertBatch(currentPageNum: Int, pageSize: Int): List<AlertBatch>

    fun sendMessage(alertBatch: AlertBatch)

    fun fetchAllAlertContent(ids: List<Long>): List<AlertContent>

    fun createAlerts(alertContents: List<AlertContent>)
}
