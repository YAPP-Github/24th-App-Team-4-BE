package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.UserEntity
import com.pokit.out.persistence.user.persist.UserRepository
import com.pokit.out.persistence.user.persist.registerInfo
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.user.model.User
import com.pokit.user.port.out.UserPort
import org.springframework.data.repository.findByIdOrNull
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

    override fun loadByEmail(email: String) = userRepository.findByEmail(email)
        ?.run { toDomain() }

    override fun loadById(id: Long) = userRepository.findByIdOrNull(id)
        ?.run { toDomain() }

    override fun register(user: User): User? {
        val userEntity = userRepository.findByIdOrNull(user.id)
        userEntity?.registerInfo(user)
        return userEntity?.toDomain()
    }

    override fun checkByNickname(nickname: String) = userRepository.existsByNickname(nickname)
}
