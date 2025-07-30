package com.pokit.user.persist

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UserCacheRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    fun save(userId: Long, user: String) {
        val key = buildKey(userId)
        redisTemplate.opsForValue().set(key, user, Duration.ofHours(1)) // TTL 1시간
    }

    fun findById(userId: Long): String? {
        val key = buildKey(userId)
        val user = redisTemplate.opsForValue().get(key) as? String
        return user
    }

    fun deleteById(userId: Long) {
        val key = buildKey(userId)
        redisTemplate.delete(key)
    }

    private fun buildKey(userId: Long): String = "user:$userId"
}
