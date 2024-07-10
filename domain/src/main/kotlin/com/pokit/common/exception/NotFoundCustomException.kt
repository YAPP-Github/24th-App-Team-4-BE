package com.pokit.common.exception

class NotFoundCustomException(
    errorCode: ErrorCode,
) : RuntimeException(errorCode.message) {
    val code = errorCode.code
}
