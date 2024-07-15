package com.pokit.user

import com.pokit.auth.config.ErrorOperation
import com.pokit.user.dto.ApiSignUpRequest
import com.pokit.user.dto.response.SignUpResponse
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.model.User
import com.pokit.user.port.`in`.UserUseCase
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userUseCase: UserUseCase
) {
    @PostMapping("/signup")
    @Operation(summary = "회원 등록 API")
    @ErrorOperation(UserErrorCode::class)
    fun signUp(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: ApiSignUpRequest
    ): ResponseEntity<SignUpResponse> {
        val signUpRequest = request.toSignUpRequest()
        val response = userUseCase.signUp(user, signUpRequest)
        return ResponseEntity.ok(response)
    }
}
