package com.pokit.alert.exception

import com.pokit.common.exception.ErrorCode

enum class AlertErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode {
    NOT_FOUND_ALERT("존재하지 않는 알림입니다.", "AL_001")
}
