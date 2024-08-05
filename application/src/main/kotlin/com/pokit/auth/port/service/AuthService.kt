package com.pokit.auth.port.service

import com.pokit.auth.port.`in`.AuthUseCase
import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.common.exception.ClientValidationException
import com.pokit.content.port.out.ContentPort
import com.pokit.token.dto.request.RevokeRequest
import com.pokit.token.dto.request.SignInRequest
import com.pokit.token.exception.AuthErrorCode
import com.pokit.token.model.AuthPlatform
import com.pokit.token.model.Token
import com.pokit.user.dto.UserInfo
import com.pokit.user.model.Role
import com.pokit.user.model.User
import com.pokit.user.port.out.UserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val googleApiClient: GoogleApiClient,
    private val appleApiClient: AppleApiClient,
    private val tokenProvider: TokenProvider,
    private val userPort: UserPort,
    private val contentPort: ContentPort
) : AuthUseCase {
    @Transactional
    override fun signIn(request: SignInRequest): Token {
        val platformType = AuthPlatform.of(request.authPlatform)

        val userInfo =
            when (platformType) {
                AuthPlatform.GOOGLE -> googleApiClient.getUserInfo(request.idToken)
                AuthPlatform.APPLE -> appleApiClient.getUserInfo(request.idToken)
            }

        val user = userPort.loadByEmail(userInfo.email) ?: createUser(userInfo) // 없으면 저장

        val token = tokenProvider.createToken(user.id)

        return token
    }

    @Transactional
    override fun withDraw(user: User, request: RevokeRequest) {
        if (user.authPlatform != request.authPlatform) {
            throw ClientValidationException(AuthErrorCode.INCORRECT_PLATFORM)
        }

        when (request.authPlatform) {
            AuthPlatform.GOOGLE -> googleApiClient.revoke(request.refreshToken)
            AuthPlatform.APPLE -> appleApiClient.revoke(request.refreshToken)
        }
        contentPort.deleteByUserId(user.id)
        userPort.delete(user)
    }

    private fun createUser(userInfo: UserInfo): User {
        val user = User(email = userInfo.email, role = Role.USER, authPlatform = userInfo.authPlatform)
        return userPort.persist(user)
    }
}
