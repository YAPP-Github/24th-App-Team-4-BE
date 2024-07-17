package com.pokit.user.port.`in`

import com.pokit.user.dto.response.CheckDuplicateNicknameResponse

interface UserUseCase {
    fun checkDuplicateNickname(nickname: String): CheckDuplicateNicknameResponse
}
