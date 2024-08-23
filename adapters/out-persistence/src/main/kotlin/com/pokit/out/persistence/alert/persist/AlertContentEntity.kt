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

    @Column(name = "is_deleted")
    var delete: Boolean = false
) : BaseEntity() {
    fun delete() {
        this.delete = true
    }

    companion object {
        fun of(alertContent: AlertContent) = AlertContentEntity(
            alertBatchId = alertContent.alertBatchId,
            contentId = alertContent.contentId
        )
    }
}
