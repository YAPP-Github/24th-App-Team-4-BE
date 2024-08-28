package com.pokit.out.persistence.user.impl

import com.pokit.out.persistence.user.persist.FcmTokenEntity
import com.pokit.out.persistence.user.persist.FcmTokenRepository
import com.pokit.out.persistence.user.persist.toDomain
import com.pokit.user.model.FcmToken
import com.pokit.user.port.out.FcmTokenPort
import org.springframework.stereotype.Repository

@Repository
class FcmTokenAdapter(
    private val fcmTokenRepository: FcmTokenRepository
) : FcmTokenPort {
    override fun persist(fcmToken: FcmToken): FcmToken {
        val fcmTokenEntity = FcmTokenEntity.of(fcmToken)
        return fcmTokenRepository.save(fcmTokenEntity).toDomain()
    }

    override fun loadByUserId(userId: Long): List<FcmToken> {
        return fcmTokenRepository.findByUserId(userId)
            .map { it.toDomain() }
    }
}
