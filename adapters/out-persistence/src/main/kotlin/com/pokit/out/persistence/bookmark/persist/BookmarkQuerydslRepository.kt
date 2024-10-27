package com.pokit.out.persistence.bookmark.persist

interface BookmarkQuerydslRepository {
    fun countByUserId(userId: Long): Int
}
