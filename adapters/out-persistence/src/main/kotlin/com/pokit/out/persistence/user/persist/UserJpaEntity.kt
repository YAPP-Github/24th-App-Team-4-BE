package com.pokit.out.persistence.user.persist

import com.pokit.user.model.Role
import com.pokit.user.model.User
import jakarta.persistence.*

@Table(name = "USER")
@Entity
class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,
    @Column(name = "email")
    val email: String,
    @Column(name = "role")
    val role: Role,
) {
    companion object {
        fun of(user: User) =
            UserJpaEntity(
                email = user.email,
                role = user.role,
            )
    }
}

fun UserJpaEntity.toDomain() = User(id = this.id, email = this.email, role = this.role)

fun UserJpaEntity.toPrincipalUser() = null // Security Context에 담을 user