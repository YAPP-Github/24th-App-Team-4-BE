package com.pokit.user.exception

import com.pokit.common.exception.ErrorCode

enum class UserErrorCode(
    override val message: String,
    override val code: String,
) : ErrorCode {
    INVALID_EMAIL("올바르지 않은 이메일 형식의 유저입니다.", "U_001"),
    NOT_FOUND_USER("회원을 찾을 수 없습니다.", "U_002"),
}
