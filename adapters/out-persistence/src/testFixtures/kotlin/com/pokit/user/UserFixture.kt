package com.pokit.user

import com.pokit.token.model.AuthPlatform
import com.pokit.user.dto.UserInfo
import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.model.InterestType
import com.pokit.user.model.Role
import com.pokit.user.model.User

class UserFixture {
    companion object {
        fun getUser() = User(1L, "ij@naver.com", Role.USER, authPlatform = AuthPlatform.GOOGLE, sub = "asdasd")

        fun getUserInfo() = UserInfo("ig@naver.com", AuthPlatform.GOOGLE, sub = "asdasd")

        fun getInvalidUser() = User(2L, "dls@naver.com", Role.USER, authPlatform = AuthPlatform.GOOGLE, sub = "sub")

        fun getSignUpRequest() = SignUpRequest("인주니", listOf(InterestType.SPORTS))
    }
}
