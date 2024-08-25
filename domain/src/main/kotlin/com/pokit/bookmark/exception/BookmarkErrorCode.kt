package com.pokit.bookmark.exception

import com.pokit.common.exception.ErrorCode

enum class BookmarkErrorCode(
    override val message: String,
    override val code: String,
) : ErrorCode {
    ALREADY_EXISTS_BOOKMARK("이미 즐겨찾기한 컨텐츠입니다.", "B_001"),
}
