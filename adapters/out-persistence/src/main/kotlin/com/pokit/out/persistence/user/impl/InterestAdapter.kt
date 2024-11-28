package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.InterestEntity
import com.pokit.out.persistence.user.persist.InterestRepository
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.user.model.Interest
import com.pokit.user.port.out.InterestPort
import org.springframework.stereotype.Repository

@Repository
class InterestAdapter (
    private val interestRepository: InterestRepository
) : InterestPort {
    override fun persist(interest: Interest): Interest {
        return interestRepository.save(InterestEntity.of(interest))
            .toDomain()
    }

    override fun delete(interest: Interest) {
        interestRepository.deleteById(interest.id)
    }

    override fun loadByUserId(userId: Long): List<Interest> {
        return interestRepository.findAllByUserId(userId)
            .map { it.toDomain() }
    }
}
