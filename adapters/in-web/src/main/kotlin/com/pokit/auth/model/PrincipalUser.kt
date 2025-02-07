package com.pokit.auth.model

import com.pokit.token.model.AuthPlatform
import com.pokit.user.model.Role
import com.pokit.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class PrincipalUser(
    val id: Long,
    val email: String,
    val role: Role,
    val authPlatform: AuthPlatform,
    val sub: String?
) : UserDetails {
    companion object {
        fun of(user: User) =
            PrincipalUser(
                id = user.id,
                email = user.email,
                role = user.role,
                authPlatform = user.authPlatform,
                sub = user.sub
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

fun PrincipalUser.toDomain() = User(id = this.id, email = this.email, role = this.role, authPlatform = this.authPlatform, sub = this.sub)
