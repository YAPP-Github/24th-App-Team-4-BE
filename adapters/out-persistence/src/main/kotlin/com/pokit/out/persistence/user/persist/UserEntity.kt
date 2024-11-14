package com.pokit.out.persistence.user.persist

import com.pokit.out.persistence.BaseEntity
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
    val email: String = "EMAIL@EMAIL.COM",

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "nickname")
    var nickname: String = "NOT_REGISTERED",

    @Column(name = "auth_platform")
    @Enumerated(EnumType.STRING)
    val authPlatform: AuthPlatform,

    @Column(name = "deleted")
    var deleted: Boolean = false,

    @Column(name = "is_registered")
    var registered: Boolean,

    @Column(name = "sub")
    var sub: String?,

    @Column(name = "profile_image")
    var profileImage: String?
) : BaseEntity() {
    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(user: User) =
            UserEntity(
                id = user.id,
                email = user.email,
                role = user.role,
                nickname = user.nickName,
                authPlatform = user.authPlatform,
                registered = user.registered,
                sub = user.sub,
                profileImage = user.profileImage
            )
    }
}

fun UserEntity.toDomain() = User(
    id = this.id,
    email = this.email,
    role = this.role,
    nickName = this.nickname,
    authPlatform = this.authPlatform,
    registered = this.registered,
    sub = this.sub,
    profileImage = this.profileImage
)
