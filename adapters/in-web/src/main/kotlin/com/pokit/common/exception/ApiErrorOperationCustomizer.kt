package com.pokit.common.exception

import com.pokit.auth.config.ErrorOperation
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod

@Component
class ApiErrorOperationCustomizer : OperationCustomizer {
    override fun customize(operation: Operation, handlerMethod: HandlerMethod): Operation {
        val annotation = handlerMethod.method.getAnnotation(ErrorOperation::class.java)
        if (annotation != null) {
            val errorCodeClass = annotation.value.java
            val errorCodes = errorCodeClass.enumConstants
            val apiResponses = ApiResponses()

            for (errorCode in errorCodes) {
                val exampleContent = ErrorResponse(errorCode.message, errorCode.code)

                val example = Example().apply {
                    value = exampleContent
                }

                val mediaType = MediaType().apply {
                    addExamples("example", example)
                    schema = Schema<ErrorResponse>()
                }

                val content = Content().apply {
                    addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType)
                }

                val response = ApiResponse()
                    .description("${errorCode.code}: ${errorCode.message}")
                    .content(content)
                apiResponses.addApiResponse(errorCode.code, response)
            }

            operation.responses(apiResponses)
        }
        return operation
    }
}
