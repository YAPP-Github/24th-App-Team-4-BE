package com.pokit.auth.common.support

import com.pokit.auth.common.dto.ApplePublicKey
import com.pokit.auth.common.dto.ApplePublicKeys
import com.pokit.common.exception.ClientValidationException
import com.pokit.token.exception.AuthErrorCode
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

@Component
class AppleKeyGenerator {
    fun generatePublicKey(headers: Map<String, String>, publicKeys: ApplePublicKeys): PublicKey {
        val alg = headers["alg"] ?: throw ClientValidationException(AuthErrorCode.INVALID_ID_TOKEN)
        val kid = headers["kid"] ?: throw ClientValidationException(AuthErrorCode.INVALID_ID_TOKEN)
        val publicKey = publicKeys.getMatchedKey(alg, kid) ?: throw ClientValidationException(AuthErrorCode.INVALID_ID_TOKEN)

        return getPublicKey(publicKey)
    }

    private fun getPublicKey(publicKey: ApplePublicKey): PublicKey {
        val nBytes = Base64.getUrlDecoder().decode(publicKey.n)
        val eBytes = Base64.getUrlDecoder().decode(publicKey.e)

        val publicKeySpec = RSAPublicKeySpec(BigInteger(1, nBytes), BigInteger(1, eBytes))

        val keyFactory = KeyFactory.getInstance(publicKey.kty)
        return keyFactory.generatePublic(publicKeySpec)
    }
}
