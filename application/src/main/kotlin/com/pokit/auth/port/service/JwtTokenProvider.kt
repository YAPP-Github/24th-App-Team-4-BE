package com.pokit.auth.port.service

import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.auth.port.out.RefreshTokenPort
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
    private val refreshTokenPort: RefreshTokenPort,
) : TokenProvider {
    companion object {
        private const val TO_DAY = 86400000
    }
    override fun createToken(userId: Long): Token {
        val accessToken = generateToken(userId, jwtProperty.accessExpiryTime * TO_DAY)
        val refreshToken = generateToken(userId, jwtProperty.refreshExpiryTime * TO_DAY)

        refreshTokenPort.deleteByUserId(userId)
        refreshTokenPort.persist(RefreshToken(userId, refreshToken))

        return Token(accessToken, refreshToken)
    }

    override fun reissueToken(userId: Long, refreshToken: String): String {
        val findRefreshToken = refreshTokenPort.loadByUserId(userId)
            ?: throw ClientValidationException(AuthErrorCode.NOT_FOUND_TOKEN)

        if (findRefreshToken.token != refreshToken) {
            throw ClientValidationException(AuthErrorCode.INVALID_TOKEN)
        }

        return generateToken(userId, jwtProperty.accessExpiryTime * TO_DAY)
    }

    override fun deleteRefreshToken(refreshTokenId: Long) {
        refreshTokenPort.deleteById(refreshTokenId)
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
