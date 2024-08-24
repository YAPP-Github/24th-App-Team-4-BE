package com.pokit.out.persistence.content.persist

import com.querydsl.core.Tuple

interface DailyContentQuerydslRepository {
    fun getDailyContentsByUserId(userId: Long): List<Tuple>
    fun getContentIdsByUserId(userId: Long): List<Long>
}
