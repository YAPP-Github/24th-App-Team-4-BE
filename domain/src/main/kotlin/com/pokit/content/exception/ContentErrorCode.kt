package com.pokit.content.exception

import com.pokit.common.exception.ErrorCode

enum class ContentErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode {
    NOT_FOUND_CONTENT("존재하지 않는 컨텐츠입니다.", "C_001")
}
