package com.pokit.common.exception

class ClientValidationException(
    errorCode: ErrorCode,
) : RuntimeException(errorCode.message) {
    val code = errorCode.code
}
