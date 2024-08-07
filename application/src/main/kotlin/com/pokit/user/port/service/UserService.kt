package com.pokit.user.port.service

import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.dto.request.UpdateNicknameRequest
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
    override fun signUp(user: User, request: SignUpRequest): User {
        val findUser = userPort.loadById(user.id)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)

        if(findUser.registered) {
            throw ClientValidationException(UserErrorCode.ALREADY_REGISTERED)
        }

        findUser.modifyUser(request.nickName,)
        val savedUser = userPort.persist(findUser)

        return savedUser
    }

    override fun checkDuplicateNickname(nickname: String): Boolean {
        return userPort.checkByNickname(nickname)
    }

    @Transactional
    override fun updateNickname(user: User, request: UpdateNicknameRequest): User {
        val findUser = (userPort.loadById(user.id)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER))

        val isDuplicate = userPort.checkByNickname(request.nickname)
        if (isDuplicate) {
            throw ClientValidationException(UserErrorCode.ALREADY_EXISTS_NICKNAME)
        }

        findUser.modifyUser(request.nickname)
        return userPort.persist(findUser)
    }
}
