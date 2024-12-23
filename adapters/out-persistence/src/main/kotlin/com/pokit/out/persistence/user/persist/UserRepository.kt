package com.pokit.out.persistence.user.persist

import com.pokit.token.model.AuthPlatform
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findBySubAndAuthPlatformAndDeleted(sub: String, authPlatform: AuthPlatform, deleted: Boolean): UserEntity?

    fun existsByNickname(nickname: String): Boolean

    fun existsByNicknameAndIdNot(nickname: String, userId: Long): Boolean

    fun findByIdAndDeleted(id: Long, deleted: Boolean): UserEntity?

    fun findByDeleted(deleted: Boolean): List<UserEntity>

    fun findByEmailAndAuthPlatformAndDeleted(email: String, authPlatform: AuthPlatform, deleted: Boolean): UserEntity?

    fun findAllByIdIn(ids: List<Long>): List<UserEntity>
}
