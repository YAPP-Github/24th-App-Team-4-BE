package com.pokit.user.port.service

import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryStatus.UNCATEGORIZED
import com.pokit.category.model.OpenType
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.user.dto.request.CreateFcmTokenRequest
import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.dto.request.UpdateNicknameRequest
import com.pokit.user.dto.request.UserCommand
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.model.FcmToken
import com.pokit.user.model.Interest
import com.pokit.user.model.User
import com.pokit.user.port.`in`.UserUseCase
import com.pokit.user.port.out.FcmTokenPort
import com.pokit.user.port.out.InterestPort
import com.pokit.user.port.out.UserImagePort
import com.pokit.user.port.out.UserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userPort: UserPort,
    private val categoryPort: CategoryPort,
    private val categoryImagePort: CategoryImagePort,
    private val fcmTokenPort: FcmTokenPort,
    private val userImagePort: UserImagePort,
    private val interestPort: InterestPort,
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

        request.interests.forEach {
            interestPort.persist(
                Interest(userId = user.id, interestType = it)
            )
        }

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

    override fun fetchAllUserId() =
        userPort.loadAllIds()


    @Transactional
    override fun createFcmToken(userId: Long, request: CreateFcmTokenRequest): FcmToken {
        val user = userPort.loadById(userId)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)
        val fcmToken = FcmToken(user.id, request.token)
        return fcmTokenPort.persist(fcmToken)
    }

    override fun getUserInfo(userId: Long): User {
        return userPort.loadById(userId)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)
    }

    @Transactional
    override fun updateProfile(userId: Long, command: UserCommand): User {
        val user = userPort.loadById(userId)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)

        val image = userImagePort.loadById(command.profileImageId)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_PROFILE_IMAGE)

        val isDuplicate = userPort.checkByNickname(command.nickname, userId)
        if (isDuplicate) {
            throw ClientValidationException(UserErrorCode.ALREADY_EXISTS_NICKNAME)
        }

        user.modifyProfile(image, command.nickname)
        return userPort.persist(user)
    }
}
