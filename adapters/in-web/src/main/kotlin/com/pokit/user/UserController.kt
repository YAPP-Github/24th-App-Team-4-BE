package com.pokit.user

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.user.dto.request.*
import com.pokit.user.dto.request.toDto
import com.pokit.user.dto.response.CheckDuplicateNicknameResponse
import com.pokit.user.dto.response.InterestTypeResponse
import com.pokit.user.dto.response.UserResponse
import com.pokit.user.dto.response.toResponse
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.model.FcmToken
import com.pokit.user.model.InterestType
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

    @GetMapping("/interests")
    @Operation(summary = "관심사 목록 조회 API")
    fun getInterests(): ResponseEntity<List<InterestTypeResponse>> =
        InterestType.values()
            .map { InterestTypeResponse(it.name, it.kor) }
            .wrapOk()

    @PostMapping("/fcm")
    @Operation(summary = "fcm 토큰 저장 API")
    fun createFcmToken(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: ApiCreateFcmTokenRequest
    ): ResponseEntity<FcmToken> {
        return userUseCase.createFcmToken(user.id, request.toDto())
            .wrapOk()
    }

    @GetMapping("/nickname")
    @Operation(summary = "유저 정보(닉네임) 조회 API")
    fun getUserInfo(
        @AuthenticationPrincipal user: PrincipalUser
    ): ResponseEntity<UserResponse> {
        return userUseCase.getUserInfo(user.id)
            .toResponse()
            .wrapOk()
    }

    @PutMapping
    @Operation(summary = "유저 프로필 수정 API")
    fun update(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: UpdateProfileRequest
    ): ResponseEntity<UserResponse> {
        return userUseCase.updateProfile(user.id, request.toDto())
            .toResponse()
            .wrapOk()
    }
}
