package com.pokit.alert.port.event

import com.pokit.alert.model.AlertBatch
import com.pokit.alert.model.AlertContent
import com.pokit.alert.model.CreateAlertRequest
import com.pokit.alert.port.out.AlertBatchPort
import com.pokit.alert.port.out.AlertContentPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDate
import java.util.function.Supplier

@Component
class AlertEventHandler(
    private val now: Supplier<LocalDate>,
    private val alertBatchPort: AlertBatchPort,
    private val alertContentPort: AlertContentPort,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createAlert(request: CreateAlertRequest) {
        val alertBatch = alertBatchPort.loadByUserIdAndDate(request.userId, now.get().plusDays(7))
            ?: createAlertBatch(request.userId)
        val alertContent = AlertContent(
            alertBatchId = alertBatch.id,
            contentId = request.contetId
        )
        alertContentPort.persist(alertContent)
    }

    private fun createAlertBatch(userId: Long): AlertBatch {
        val alertBatch = AlertBatch(
            userId = userId,
            shouldBeSentAt = now.get().plusDays(7)
        )
        return alertBatchPort.persist(alertBatch)
    }
}
