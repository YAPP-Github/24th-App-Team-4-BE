package com.pokit.alert.component

import com.pokit.alert.model.AlertBatch
import com.pokit.alert.port.`in`.AlertUseCase
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
class AlertReader(
    private val alertUseCase: AlertUseCase,
) : ItemReader<AlertBatch> {

    private var currentPage: Int = 0
    private val pageSize: Int = 50
    private var alertBatchList: MutableList<AlertBatch> = mutableListOf()

    override fun read(): AlertBatch? {
        if (alertBatchList.isEmpty()) {
            val alertBatches = alertUseCase.loadAllAlertBatch(currentPage++, pageSize)
            alertBatchList.addAll(alertBatches)

            if (alertBatchList.isEmpty()) {
                return null
            }
        }

        return alertBatchList.removeFirst()
    }
}
