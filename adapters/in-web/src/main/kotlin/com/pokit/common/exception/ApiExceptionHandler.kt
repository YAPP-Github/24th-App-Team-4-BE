package com.pokit.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    private val notValidMessage = "잘못된 입력 값입니다."
    private val notValidCode = "G_001"

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidationException(e: MethodArgumentNotValidException): ErrorResponse {
        var message = notValidMessage
        val allErrors = e.bindingResult.allErrors
        if (allErrors.isNotEmpty()) {
            message = allErrors[0].defaultMessage!!
        }

        return ErrorResponse(message, notValidCode)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PokitException::class)
    fun handlePokitException(e: PokitException): ErrorResponse {
        return ErrorResponse(e.errorCode.message, e.errorCode.code)
    }
}
