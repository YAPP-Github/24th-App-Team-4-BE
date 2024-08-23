package com.pokit.out.persistence.user.persist

import com.pokit.user.model.FcmToken
import jakarta.persistence.*

@Table(name = "FCM_TOKEN")
@Entity
class FcmTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "token")
    val token: String
) {
    companion object {
        fun of(fcmToken: FcmToken) = FcmTokenEntity(
            userId = fcmToken.userId,
            token = fcmToken.token
        )
    }
}
