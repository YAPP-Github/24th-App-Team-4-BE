package com.pokit.user.port.service

import com.pokit.common.exception.NotFoundCustomException
import com.pokit.user.UserFixture
import com.pokit.user.model.User
import com.pokit.user.port.out.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserServiceTest : BehaviorSpec({
    val userPort = mockk<UserPort>()
    val userService = UserService(userPort)
    Given("회원을 등록할 때") {
        val user = UserFixture.getUser()
        val invalidUser = UserFixture.getInvalidUser()
        val request = UserFixture.getSignUpRequest()
        val modifieUser = User(user.id, user.email, user.role, request.nickName)

        every { userPort.register(user) } returns modifieUser
        every { userPort.register(invalidUser) } returns null

        When("수정하려는 정보를 받으면") {
            val response = userService.signUp(user, request)
            Then("회원 정보가 수정되고 수정된 회원의 id가 반환된다.") {
                response.userId shouldBe modifieUser.id
                user.nickName shouldBe modifieUser.nickName
            }
        }

        When("수정하려는 회원을 찾을 수 없으면") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    userService.signUp(invalidUser, request)
                }
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
                response.isDuplicate shouldBe false
            }
        }

        When("해당 닉네임의 유저가 존재하면 ") {
            val response = userService.checkDuplicateNickname(duplicateName)
            Then("true가 반환된다.") {
                response.isDuplicate shouldBe true
            }
        }
    }
})
