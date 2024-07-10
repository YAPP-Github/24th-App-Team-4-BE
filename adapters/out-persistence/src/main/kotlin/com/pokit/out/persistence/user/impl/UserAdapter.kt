package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.UserJpaEntity
import com.pokit.out.persistence.user.persist.UserRepository
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.user.model.User
import com.pokit.user.port.out.UserPort
import org.springframework.stereotype.Repository

@Repository
class UserAdapter(
    private val userRepository: UserRepository,
) : UserPort {
    override fun persist(user: User): User {
        val userJpaEntity = UserJpaEntity.of(user)
        val savedUser = userRepository.save(userJpaEntity)
        return savedUser.toDomain()
    }

    override fun loadByEmail(email: String): User? {
        val userJpaEntity = userRepository.findByEmail(email)
        return userJpaEntity?.toDomain()
    }
}
