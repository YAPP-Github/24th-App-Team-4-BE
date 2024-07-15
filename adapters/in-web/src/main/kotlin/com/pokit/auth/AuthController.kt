package com.pokit.auth

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.port.`in`.AuthUseCase
import com.pokit.token.dto.request.SignInRequest
import com.pokit.user.exception.UserErrorCode
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authUseCase: AuthUseCase,
) {
    @PostMapping("/signin")
    @Operation(summary = "로그인 API")
    @ErrorOperation(UserErrorCode::class)
    fun signIn(
        @RequestBody request: SignInRequest,
    ) = ResponseEntity.ok(authUseCase.signIn(request))
}
