package com.pokit.bookmark.port.out

import com.pokit.bookmark.model.Bookmark

interface BookmarkPort {
    fun persist(bookmark: Bookmark): Bookmark
}