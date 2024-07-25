package com.pokit.log.port.out

import com.pokit.log.model.UserLog

interface UserLogPort {
    fun loadAndpersist(userLog: UserLog): UserLog
}
