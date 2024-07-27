package com.pokit.common.exception

open class PokitException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)

class ClientValidationException(errorCode: ErrorCode) : PokitException(errorCode)

class InvalidRequestException(errorCode: ErrorCode) : PokitException(errorCode)

class NotFoundCustomException(errorCode: ErrorCode) : PokitException(errorCode)

class AlreadyExistsException(errorCode: ErrorCode) : PokitException(errorCode)

class ExternalApiException(errorCode: ErrorCode) : PokitException(errorCode)
