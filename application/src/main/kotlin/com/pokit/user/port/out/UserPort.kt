package com.pokit.user.port.out

import com.pokit.token.model.AuthPlatform
import com.pokit.user.model.User

interface UserPort {
    fun persist(user: User): User

    fun loadByEmailAndAuthPlatform(email: String, authPlatform: AuthPlatform): User?

    fun loadById(id: Long): User?

    fun checkByNickname(nickname: String): Boolean

    fun delete(user: User)

    fun loadAllIds(): List<Long>
}
