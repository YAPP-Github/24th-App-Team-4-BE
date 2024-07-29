package com.pokit.out.persistence.user.persist

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?

    fun existsByNickname(nickname: String): Boolean

    fun findByIdAndDeleted(id: Long, deleted: Boolean): UserEntity?
}
