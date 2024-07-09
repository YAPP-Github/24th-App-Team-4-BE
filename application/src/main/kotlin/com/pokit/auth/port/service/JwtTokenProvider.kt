package com.pokit.auth.port.service

import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.auth.port.out.RefreshTokenRepository
import com.pokit.auth.property.JwtProperty
import com.pokit.common.exception.ClientValidationException
import com.pokit.token.exception.AuthErrorCode
import com.pokit.token.model.RefreshToken
import com.pokit.token.model.Token
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    private val jwtProperty: JwtProperty,
    private val refreshTokenRepository: RefreshTokenRepository,
) : TokenProvider {
    override fun createToken(userId: Long): Token {
        val accessToken = generateToken(userId, jwtProperty.accessExpiryTime)
        val refreshToken = generateToken(userId, jwtProperty.refreshExpiryTime)
        refreshTokenRepository.save(RefreshToken(userId, refreshToken))

        return Token(accessToken, refreshToken)
    }

    override fun reissueToken(refreshToken: String): String {
        val userId = getUserId(refreshToken)
        refreshTokenRepository.findByUserId(userId)
            ?: throw ClientValidationException(AuthErrorCode.NOT_FOUND_TOKEN)
        return generateToken(userId, jwtProperty.accessExpiryTime)
    }

    override fun deleteRefreshToken(refreshTokenId: Long) {
        refreshTokenRepository.deleteById(refreshTokenId)
    }

    override fun getUserId(token: String): Long {
        val claims = getClaims(token)
        val userId = claims.payload.subject
        return userId.toLong()
    }

    private fun generateToken(
        id: Long,
        expireTime: Long,
    ): String {
        val now = Date()
        val expireDate = Date(now.time + expireTime)
        val keyBytes = Decoders.BASE64.decode(jwtProperty.clientSecret)

        val claims = Jwts.claims().subject(id.toString()).build()
        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(Keys.hmacShaKeyFor(keyBytes))
            .compact()
    }

    private fun getClaims(token: String): Jws<Claims> {
        val keyBytes = Decoders.BASE64.decode(jwtProperty.clientSecret)

        try {
            return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ClientValidationException(AuthErrorCode.TOKEN_EXPIRED)
        } catch (e: MalformedJwtException) {
            throw ClientValidationException(AuthErrorCode.INVALID_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw ClientValidationException(AuthErrorCode.UNSUPPORTED_TOKEN)
        } catch (e: SignatureException) {
            throw ClientValidationException(AuthErrorCode.WRONG_SIGNATURE)
        }
    }
}
