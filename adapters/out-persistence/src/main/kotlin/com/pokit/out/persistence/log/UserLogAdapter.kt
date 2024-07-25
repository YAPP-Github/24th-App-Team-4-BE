package com.pokit.out.persistence.log

import com.pokit.log.model.UserLog
import com.pokit.log.port.out.UserLogPort
import com.pokit.out.persistence.log.persist.UserLogEntity
import com.pokit.out.persistence.log.persist.UserLogRepository
import com.pokit.out.persistence.log.persist.toDomain
import org.springframework.stereotype.Repository

@Repository
class UserLogAdapter(
    private val userLogRepository: UserLogRepository
) : UserLogPort {
    override fun loadAndpersist(userLog: UserLog): UserLog {
        val userLog = userLogRepository.findByContentIdAndUserId(userLog.contentId, userLog.userId)
            ?: userLogRepository.save(UserLogEntity.of(userLog))
        return userLog.toDomain()
    }
}
