package com.pokit.content.port.out

interface ContentCountPort {
    fun getUnreadCount(userId: Long): Int

    fun getBookmarkContent(userId: Long): Int
}
