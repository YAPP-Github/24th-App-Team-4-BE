package com.pokit.auth.impl

import com.pokit.auth.common.dto.ApplePublicKeys
import com.pokit.auth.common.dto.AppleIdTokenResponse
import com.pokit.auth.common.property.AppleProperty
import com.pokit.auth.common.support.AppleKeyGenerator
import com.pokit.auth.common.support.AppleTokenParser
import com.pokit.auth.port.out.AppleApiClient
import com.pokit.user.dto.UserInfo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant
import java.util.*


@Component
class AppleApiAdapter(
    private val appleProperty: AppleProperty,
    private val restTemplate: RestTemplate,
    private val appleKeyGenerator: AppleKeyGenerator,
    private val appleTokenParser: AppleTokenParser
) : AppleApiClient{
    override fun getUserInfo(authorizationCode: String): UserInfo {
        val appleTokenResponse = exchangeCodeForToken(authorizationCode)
        val identityToken = appleTokenResponse.idToken // 인가코드로 id token 추출
        val claims = decodeAndVerifyIdToken(identityToken) // id token을 통해 사용자 정보 추출
        val email = claims["email"] as String

        return UserInfo(email = email)
    }

    private fun exchangeCodeForToken(authorizationCode: String): AppleIdTokenResponse {
        val clientSecret = generateClientSecret()
        val params = mapOf(
            "client_id" to appleProperty.clientId,
            "client_secret" to clientSecret,
            "code" to authorizationCode,
            "grant_type" to "authorization_code"
        )

        val response = restTemplate.postForObject(
            appleProperty.tokenUrl,
            params,
            AppleIdTokenResponse::class.java,
        )

        return response
    }

    private fun generateClientSecret(): String {
        val now = Instant.now()
        val exp = now.plusSeconds(15777000) // 6개월, 수정 예정
        val claims = Jwts.claims().subject(appleProperty.clientId).build()
        val keyBytes = Decoders.BASE64.decode(appleProperty.privateKey
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s+".toRegex(), ""))

        return Jwts.builder()
            .header().add("kid", appleProperty.keyId)
            .and()
            .claims(claims)
            .issuer(appleProperty.teamId)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .audience().add(appleProperty.audience)
            .and()
            .signWith(Keys.hmacShaKeyFor(keyBytes))
            .compact()
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
