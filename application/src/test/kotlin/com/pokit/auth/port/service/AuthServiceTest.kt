package com.pokit.auth.port.service

import com.pokit.auth.AuthFixture
import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.common.exception.ClientValidationException
import com.pokit.content.port.out.ContentPort
import com.pokit.token.model.AuthPlatform
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
    val contentPort = mockk<ContentPort>()
    val authService = AuthService(googleApiClient, appleApiClient, tokenProvider, userPort, contentPort)

    Given("사용자가 인증 관련하여 요청 시") {
        val request = AuthFixture.getGoogleSigniInRequest()
        val invalidPlatformRequst = AuthFixture.getInvalidSignInRequest() // 플랫폼명 오타 요청
        val user = UserFixture.getUser()
        val userInfo = UserFixture.getUserInfo()
        val token = AuthFixture.getToken()
        val reissueResult = "accessToken"

        every { googleApiClient.getUserInfo(request.idToken) } returns userInfo
        every {
            userPort.loadByEmailAndAuthPlatform(userInfo.email!!, AuthPlatform.of(request.authPlatform))
        } returns user
        every { userPort.loadById(user.id) } returns user
        every { tokenProvider.createToken(user.id) } returns token
        every { tokenProvider.getUserId("refresh") } returns user.id
        every { tokenProvider.reissueToken(user.id, "refresh") } returns reissueResult

        When("로그인할 때 구글 플랫폼으로 올바른 인증코드로 요청하면") {
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
        When("Refresh Token으로 토큰 재발급을 요청하면") {
            val result = authService.reissue("refresh")
            Then("액세스 토큰을 재발급한다.") {
                result shouldBe reissueResult
            }
        }
    }
})
