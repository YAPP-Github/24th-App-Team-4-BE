package com.pokit.user.port.`in`

import com.pokit.user.dto.request.CreateFcmTokenRequest
import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.dto.request.UpdateNicknameRequest
import com.pokit.user.model.FcmToken
import com.pokit.user.model.User

interface UserUseCase {
    fun signUp(user: User, request: SignUpRequest): User

    fun checkDuplicateNickname(nickname: String): Boolean

    fun updateNickname(user: User, request: UpdateNicknameRequest): User

    fun fetchAllUserId(): List<Long>

    fun createFcmToken(userId: Long, request: CreateFcmTokenRequest): FcmToken
}
