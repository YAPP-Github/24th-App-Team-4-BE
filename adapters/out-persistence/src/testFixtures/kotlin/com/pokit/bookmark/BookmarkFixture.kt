package com.pokit.bookmark

import com.pokit.bookmark.model.Bookmark

class BookmarkFixture {
    companion object {
        fun getBookmark(contentId: Long, userId: Long) = Bookmark(
            contentId = contentId,
            userId = userId
        )
    }
}
