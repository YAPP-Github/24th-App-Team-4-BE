package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.UserEntity
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
        val userEntity = UserEntity.of(user)
        val savedUser = userRepository.save(userEntity)
        return savedUser.toDomain()
    }

    override fun loadByEmail(email: String): User? {
        val userJpaEntity = userRepository.findByEmail(email)
        return userJpaEntity?.toDomain()
    }
}
