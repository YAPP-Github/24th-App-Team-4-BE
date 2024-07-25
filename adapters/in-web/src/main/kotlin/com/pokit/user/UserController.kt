package com.pokit.user

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.user.dto.request.ApiSignUpRequest
import com.pokit.user.dto.request.ApiUpdateNicknameRequest
import com.pokit.user.dto.request.toDto
import com.pokit.user.dto.response.CheckDuplicateNicknameResponse
import com.pokit.user.dto.response.UserResponse
import com.pokit.user.dto.response.toResponse
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
    ): ResponseEntity<UserResponse> {
        val user = principalUser.toDomain()
        return userUseCase.signUp(user, request.toDto())
            .toResponse()
            .wrapOk()
    }

    @GetMapping("/duplicate/{nickname}")
    @Operation(summary = "닉네임 중복 체크 API")
    @ErrorOperation(UserErrorCode::class)
    fun checkNickname(
        @PathVariable("nickname") nickname: String
    ): ResponseEntity<CheckDuplicateNicknameResponse> {
        return userUseCase.checkDuplicateNickname(nickname)
            .toResponse()
            .wrapOk()
    }

    @PutMapping("/nickname")
    @Operation(summary = "닉네임 수정 API")
    @ErrorOperation(UserErrorCode::class)
    fun updateNickname(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @Valid @RequestBody request: ApiUpdateNicknameRequest
    ): ResponseEntity<UserResponse> {
        val user = principalUser.toDomain()
        return userUseCase.updateNickname(user, request.toDto())
            .toResponse()
            .wrapOk()
    }
}
