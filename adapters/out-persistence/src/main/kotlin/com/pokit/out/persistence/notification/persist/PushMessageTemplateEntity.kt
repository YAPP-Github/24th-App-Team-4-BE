package com.pokit.out.persistence.notification.persist

import com.pokit.notification.model.NavigationType
import com.pokit.notification.model.NotificationType
import com.pokit.notification.model.PushMessageTemplate
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "push_message_template")
@Entity
class PushMessageTemplateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    val notificationType: NotificationType,

    @Column(name = "title", nullable = false, length = 255)
    val title: String,

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    val body: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "navigation_type", nullable = false, length = 50)
    val navigationType: NavigationType,
) : BaseEntity() {

    companion object {
        fun of(pushMessageTemplate: PushMessageTemplate) = PushMessageTemplateEntity(
            id = pushMessageTemplate.id,
            notificationType = pushMessageTemplate.notificationType,
            title = pushMessageTemplate.title,
            body = pushMessageTemplate.body,
            navigationType = pushMessageTemplate.navigationType,
        )
    }
}

fun PushMessageTemplateEntity.toDomain() = PushMessageTemplate(
    id = this.id,
    notificationType = this.notificationType,
    title = this.title,
    body = this.body,
    navigationType = this.navigationType,
)
