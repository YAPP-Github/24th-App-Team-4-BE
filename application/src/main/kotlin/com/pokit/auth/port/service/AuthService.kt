package com.pokit.auth.port.service

import com.pokit.auth.port.`in`.AuthUseCase
import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.token.dto.request.SignInRequest
import com.pokit.token.model.AuthPlatform
import com.pokit.token.model.Token
import com.pokit.user.dto.UserInfo
import com.pokit.user.model.Role
import com.pokit.user.model.User
import com.pokit.user.port.out.UserPort
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val googleApiClient: GoogleApiClient,
    private val appleApiClient: AppleApiClient,
    private val tokenProvider: TokenProvider,
    private val userPort: UserPort,
) : AuthUseCase {
    override fun signIn(request: SignInRequest): Token {
        val platformType = AuthPlatform.of(request.authPlatform)

        val userInfo =
            when (platformType) {
                AuthPlatform.GOOGLE -> googleApiClient.getUserInfo(request.authorizationCode)
                AuthPlatform.APPLE -> appleApiClient.getUserInfo(request.authorizationCode)
            }

        val userEmail = userInfo.email
        val user = userPort.loadByEmail(userEmail) ?: createUser(userEmail) // 없으면 저장

        val token = tokenProvider.createToken(user.id)

        return token
    }

    private fun createUser(email: String): User {
        val user = User(email = email, role = Role.USER)
        return userPort.persist(user)
    }
}
