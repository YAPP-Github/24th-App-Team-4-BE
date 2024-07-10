package com.pokit.user.port.out

import com.pokit.user.model.User

interface UserPort {
    fun persist(user: User): User

    fun loadByEmail(email: String): User?
}
