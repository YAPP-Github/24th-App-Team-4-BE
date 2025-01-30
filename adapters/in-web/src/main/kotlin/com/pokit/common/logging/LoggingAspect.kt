package com.pokit.common.logging

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.pokit.auth.model.PrincipalUser
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import kotlin.system.measureTimeMillis


@Aspect
@Component
class LoggingAspect(
    private val request: HttpServletRequest,
    private val objectMapper: ObjectMapper,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Pointcut("within(com.pokit..*) && within(@org.springframework.web.bind.annotation.RestController *)")
    fun controllerPointcut() {
    }

    @Around("controllerPointcut()")
    fun logApi(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val args = joinPoint.args
        val userId = getUserId(args)
        val httpMethod = request.method
        val requestUri = request.requestURI
        val operationSummary = getOperationSummary(methodSignature.method)
        val queryString = request.queryString

        val requestBody = getRequestBody(args)
        log.info(
            "\n----Request Log----\n" +
                    "API: {}\n" +
                    "Method: {}\n" +
                    "API Path: {}\n" +
                    "User Id : {}\n" +
                    "Query String: {}\n" +
                    "Request Body: \n{}\n" +
                    "---------------",
            operationSummary, httpMethod, requestUri, userId, queryString, requestBody
        )

        var response: Any?
        val executionTime = measureTimeMillis {
            response = joinPoint.proceed()
        }

        val responseBody = getResponseBody(response)
        log.info(
            "\n----Response Log----\n" +
                    "API: {}\n" +
                    "Response Body: \n{}\n" +
                    "Execution Time: ${executionTime}ms" +
                    "---------------",
            operationSummary, responseBody
        )

        return response
    }

    private fun getUserId(args: Array<Any>): Long? {
        for (arg in args) {
            if (arg is PrincipalUser) {
                return arg.id
            }
        }
        return null
    }

    private fun getRequestBody(args: Array<Any>): String {
        for (arg in args) {
            if (arg !is HttpServletRequest && arg !is HttpServletResponse && isDto(arg)
            ) {
                try {
                    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arg)
                } catch (e: JsonProcessingException) {
                    log.error("Failed to serialize request body: {}", e.message)
                    return "Failed to serialize request body"
                }
            }
        }
        return "No Request Body"
    }

    private fun getResponseBody(response: Any?): String {
        return try {
            objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(response)  // Pretty Print
        } catch (e: Exception) {
            log.error("Failed to serialize response body: {}", e.message)
            "Failed to serialize response body"
        }
    }

    private fun getOperationSummary(method: Method): String {
        val operation = method.getAnnotation(Operation::class.java)
        return operation?.summary ?: "Summary 없음"
    }

    private fun isDto(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        val packageName = obj::class.qualifiedName ?: return false
        return packageName.startsWith("com.pokit") && packageName.contains(".dto")
    }
}
