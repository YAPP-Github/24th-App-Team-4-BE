package com.pokit.out.persistence.alert.persist

import com.pokit.alert.model.Alert
import com.pokit.content.model.ContentInfo
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

    @Column(name = "body")
    val body: String,

    @Column(name = "is_deleted")
    val deleted: Boolean = false

) : BaseEntity() {
    companion object {
        fun of(alert: Alert) = AlertEntity(
            userId = alert.userId,
            contentId = alert.content.contentId,
            contentThumbNail = alert.content.contentThumbNail,
            title = alert.title,
            body = alert.body,
        )
    }
}

fun AlertEntity.toDomain() = Alert(
    id = this.id,
    userId = this.userId,
    content = ContentInfo(this.contentId, this.contentThumbNail),
    title = this.title,
    body = this.body,
)
