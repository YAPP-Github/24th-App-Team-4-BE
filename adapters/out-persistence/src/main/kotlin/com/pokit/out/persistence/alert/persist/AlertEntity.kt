package com.pokit.out.persistence.alert.persist

import com.pokit.alert.model.Alert
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "ALERT")
@Entity
class AlertEntity(
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

    @Column(name = "is_deleted")
    var deleted: Boolean = false
) : BaseEntity() {

    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(alert: Alert) = AlertEntity(
            userId = alert.userId,
            contentId = alert.contentId,
            contentThumbNail = alert.contentThumbNail,
            title = alert.title,
        )
    }
}

fun AlertEntity.toDomain() = Alert(
    id = this.id,
    userId = this.userId,
    contentId = this.contentId,
    contentThumbNail = this.contentThumbNail,
    title = this.title,
    createdAt = this.createdAt
)
