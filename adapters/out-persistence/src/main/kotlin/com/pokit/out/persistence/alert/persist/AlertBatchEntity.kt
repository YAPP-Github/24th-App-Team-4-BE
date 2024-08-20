package com.pokit.out.persistence.alert.persist

import com.pokit.alert.model.AlertBatch
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "ALERT_BATCH")
@Entity
class AlertBatchEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "content_id")
    val contentId: Long,

    @Column(name = "content_thumb_nail")
    val contentThumbNail: String,

    @Column(name = "title")
    val title: String,

    @Column(name = "is_sent")
    var sent: Boolean = false
) : BaseEntity() {

    fun send() {
        this.sent = true
    }

    companion object {
        fun of(alertBatch: AlertBatch) = AlertBatchEntity(
            userId = alertBatch.userId,
            contentId = alertBatch.userId,
            contentThumbNail = alertBatch.contentThumbNail,
            title = alertBatch.title
        )
    }
}
