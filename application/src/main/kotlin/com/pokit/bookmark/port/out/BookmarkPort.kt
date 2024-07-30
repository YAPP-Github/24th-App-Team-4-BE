package com.pokit.bookmark.port.out

import com.pokit.bookmark.model.Bookmark

interface BookmarkPort {
    fun persist(bookmark: Bookmark): Bookmark

    fun delete(userId: Long, contentId: Long)

    fun loadByContentIdAndUserId(contentId: Long, userId: Long): Bookmark?

    fun loadByUserId(userId: Long): List<Bookmark>
}
