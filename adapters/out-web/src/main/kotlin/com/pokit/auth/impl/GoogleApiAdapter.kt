package com.pokit.auth.impl

import com.pokit.auth.common.support.GoogleFeignClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.common.exception.ClientValidationException
import com.pokit.token.exception.AuthErrorCode
import com.pokit.token.model.AuthPlatform
import com.pokit.user.dto.UserInfo
import org.apache.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class GoogleApiAdapter(
    private val googleFeignClient: GoogleFeignClient,
) : GoogleApiClient {
    override fun getUserInfo(idToken: String): UserInfo {
        val response = googleFeignClient.getUserInfo(idToken)

        return UserInfo(
            email = response.email,
            authPlatform = AuthPlatform.GOOGLE,
            sub = response.sub
        )
    }

    override fun revoke(refreshToken: String) {
        if (refreshToken.isBlank()) {
            return
        }
        val revokeResponse = googleFeignClient.revoke(refreshToken)

        if (revokeResponse.status() != HttpStatus.SC_OK) {
            throw ClientValidationException(AuthErrorCode.FAILED_TO_REVOKE)
        }
    }
}
