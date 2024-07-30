package com.pokit.log.port.out

import com.pokit.log.model.LogType
import com.pokit.log.model.UserLog

interface UserLogPort {
    fun persist(userLog: UserLog): UserLog

    fun loadByUserIdAndType(userId: Long, type: LogType): List<UserLog>
}
