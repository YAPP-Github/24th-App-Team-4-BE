package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.UserJpaEntity
import com.pokit.out.persistence.user.persist.UserJpaRepository
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.user.model.User
import com.pokit.user.port.out.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserAdapter(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun save(user: User): User {
        val userJpaEntity = UserJpaEntity.of(user)
        val savedUser = userJpaRepository.save(userJpaEntity)
        return savedUser.toDomain()
    }

    override fun findByEmail(email: String): User? {
        val userJpaEntity = userJpaRepository.findByEmail(email)
        return userJpaEntity?.toDomain()
    }
}
