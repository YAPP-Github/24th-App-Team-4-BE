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
    var nickname: String = email
) {
    companion object {
        fun of(user: User) =
            UserEntity(
                email = user.email,
                role = user.role,
                nickname = user.nickName
            )
    }
}

fun UserEntity.toDomain() = User(
    id = this.id,
    email = this.email,
    role = this.role,
    nickName = this.nickname
)

fun UserEntity.registerInfo(user: User) {
    this.nickname = user.nickName
}
