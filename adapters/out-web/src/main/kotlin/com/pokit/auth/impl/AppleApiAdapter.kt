package com.pokit.auth.impl

import com.pokit.auth.common.dto.ApplePublicKeys
import com.pokit.auth.common.support.AppleKeyGenerator
import com.pokit.auth.common.support.AppleTokenParser
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.user.dto.UserInfo
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


@Component
class AppleApiAdapter(
    private val restTemplate: RestTemplate,
    private val appleKeyGenerator: AppleKeyGenerator,
    private val appleTokenParser: AppleTokenParser
) : AppleApiClient{
    override fun getUserInfo(idToken: String): UserInfo {
        val claims = decodeAndVerifyIdToken(idToken) // id token을 통해 사용자 정보 추출
        val email = claims["email"] as String

        return UserInfo(email = email)
    }

    // 애플에게 공개 키 요청 후 공개키로 idToken 내 고객 정보 추출
    private fun decodeAndVerifyIdToken(idToken: String): Map<String, Any> {
        val appleKeyUrl = "https://appleid.apple.com/auth/keys"
        val url = UriComponentsBuilder
            .fromUriString(appleKeyUrl)
            .encode()
            .build()
            .toUri()

        val publicKeys = restTemplate.getForObject(
            url,
            ApplePublicKeys::class.java
        )

        val header = appleTokenParser.parseHeader(idToken)
        val publicKey = appleKeyGenerator.generatePublicKey(header, publicKeys)
        val claims = appleTokenParser.parseClaims(idToken, publicKey)
        return claims
    }
}
