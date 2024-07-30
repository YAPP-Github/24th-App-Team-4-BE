package com.pokit.log.port.out

import com.pokit.log.model.UserLog

interface UserLogPort {
    fun persist(userLog: UserLog): UserLog

    fun isContentRead(contentId: Long, userId: Long): Boolean
}
