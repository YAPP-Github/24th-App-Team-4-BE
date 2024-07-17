package com.pokit.user.port.service

import com.pokit.user.port.out.UserPort
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserServiceTest : BehaviorSpec({
    val userPort = mockk<UserPort>()
    val userService = UserService(userPort)

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
