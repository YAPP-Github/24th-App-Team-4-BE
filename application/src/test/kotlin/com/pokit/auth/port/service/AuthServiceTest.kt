package com.pokit.auth.port.service

import com.pokit.auth.AuthFixture
import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.common.exception.ClientValidationException
import com.pokit.user.UserFixture
import com.pokit.user.port.out.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

@DisplayName("[AuthService Test]")
class AuthServiceTest : BehaviorSpec({
    val googleApiClient = mockk<GoogleApiClient>()
    val tokenProvider = mockk<TokenProvider>()
    val userPort = mockk<UserPort>()
    val appleApiClient = mockk<AppleApiClient>()
    val authService = AuthService(googleApiClient, appleApiClient, tokenProvider, userPort)

    Given("사용자가 로그인할 때") {
        val request = AuthFixture.getGoogleSigniInRequest()
        val invalidPlatformRequst = AuthFixture.getInvalidSignInRequest() // 플랫폼명 오타 요청
        val user = UserFixture.getUser()
        val userInfo = UserFixture.getUserInfo()
        val token = AuthFixture.getToken()

        every { googleApiClient.getUserInfo(request.idToken) } returns userInfo
        every { userPort.loadByEmail(userInfo.email) } returns user
        every { tokenProvider.createToken(user.id) } returns token

        When("구글 플랫폼으로 올바른 인증코드로 요청하면") {
            val resultToken = authService.signIn(request)
            Then("토큰이 정상적으로 반환된다.") {
                resultToken.accessToken shouldBe token.accessToken
                resultToken.refreshToken shouldBe token.refreshToken
            }
        }
        When("잘못된 플랫폼을 요청으로 보내면") {
            Then("예외가 발생한다.") {
                shouldThrow<ClientValidationException> {
                    authService.signIn(invalidPlatformRequst)
                }
            }
        }
    }
})
