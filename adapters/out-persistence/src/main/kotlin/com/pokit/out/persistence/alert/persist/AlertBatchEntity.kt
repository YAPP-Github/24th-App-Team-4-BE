package com.pokit.out.persistence.alert.persist

import com.pokit.alert.model.AlertBatch
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Table(name = "ALERT_BATCH")
@Entity
class AlertBatchEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "should_be_send_at")
    val shouldBeSentAt: LocalDate,

    @Column(name = "is_sent")
    var sent: Boolean = false
) : BaseEntity() {
    fun sent() {
        this.sent = true
    }

    companion object {
        fun of(alertBatch: AlertBatch) = AlertBatchEntity(
            userId = alertBatch.userId,
            shouldBeSentAt = alertBatch.shouldBeSentAt,
        )
    }
}
