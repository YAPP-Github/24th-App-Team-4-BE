package com.pokit.auth

import com.pokit.token.dto.request.SignInRequest
import com.pokit.token.model.AuthPlatform
import com.pokit.token.model.Token

class AuthFixture {
    companion object {
        fun getToken() = Token(accessToken = "at", refreshToken = "rf")

        fun getGoogleSigniInRequest() = SignInRequest(AuthPlatform.GOOGLE.platform, "code")

        fun getInvalidSignInRequest() = SignInRequest("구긍", "code")
    }
}
