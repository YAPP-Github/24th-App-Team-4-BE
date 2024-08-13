package com.pokit.out.persistence.user.persist

import com.pokit.token.model.AuthPlatform
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmailAndAuthPlatformAndDeleted(email: String,authPlatform: AuthPlatform, deleted: Boolean): UserEntity?

    fun existsByNickname(nickname: String): Boolean

    fun findByIdAndDeleted(id: Long, deleted: Boolean): UserEntity?
}
