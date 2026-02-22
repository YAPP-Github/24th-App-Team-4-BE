package com.pokit.notification.exception

import com.pokit.common.exception.ErrorCode

enum class NotificationErrorCode(
    override val message: String,
    override val code: String
) : ErrorCode {
    NOT_FOUND_NOTIFICATION("존재하지 않는 알림입니다.", "NF_001")
}
