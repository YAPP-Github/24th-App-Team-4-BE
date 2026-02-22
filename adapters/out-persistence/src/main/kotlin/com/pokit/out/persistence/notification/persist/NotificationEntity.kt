package com.pokit.out.persistence.notification.persist

import com.pokit.notification.model.NavigationType
import com.pokit.notification.model.Notification
import com.pokit.notification.model.NotificationType
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "notification")
@Entity
class NotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    val notificationType: NotificationType,

    @Column(name = "title", nullable = false, length = 255)
    val title: String,

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    val body: String,

    @Column(name = "thumbnail_url", length = 2048)
    val thumbnailUrl: String? = null,

    @Column(name = "is_read", nullable = false)
    val isRead: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "navigation_type", length = 50)
    val navigationType: NavigationType = NavigationType.NONE,

    @Column(name = "deep_link", length = 2048)
    val deepLink: String? = null,

    @Column(name = "is_deleted", nullable = false)
    val deleted: Boolean = false
) : BaseEntity() {

    companion object {
        fun of(notification: Notification) = NotificationEntity(
            id = notification.id,
            userId = notification.userId,
            notificationType = notification.notificationType,
            title = notification.title,
            body = notification.body,
            thumbnailUrl = notification.thumbnailUrl,
            isRead = notification.isRead,
            navigationType = notification.navigationType,
            deepLink = notification.deepLink,
            deleted = notification.isDeleted
        )
    }
}

fun NotificationEntity.toDomain() = Notification(
    id = this.id,
    userId = this.userId,
    notificationType = this.notificationType,
    title = this.title,
    body = this.body,
    thumbnailUrl = this.thumbnailUrl,
    isRead = this.isRead,
    navigationType = this.navigationType,
    deepLink = this.deepLink,
    isDeleted = this.deleted,
    createdAt = this.createdAt
)
