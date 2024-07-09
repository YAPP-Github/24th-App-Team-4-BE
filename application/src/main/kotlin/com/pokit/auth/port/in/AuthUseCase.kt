package com.pokit.auth.port.`in`

import com.pokit.token.dto.request.SignInRequest
import com.pokit.token.model.Token

interface AuthUseCase {
    fun signIn(request: SignInRequest): Token
}
