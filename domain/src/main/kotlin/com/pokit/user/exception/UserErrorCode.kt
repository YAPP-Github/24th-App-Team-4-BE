package com.pokit.user.exception

import com.pokit.common.exception.ErrorCode

enum class UserErrorCode(
    override val message: String,
    override val code: String,
) : ErrorCode {
    INVALID_EMAIL("올바르지 않은 이메일 형식의 유저입니다.", "U_001"),
    INVALID_INTEREST_TYPE("관심사가 잘못되었습니다.", "U_002"),
    NOT_FOUND_USER("존재하지 않는 회원입니다.", "U_003"),
    ALREADY_EXISTS_NICKNAME("이미 존재하는 닉네임입니다.", "U_004"),
    ALREADY_REGISTERED("이미 회원가입 한 유저입니다.", "U_005"),
    NOT_FOUND_PROFILE_IMAGE("존재하지 않는 프로필 이미지입니다.", "U_006"),
}
