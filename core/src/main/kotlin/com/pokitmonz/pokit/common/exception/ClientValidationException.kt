package com.pokitmonz.pokit.common.exception

class ClientValidationException(
    errorCode: ErrorCode,
) : RuntimeException(errorCode.getMessage()) {
    val code = errorCode.getCode()
}
