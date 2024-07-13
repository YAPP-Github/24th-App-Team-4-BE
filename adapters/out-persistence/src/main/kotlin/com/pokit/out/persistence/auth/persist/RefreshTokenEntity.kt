package com.pokit.out.persistence.auth.persist

import com.pokit.token.model.RefreshToken
import jakarta.persistence.*

@Table(name = "REFRESH_TOKEN")
@Entity
class RefreshTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "userId")
    val userId: Long,

    @Column(name = "token")
    val token: String,
) {
    companion object {
        fun of(refreshToken: RefreshToken) =
            RefreshTokenEntity(
                userId = refreshToken.userId,
                token = refreshToken.token,
            )
    }
}

fun RefreshTokenEntity.toDomain() = RefreshToken(userId = this.userId, token = this.token)
