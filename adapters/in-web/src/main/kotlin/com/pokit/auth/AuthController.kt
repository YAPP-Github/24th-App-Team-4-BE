package com.pokit.auth

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.dto.request.ApiRevokeRequest
import com.pokit.auth.dto.request.ReIssueRequest
import com.pokit.auth.dto.request.toDto
import com.pokit.auth.dto.response.ReIssueResponse
import com.pokit.auth.dto.response.toReIssueResponse
import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.auth.port.`in`.AuthUseCase
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapUnit
import com.pokit.token.dto.request.SignInRequest
import com.pokit.user.exception.UserErrorCode
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/withdraw")
    @Operation(summary = "회원 탈퇴 API")
    fun withDraw(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: ApiRevokeRequest
    ): ResponseEntity<Unit> {
        return authUseCase.withDraw(user.toDomain(), request.toDto())
            .wrapUnit()
    }

    @PostMapping("/reissue")
    @Operation(summary = "액세스 토큰 재발급 API")
    @ErrorOperation(UserErrorCode::class)
    fun reissue(
        @RequestBody request: ReIssueRequest
    ): ResponseEntity<ReIssueResponse> {
        return authUseCase.reissue(request.refreshToken)
            .toReIssueResponse()
            .wrapOk()
    }
}
