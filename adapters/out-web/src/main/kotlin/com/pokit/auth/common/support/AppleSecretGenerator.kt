package com.pokit.auth.common.support

import com.pokit.auth.common.property.AppleProperty
import com.pokit.common.exception.ExternalApiException
import com.pokit.token.exception.AuthErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.stereotype.Component
import java.io.StringReader
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class AppleSecretGenerator(
    private val appleProperty: AppleProperty
) {
    private val logger = KotlinLogging.logger { }

    fun createClientSecret(): String {
        val expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
        val jwtHeader: Map<String, Any?> = mapOf("kid" to appleProperty.keyId, "alg" to "ES256")


        return Jwts.builder()
            .header().add(jwtHeader)
            .and()
            .issuer(appleProperty.teamId)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .audience().add("https://appleid.apple.com")
            .and()
            .subject(appleProperty.audience)
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact()
    }

    private fun getPrivateKey(): PrivateKey {
        return try {
            val privateKeyContent = appleProperty.privateKey
            val pemReader = StringReader(privateKeyContent)
            val pemParser = PEMParser(pemReader)
            val converter = JcaPEMKeyConverter()
            val objects = pemParser.readObject() as PrivateKeyInfo
            converter.getPrivateKey(objects)
        } catch (e: Exception) {
            logger.warn { "AppleException: ${e.message} / $e" }
            throw ExternalApiException(AuthErrorCode.APPLE_KEY_FAILED)
        }
    }
}
