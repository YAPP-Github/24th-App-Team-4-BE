package com.pokit.auth.model

import com.pokit.user.model.Role
import com.pokit.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class PrincipalUser(
    val id: Long,
    val email: String,
    val role: Role,
) : UserDetails {
    companion object {
        fun of(user: User) =
            PrincipalUser(
                id = user.id,
                email = user.email,
                role = user.role,
            )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.description))
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return email
    }
}

fun PrincipalUser.toDomain() = User(id = this.id, email = this.email, role = this.role)
