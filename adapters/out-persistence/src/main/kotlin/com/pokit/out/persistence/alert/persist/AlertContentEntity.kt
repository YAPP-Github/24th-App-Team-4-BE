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

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "content_id")
    val contentId: Long,

    @Column(name = "content_thumb_nail")
    val contentThumbNail: String,

    @Column(name = "title")
    val title: String,

    @Column(name = "is_deleted")
    var deleted: Boolean = false
) : BaseEntity() {
    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(alertContent: AlertContent) = AlertContentEntity(
            alertBatchId = alertContent.alertBatchId,
            userId = alertContent.userId,
            contentId = alertContent.contentId,
            contentThumbNail = alertContent.contentThumbNail,
            title = alertContent.title
        )
    }
}

fun AlertContentEntity.toDomain() = AlertContent(
    id = this.id,
    alertBatchId = this.alertBatchId,
    userId = this.userId,
    contentId = this.contentId,
    contentThumbNail = this.contentThumbNail,
    title = this.title
)
