package com.pokit.out.persistence.user.persist

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
    val role: Role,

    @Column(name = "nickname")
    var nickName: String = email,
) {
    companion object {
        fun of(user: User) =
            UserEntity(
                email = user.email,
                role = user.role,
            )
    }
}

fun UserEntity.toDomain() = User(id = this.id, email = this.email, role = this.role)

fun UserEntity.toPrincipalUser() = null // Security Context에 담을 user

fun UserEntity.registerInfo(user: User) {
    this.nickName = user.nickName
}
