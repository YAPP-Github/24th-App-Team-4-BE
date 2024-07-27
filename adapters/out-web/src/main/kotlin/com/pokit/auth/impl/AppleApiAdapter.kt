package com.pokit.auth.impl

import com.pokit.auth.common.dto.AppleRevokeRequest
import com.pokit.auth.common.dto.AppleTokenResponse
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

    override fun revoke(authorizationCode: String) {
        val clientSecret = appleSecretGenerator.createClientSecret()
        val tokenResponse = getAuthToken(authorizationCode, clientSecret)
            ?: throw ClientValidationException(AuthErrorCode.INVALID_AUTHORIZATION_CODE)

        revokeAuth(tokenResponse.accessToken, clientSecret)
    }

    // 애플에게 공개 키 요청 후 공개키로 idToken 내 고객 정보 추출
    private fun decodeAndVerifyIdToken(idToken: String): Map<String, Any> {
        val publicKeys = appleFeignClient.getApplePublicKeys()

        val header = appleTokenParser.parseHeader(idToken)
        val publicKey = appleKeyGenerator.generatePublicKey(header, publicKeys)
        val claims = appleTokenParser.parseClaims(idToken, publicKey)
        return claims
    }

    private fun getAuthToken(authorizationCode: String, clientSecret: String): AppleTokenResponse? {
        return appleFeignClient.getToken(
            appleProperty.clientId,
            clientSecret,
            authorizationCode,
            "authorization_code"
        )
    }

    private fun revokeAuth(accessToken: String, clientSecret: String) {
        val request = AppleRevokeRequest(
            appleProperty.clientId,
            clientSecret,
            accessToken,
            "access_token"
        )
        val response = appleFeignClient.revoke(request)
        if (response.status() != 200) {
            throw ClientValidationException(AuthErrorCode.FAILED_TO_REVOKE)
        }
    }
}
