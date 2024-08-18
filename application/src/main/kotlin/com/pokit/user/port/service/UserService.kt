package com.pokit.user.port.service

import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryStatus.UNCATEGORIZED
import com.pokit.category.model.OpenType
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.category.port.out.CategoryPort
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
    private val userPort: UserPort,
    private val categoryPort: CategoryPort,
    private val categoryImagePort: CategoryImagePort
) : UserUseCase {
    companion object {
        private const val UNCATEGORIZED_IMAGE_ID = 1
    }

    @Transactional
    override fun signUp(user: User, request: SignUpRequest): User {
        val findUser = userPort.loadById(user.id)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)

        if (findUser.registered) {
            throw ClientValidationException(UserErrorCode.ALREADY_REGISTERED)
        }

        findUser.register(request.nickName)
        val savedUser = userPort.persist(findUser)

        val image = (categoryImagePort.loadById(UNCATEGORIZED_IMAGE_ID)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_UNCATEGORIZED_IMAGE))

        val category = Category(
            userId = savedUser.id,
            categoryName = UNCATEGORIZED.displayName,
            categoryImage = image,
            openType = OpenType.PRIVATE,
        )
        categoryPort.persist(category)

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

        findUser.modifyNickname(request.nickname)
        return userPort.persist(findUser)
    }
}
