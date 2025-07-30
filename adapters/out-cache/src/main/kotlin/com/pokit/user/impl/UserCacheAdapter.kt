package com.pokit.user.impl

import com.google.gson.Gson
import com.pokit.user.model.User
import com.pokit.user.persist.UserCacheRepository
import com.pokit.user.port.out.UserCachePort
import org.springframework.stereotype.Repository

@Repository
class UserCacheAdapter(
    private val userCacheRepository: UserCacheRepository,
) : UserCachePort {
    override fun persist(user: User) {
        val jsonUser = Gson().toJson(user)
        return userCacheRepository.save(user.id, jsonUser)
    }

    override fun loadById(userId: Long): User? {
        val findUser = userCacheRepository.findById(userId)
        val user = findUser?.let { Gson().fromJson(it, User::class.java) }
        return user
    }

    override fun deleteById(userId: Long) {
        userCacheRepository.deleteById(userId)
    }
}
