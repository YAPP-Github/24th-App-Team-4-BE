package com.pokit.out.persistence.user.persist

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
}
