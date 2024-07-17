package com.pokit.user

import com.pokit.auth.model.PrincipalUser
import com.pokit.user.dto.response.CheckDuplicateNicknameResponse
import com.pokit.user.port.`in`.UserUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userUseCase: UserUseCase
) {
    @GetMapping("/duplicate/{nickname}")
    fun checkNickname(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("nickname") nickname: String
    ): ResponseEntity<CheckDuplicateNicknameResponse> {
        return ResponseEntity.ok(userUseCase.checkDuplicateNickname(nickname))
    }
}
