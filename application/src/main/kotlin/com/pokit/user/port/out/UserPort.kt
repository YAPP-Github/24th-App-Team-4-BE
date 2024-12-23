package com.pokit.user.port.out

import com.pokit.token.model.AuthPlatform
import com.pokit.user.model.User

interface UserPort {
    fun persist(user: User): User

    fun loadBySubAndAuthPlatform(sub: String, authPlatform: AuthPlatform): User?

    fun loadById(id: Long): User?

    fun checkByNickname(nickname: String): Boolean

    fun checkByNickname(nickname: String, userId: Long): Boolean

    fun delete(user: User)

    fun loadAllIds(): List<Long>

    fun loadByEmailAndAuthPlatform(email: String, authPlatform: AuthPlatform): User?

    fun loadAllInIds(userIds: List<Long>): List<User>
}
