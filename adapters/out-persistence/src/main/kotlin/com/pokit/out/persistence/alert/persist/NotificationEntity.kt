package com.pokit.out.persistence.alert.persist

import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "NOTIFICATION")
@Entity
class NotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "notification_time")
    val notificationTime: LocalDateTime,

    @Column(name = "content_id")
    val contentId: Long,

    @Column(name = "data")
    val data: String,
) {
}
