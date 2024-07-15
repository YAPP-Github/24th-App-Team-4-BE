package com.pokit.auth.common.support

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import java.util.*

@Component
class AppleTokenParser(
    private val objectMapper: ObjectMapper
) {
    private val typeReference = object : TypeReference<Map<String, String>>() {}

    fun parseHeader(idToken: String): Map<String, String> {
        val header = idToken.split("\\.")[0]
        val decodedHeader = String(Base64.getDecoder().decode(header), StandardCharsets.UTF_8)
        return objectMapper.readValue(decodedHeader, typeReference)
    }

    fun parseClaims(
        idToken: String,
        publicKey: PublicKey,
    ): Claims {
        return Jwts.parser()
            .verifyWith(publicKey)
            .build()
            .parseSignedClaims(idToken)
            .payload
    }
}
