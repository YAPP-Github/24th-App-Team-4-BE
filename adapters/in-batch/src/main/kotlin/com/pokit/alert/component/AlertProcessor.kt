package com.pokit.alert.component

import com.pokit.alert.model.AlertBatch
import com.pokit.alert.port.`in`.AlertUseCase
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class AlertProcessor(
    private val alertUseCase: AlertUseCase,
) : ItemProcessor<AlertBatch, AlertBatch> {

    override fun process(alertBatch: AlertBatch): AlertBatch {
        alertUseCase.sendMessage(alertBatch)
        return alertBatch
    }
}
