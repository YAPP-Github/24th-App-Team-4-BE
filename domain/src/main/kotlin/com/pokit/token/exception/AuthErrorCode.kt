package com.pokit.token.exception

import com.pokit.common.exception.ErrorCode

enum class AuthErrorCode(
    override val message: String,
    override val code: String,
) : ErrorCode {
    TOKEN_EXPIRED("토큰이 만료되었습니다.", "A_000"),
    NOT_FOUND_TOKEN("존재하지 않는 토큰입니다.", "A_001"),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", "A_003"),
    UNSUPPORTED_TOKEN("지원하지 않는 형식의 토큰입니다.", "A_004"),
    WRONG_SIGNATURE("JWT 서명이 서버에 산정된 서명과 일치하지 않습니다.", "A_005"),
    TOKEN_REQUIRED("토큰이 비어있습니다.", "A_006"),
    INVALID_PLATFORM("플랫폼 타입이 올바르지 않습니다.", "A_007"),
    INVALID_ID_TOKEN("ID TOKEN 값이 올바르지 않습니다.", "A_008"),
}
