package com.pokit.user

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.user.dto.ApiSignUpRequest
import com.pokit.user.dto.response.CheckDuplicateNicknameResponse
import com.pokit.user.dto.response.SignUpResponse
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.port.`in`.UserUseCase
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userUseCase: UserUseCase
) {
    @PostMapping("/signup")
    @Operation(summary = "회원 등록 API")
    @ErrorOperation(UserErrorCode::class)
    fun signUp(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @Valid @RequestBody request: ApiSignUpRequest
    ): ResponseEntity<SignUpResponse> {
        val user = principalUser.toDomain()
        val signUpRequest = request.toSignUpRequest()
        val response = userUseCase.signUp(user, signUpRequest)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/duplicate/{nickname}")
    @Operation(summary = "닉네임 중복 체크 API")
    @ErrorOperation(UserErrorCode::class)
    fun checkNickname(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("nickname") nickname: String
    ): ResponseEntity<CheckDuplicateNicknameResponse> {
        return ResponseEntity.ok(userUseCase.checkDuplicateNickname(nickname))
    }
}
