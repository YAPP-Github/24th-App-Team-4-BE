package com.pokit.out.persistence.alert.impl

import com.pokit.alert.model.Alert
import com.pokit.alert.port.out.AlertPort
import com.pokit.out.persistence.alert.persist.AlertRepository
import com.pokit.out.persistence.alert.persist.toDomain
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class AlertAdapter(
    private val alertRepository: AlertRepository
) : AlertPort {
    override fun loadAllByUserId(userId: Long, pageable: Pageable): Slice<Alert> {
        return alertRepository.findAllByUserIdAndDeleted(userId, false, pageable)
            .map { it.toDomain() }
    }

    override fun loadByIdAndUserId(id: Long, userId: Long): Alert? {
        return alertRepository.findByIdAndUserId(id, userId)
            ?.toDomain()
    }

    override fun delete(alert: Alert) {
        alertRepository.findByIdOrNull(alert.id)
            ?.delete()
    }

}
