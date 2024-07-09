package com.pokit.user.port.out

import com.pokit.user.model.User

interface UserRepository {
    fun save(user: User): User

    fun findByEmail(email: String): User?
}
