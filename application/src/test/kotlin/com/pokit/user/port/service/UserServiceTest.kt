package com.pokit.user.port.service

import com.pokit.category.CategoryFixture
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.token.model.AuthPlatform
import com.pokit.user.UserFixture
import com.pokit.user.dto.request.UpdateNicknameRequest
import com.pokit.user.dto.request.UserCommand
import com.pokit.user.model.User
import com.pokit.user.port.out.FcmTokenPort
import com.pokit.user.port.out.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserServiceTest : BehaviorSpec({
    val userPort = mockk<UserPort>()
    val categoryPort = mockk<CategoryPort>()
    val categoryImagePort = mockk<CategoryImagePort>()
    val fcmTokenPort = mockk<FcmTokenPort>()
    val userService = UserService(userPort, categoryPort, categoryImagePort, fcmTokenPort)
    Given("회원을 등록할 때") {
        val user = UserFixture.getUser()
        val invalidUser = UserFixture.getInvalidUser()
        val request = UserFixture.getSignUpRequest()
        val modifieUser = User(user.id, user.email, user.role, request.nickName, AuthPlatform.GOOGLE, sub = "sub")
        val image = CategoryImage(1, "https://www.image.com")
        val unCategorized = CategoryFixture.getUnCategorized(user.id, image)

        every { userPort.loadById(user.id) } returns user
        every { userPort.persist(any(User::class)) } returns modifieUser
        every { categoryImagePort.loadById(1) } returns image
        every { categoryPort.persist(unCategorized) } returns unCategorized
        every { categoryPort.persist(any(Category::class)) } returns unCategorized

        When("수정하려는 정보를 받으면") {
            val response = userService.signUp(user, request)
            Then("회원 정보가 수정되고 수정된 회원의 id가 반환된다.") {
                response.id shouldBe modifieUser.id
                response.nickName shouldBe modifieUser.nickName
            }
        }
    }

    Given("닉네임 중복 체크 시") {
        val notDuplicateName = "injun"
        val duplicateName = "jimin"

        every { userPort.checkByNickname(notDuplicateName) } returns false
        every { userPort.checkByNickname(duplicateName) } returns true

        When("해당 닉네임의 유저가 존재하지 않으면") {
            val response = userService.checkDuplicateNickname(notDuplicateName)
            Then("false가 반환된다") {
                response shouldBe false
            }
        }

        When("해당 닉네임의 유저가 존재하면 ") {
            val response = userService.checkDuplicateNickname(duplicateName)
            Then("true가 반환된다.") {
                response shouldBe true
            }
        }
    }

    Given("닉네임을 수정할 때") {
        val user = UserFixture.getUser()
        val request = UpdateNicknameRequest(
            nickname = "수정하려는 닉네임"
        )
        val invalidRequest = UpdateNicknameRequest(
            nickname = "이미 사용중인 닉네임"
        )

        every { userPort.loadById(user.id) } returns user
        every { userPort.checkByNickname(request.nickname) } returns false
        every { userPort.checkByNickname(invalidRequest.nickname) } returns true
        every { userPort.persist(any(User::class)) } returns user

        When("사용 가능한 닉네임으로 수정하려 하면 ") {
            val userResponse = userService.updateNickname(user, request)
            Then("수정된 유저 도메인을 반환한다.") {
                userResponse.nickName shouldBe request.nickname
            }
        }
        When("이미 사용중인 닉네임으로 수정하려 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<ClientValidationException> {
                    userService.updateNickname(user, invalidRequest)
                }
            }
        }
    }

    Given("프로필 변경 시") {
        val notExistUserId = 0L
        val notExistImageId = 0
        val existNickname = "Duplicate"

        val user = UserFixture.getUser()
        val toModifyName = "Modify"

        val imageId = 1
        val image = CategoryImage(imageId, "url")

        val command = UserCommand(imageId, toModifyName)
        val invalidCommand = UserCommand(notExistImageId, toModifyName)
        val duplicateCommand = UserCommand(imageId, existNickname)

        every { userPort.loadById(notExistUserId) } returns null
        every { categoryImagePort.loadById(notExistImageId) } returns null
        every { userPort.checkByNickname(existNickname) } returns true
        every { userPort.loadById(user.id) } returns user
        every { categoryImagePort.loadById(imageId) } returns image
        every { userPort.checkByNickname(toModifyName) } returns false
        every { userPort.persist(user) } returns user

        When("존재하지 않는 유저하면") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    userService.updateProfile(notExistUserId, command)
                }
            }
        }
        When("존재하지 않는 이미지로 요청 시 ") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    userService.updateProfile(user.id, invalidCommand)
                }
            }
        }
        When("이미 존재하는 닉네임으로 변경할 시") {
            Then("예외가 발생한다.") {
                shouldThrow<ClientValidationException> {
                    userService.updateProfile(user.id, duplicateCommand)
                }
            }
        }
        When("유효한 닉네임, 프로필로 요청 시") {
            val response = userService.updateProfile(user.id, command)
            Then("프로필 변경에 성공한다.") {
                response.nickName shouldBe toModifyName
                response.profileImage shouldBe image.imageUrl
            }
        }
    }
})
