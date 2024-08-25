package com.pokit.auth.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor


@Component
class RequestLoggingInterceptor : HandlerInterceptor {
    private val logger = KotlinLogging.logger { }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val uri = request.requestURI
        val params = request.parameterMap.map { (key, value) -> "$key=${value.joinToString(",")}" }.joinToString("&")
        logger.info{"Requested URI: $uri"}
        if (params.isNotEmpty()) {
            logger.info{"Request Parameters: $params"}
        }
        return true
    }
}
