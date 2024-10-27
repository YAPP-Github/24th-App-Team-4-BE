package com.pokit.content.port.out

interface ContentCountPort {
    fun getUnreadCount(userId: Long): Int

    fun getBookmarkCount(userId: Long): Int
}
