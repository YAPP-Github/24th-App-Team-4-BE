package com.pokitmonz.pokit.common.exception

class NotFoundCustomException(
    errorCode: ErrorCode,
) : RuntimeException(errorCode.getMessage()) {
    val code = errorCode.getCode()
}
