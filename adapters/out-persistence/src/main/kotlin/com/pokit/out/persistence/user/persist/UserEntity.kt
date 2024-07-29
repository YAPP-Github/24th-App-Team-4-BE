package com.pokit.out.persistence.user.persist

import com.pokit.token.model.AuthPlatform
import com.pokit.user.model.Role
import com.pokit.user.model.User
import jakarta.persistence.*

@Table(name = "USER")
@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "email")
    val email: String,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "nickname")
    var nickname: String = email,

    @Column(name = "auth_platform")
    @Enumerated(EnumType.STRING)
    val authPlatform: AuthPlatform,

    @Column(name = "deleted")
    var deleted: Boolean = false
) {
    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(user: User) =
            UserEntity(
                email = user.email,
                role = user.role,
                nickname = user.nickName,
                authPlatform = user.authPlatform
            )
    }
}

fun UserEntity.toDomain() = User(
    id = this.id,
    email = this.email,
    role = this.role,
    nickName = this.nickname,
    authPlatform = this.authPlatform
)

fun UserEntity.registerInfo(user: User) {
    this.nickname = user.nickName
}
