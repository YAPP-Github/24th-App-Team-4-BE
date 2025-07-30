package com.pokit.user.port.out

import com.pokit.user.model.User

interface UserCachePort {
    fun persist(user: User)

    fun loadById(userId: Long): User?

    fun deleteById(userId: Long)
}
