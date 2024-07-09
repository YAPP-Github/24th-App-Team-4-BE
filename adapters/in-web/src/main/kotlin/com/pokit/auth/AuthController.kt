package com.pokit.auth

import com.pokit.auth.port.`in`.AuthUseCase
import com.pokit.token.dto.request.SignInRequest
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
    fun signIn(
        @RequestBody request: SignInRequest,
    ) = ResponseEntity.ok(authUseCase.signIn(request))
}
