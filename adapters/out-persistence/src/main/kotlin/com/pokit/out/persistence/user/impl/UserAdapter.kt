package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.UserEntity
import com.pokit.out.persistence.user.persist.UserRepository
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.token.model.AuthPlatform
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

    override fun loadBySubAndAuthPlatform(sub: String, authPlatform: AuthPlatform) =
        userRepository.findBySubAndAuthPlatformAndDeleted(sub, authPlatform, false)
            ?.run { toDomain() }

    override fun loadById(id: Long) = userRepository.findByIdAndDeleted(id, false)
        ?.run { toDomain() }

    override fun checkByNickname(nickname: String) = userRepository.existsByNickname(nickname)

    override fun checkByNickname(nickname: String, userId: Long) =
        userRepository.existsByNicknameAndIdNot(nickname, userId)

    override fun delete(user: User) {
        userRepository.findByIdOrNull(user.id)
            ?.delete()
    }

    override fun loadAllIds(): List<Long> =
        userRepository.findByDeleted(false)
            .map { it.id }

    override fun loadByEmailAndAuthPlatform(email: String, authPlatform: AuthPlatform) =
        userRepository.findByEmailAndAuthPlatformAndDeleted(email, authPlatform, false)
            ?.run { toDomain() }

    override fun loadAllInIds(userIds: List<Long>): List<User> {
        return userRepository.findAllByIdIn(userIds)
            .map { it.toDomain() }
    }
}
