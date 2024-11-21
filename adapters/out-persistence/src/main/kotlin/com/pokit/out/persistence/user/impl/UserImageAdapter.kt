package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.UserImageRepository
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.user.model.UserImage
import com.pokit.user.port.out.UserImagePort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserImageAdapter(
    private val userImageRepository: UserImageRepository
) : UserImagePort {
    override fun loadById(id: Int): UserImage? {
        return userImageRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    override fun loadAll(): List<UserImage> {
        return userImageRepository.findAll()
            .map { it.toDomain() }
    }
}
