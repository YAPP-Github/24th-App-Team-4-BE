package com.pokit.alert.port.out

import com.pokit.alert.model.Alert
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface AlertPort {
    fun loadAllByUserId(userId: Long, pageable: Pageable): Slice<Alert>

    fun loadByIdAndUserId(id: Long, userId: Long): Alert?

    fun delete(alert: Alert)

    fun persistAlerts(alerts: List<Alert>)
}
