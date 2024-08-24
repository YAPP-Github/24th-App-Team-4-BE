package com.pokit.auth.impl

import com.pokit.auth.common.dto.AppleRevokeRequest
import com.pokit.auth.common.property.AppleProperty
import com.pokit.auth.common.support.AppleFeignClient
import com.pokit.auth.common.support.AppleKeyGenerator
import com.pokit.auth.common.support.AppleSecretGenerator
import com.pokit.auth.common.support.AppleTokenParser
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.common.exception.ClientValidationException
import com.pokit.token.exception.AuthErrorCode
import com.pokit.token.model.AuthPlatform
import com.pokit.user.dto.UserInfo
import org.apache.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class AppleApiAdapter(
    private val appleKeyGenerator: AppleKeyGenerator,
    private val appleTokenParser: AppleTokenParser,
    private val appleProperty: AppleProperty,
    private val appleFeignClient: AppleFeignClient,
    private val appleSecretGenerator: AppleSecretGenerator
) : AppleApiClient {
    override fun getUserInfo(idToken: String): UserInfo {
        val claims = decodeAndVerifyIdToken(idToken) // id token을 통해 사용자 정보 추출
        val email = claims["email"] as String

        return UserInfo(email = email, authPlatform = AuthPlatform.APPLE)
    }

    override fun revoke(refreshToken: String) {
        if (refreshToken.isBlank()) {
            return
        }
        val clientSecret = appleSecretGenerator.createClientSecret()

        revokeAuth(refreshToken, clientSecret)
    }

    private fun decodeAndVerifyIdToken(idToken: String): Map<String, Any> {
        val publicKeys = appleFeignClient.getApplePublicKeys()

        val header = appleTokenParser.parseHeader(idToken)
        val publicKey = appleKeyGenerator.generatePublicKey(header, publicKeys)
        val claims = appleTokenParser.parseClaims(idToken, publicKey)
        return claims
    }

    private fun revokeAuth(refreshToken: String, clientSecret: String) {
        val request = AppleRevokeRequest(
            appleProperty.clientId,
            clientSecret,
            refreshToken,
            "refresh_token"
        )
        val response = appleFeignClient.revoke(request)
        if (response.status() != HttpStatus.SC_OK) {
            throw ClientValidationException(AuthErrorCode.FAILED_TO_REVOKE)
        }
    }
}
