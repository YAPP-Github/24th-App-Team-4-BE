package com.pokit.alert.port.service

import com.pokit.alert.dto.response.AlertsResponse
import com.pokit.alert.dto.response.toAlertsResponse
import com.pokit.alert.exception.AlertErrorCode
import com.pokit.alert.model.Alert
import com.pokit.alert.model.AlertBatch
import com.pokit.alert.model.AlertContent
import com.pokit.alert.port.`in`.AlertUseCase
import com.pokit.alert.port.out.AlertBatchPort
import com.pokit.alert.port.out.AlertContentPort
import com.pokit.alert.port.out.AlertPort
import com.pokit.alert.port.out.AlertSender
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.model.ContentDefault
import com.pokit.content.port.out.ContentPort
import com.pokit.user.port.out.FcmTokenPort
import org.springframework.data.domain.PageRequest
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
    private val alertPort: AlertPort,
    private val alertBatchPort: AlertBatchPort,
    private val alertSender: AlertSender,
    private val fcmTokenPort: FcmTokenPort,
    private val alertContentPort: AlertContentPort,
    private val contentPort: ContentPort
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

    override fun loadAllAlertBatch(currentPageNum: Int, pageSize: Int): List<AlertBatch> {
        val pageable = PageRequest.of(currentPageNum, pageSize)
        return alertBatchPort.loadAllByShouldBeSentAt(now.get().toLocalDate(), pageable).content
    }

    @Transactional
    override fun sendMessage(alertBatch: AlertBatch) {
        val tokens = fcmTokenPort.loadByUserId(alertBatch.userId)
            .map { it.token }
        alertBatchPort.send(alertBatch)
        alertSender.send(tokens)
    }

    override fun fetchAllAlertContent(ids: List<Long>): List<AlertContent> {
        return alertContentPort.loadAllInAlertBatchIds(ids)
    }

    @Transactional
    override fun createAlerts(alertContents: List<AlertContent>) {
        val contents = contentPort.loadByContentIdsWithUser(alertContents.map { it.contentId })
        val alerts = contents.map {
            Alert(
                userId = it.userId,
                contentId = it.contentId,
                contentThumbNail = it.thumbNail ?: ContentDefault.THUMB_NAIL,
                title = it.title
            )
        }
        alertPort.persistAlerts(alerts)
        alertContentPort.deleteAll(alertContents.map { it.id })
    }
}
