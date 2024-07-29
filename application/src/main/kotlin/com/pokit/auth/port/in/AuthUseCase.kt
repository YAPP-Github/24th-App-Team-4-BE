package com.pokit.auth.port.`in`

import com.pokit.token.dto.request.RevokeRequest
import com.pokit.token.dto.request.SignInRequest
import com.pokit.token.model.Token
import com.pokit.user.model.User

interface AuthUseCase {
    fun signIn(request: SignInRequest): Token

    fun withDraw(user: User, request: RevokeRequest)
}
