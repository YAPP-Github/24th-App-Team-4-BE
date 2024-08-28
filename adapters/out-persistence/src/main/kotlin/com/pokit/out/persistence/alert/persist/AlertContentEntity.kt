package com.pokit.out.persistence.alert.persist

import com.pokit.alert.model.AlertContent
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "alert_content")
@Entity
class AlertContentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "alert_batch_id")
    val alertBatchId: Long,

    @Column(name = "content_id")
    val contentId: Long,
) : BaseEntity() {

    companion object {
        fun of(alertContent: AlertContent) = AlertContentEntity(
            alertBatchId = alertContent.alertBatchId,
            contentId = alertContent.contentId,
        )
    }
}

fun AlertContentEntity.toDomain() = AlertContent(
    id = this.id,
    alertBatchId = this.alertBatchId,
    contentId = this.contentId,
)
