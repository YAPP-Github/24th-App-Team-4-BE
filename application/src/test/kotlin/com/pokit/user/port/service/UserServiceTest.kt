package com.pokit.user.port.service

import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.user.UserFixture
import com.pokit.user.dto.request.UpdateNicknameRequest
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
                response.id shouldBe modifieUser.id
                response.nickName shouldBe modifieUser.nickName
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
})
