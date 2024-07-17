package com.pokit.user.port.service

import com.pokit.user.dto.response.CheckDuplicateNicknameResponse
import com.pokit.user.port.`in`.UserUseCase
import com.pokit.user.port.out.UserPort
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userPort: UserPort
) : UserUseCase {
    override fun checkDuplicateNickname(nickname: String)
        : CheckDuplicateNicknameResponse {
        val isDuplicate = userPort.checkByNickname(nickname)
        return CheckDuplicateNicknameResponse(isDuplicate)
    }
}
