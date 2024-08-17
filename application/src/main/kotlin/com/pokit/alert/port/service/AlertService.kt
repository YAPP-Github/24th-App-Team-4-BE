package com.pokit.alert.port.service

import com.pokit.alert.dto.response.AlertsResponse
import com.pokit.alert.dto.response.toAlertsResponse
import com.pokit.alert.exception.AlertErrorCode
import com.pokit.alert.port.`in`.AlertUseCase
import com.pokit.alert.port.out.AlertPort
import com.pokit.common.exception.NotFoundCustomException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.function.Supplier
import kotlin.math.abs

@Service
@Transactional(readOnly = true)
class AlertService(
    private val now: Supplier<LocalDateTime>,
    private val alertPort: AlertPort
) : AlertUseCase {
    override fun getAlerts(userId: Long, pageable: Pageable): Slice<AlertsResponse> {
        val nowDay = now.get().toLocalDate()

        return alertPort.loadAllByUserId(userId, pageable)
            .map {
                val dayDifference = ChronoUnit.DAYS.between(nowDay, it.createdAt.toLocalDate())
                it.toAlertsResponse(abs(dayDifference.toInt()))
            }
    }

    @Transactional
    override fun delete(userId: Long, alertId: Long) {
        val alert = alertPort.loadByIdAndUserId(alertId, userId)
            ?: throw NotFoundCustomException(AlertErrorCode.NOT_FOUND_ALERT)
        alertPort.delete(alert)
    }
}
