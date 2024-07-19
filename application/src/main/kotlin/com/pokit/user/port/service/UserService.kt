package com.pokit.user.port.service

import com.pokit.common.exception.NotFoundCustomException
import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.dto.response.SignUpResponse
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.model.User
import com.pokit.user.port.`in`.UserUseCase
import com.pokit.user.port.out.UserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userPort: UserPort
) : UserUseCase {
    @Transactional
    override fun signUp(user: User, request: SignUpRequest): SignUpResponse {
        user.modifyUser(
            request.nickName,
        )

        val savedUser = userPort.register(user)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)

        return SignUpResponse(savedUser.id)
    }
}
