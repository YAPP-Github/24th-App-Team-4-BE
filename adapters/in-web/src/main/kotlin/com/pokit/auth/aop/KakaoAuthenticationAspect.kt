package com.pokit.auth.aop

import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.InvalidRequestException
import com.pokit.token.exception.AuthErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Aspect
@Component
class KakaoAuthenticationAspect(
    private val request: HttpServletRequest
) {
    @Value("\${auth.kakao.app-admin-key}")
    lateinit var adminKey: String

    @Before("@annotation(com.pokit.auth.aop.KakaoAuth)")
    fun checkCallbackHeader() {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorizationHeader.isNullOrBlank()) {
            throw ClientValidationException(AuthErrorCode.TOKEN_REQUIRED)
        }

        val (type, token) = authorizationHeader.split(" ", limit = 2)
        if (type != "KakaoAK" || adminKey != token) {
            throw InvalidRequestException(AuthErrorCode.INVALID_TOKEN)
        }
    }
}
