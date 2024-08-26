package com.pokit.alert.port.out

import com.pokit.alert.model.AlertContent

interface AlertContentPort {
    fun loadAllInAlertBatchIds(ids: List<Long>): List<AlertContent>

    fun deleteAll(ids: List<Long>)
}
