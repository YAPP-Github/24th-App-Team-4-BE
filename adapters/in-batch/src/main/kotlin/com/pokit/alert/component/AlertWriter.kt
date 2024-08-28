package com.pokit.alert.component

import com.pokit.alert.model.AlertBatch
import com.pokit.alert.port.`in`.AlertUseCase
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class AlertWriter(
    private val alertUseCase: AlertUseCase,
) : ItemWriter<AlertBatch> {
    override fun write(alertBatches: Chunk<out AlertBatch>) {
        val batchIds = alertBatches.map { it.id }
        val alertContents = alertUseCase.fetchAllAlertContent(batchIds)

        alertUseCase.createAlerts(alertContents)
    }
}
