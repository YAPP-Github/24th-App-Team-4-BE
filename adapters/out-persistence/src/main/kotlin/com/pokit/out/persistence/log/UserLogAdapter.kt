package com.pokit.out.persistence.log

import com.pokit.log.model.LogType
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
    override fun persist(userLog: UserLog) =
        userLogRepository.save(UserLogEntity.of(userLog)).toDomain()

    override fun isContentRead(contentId: Long, userId: Long): Boolean =
        userLogRepository.existsByContentIdAndUserIdAndType(contentId, userId, LogType.READ)

}
