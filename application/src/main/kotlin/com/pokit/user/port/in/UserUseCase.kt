package com.pokit.user.port.`in`

import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.dto.response.SignUpResponse
import com.pokit.user.model.User

interface UserUseCase {
    fun signUp(user: User, request: SignUpRequest): SignUpResponse
}
