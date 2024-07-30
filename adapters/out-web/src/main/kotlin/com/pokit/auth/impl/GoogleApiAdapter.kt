package com.pokit.auth.impl

import com.pokit.auth.common.property.GoogleProperty
import com.pokit.auth.common.support.GoogleFeignClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.common.exception.ClientValidationException
import com.pokit.token.exception.AuthErrorCode
import com.pokit.token.model.AuthPlatform
import com.pokit.user.dto.UserInfo
import org.springframework.stereotype.Component

@Component
class GoogleApiAdapter(
    private val googleFeignClient: GoogleFeignClient,
    private val googleProperty: GoogleProperty
) : GoogleApiClient {
    override fun getUserInfo(idToken: String): UserInfo {
        val response = googleFeignClient.getUserInfo(idToken)

        return UserInfo(
            email = response.email,
            authPlatform = AuthPlatform.GOOGLE
        )
    }

    override fun revoke(authorizationCode: String) {
        val tokenResponse = googleFeignClient.getToken(
            authorizationCode,
            googleProperty.clientId,
            googleProperty.clientSecret,
            "authorization_code"
        ) ?: throw ClientValidationException(AuthErrorCode.INVALID_AUTHORIZATION_CODE)

        val revokeResponse = googleFeignClient.revoke(tokenResponse.accessToken)

        if (revokeResponse.status() != 200) {
            throw ClientValidationException(AuthErrorCode.FAILED_TO_REVOKE)
        }
    }
}
