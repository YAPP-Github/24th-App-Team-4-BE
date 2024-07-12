package com.pokit.auth.common.dto

data class ApplePublicKeys(
    val keys: List<ApplePublicKey>,
) {
    fun getMatchedKey(alg: String, kid: String): ApplePublicKey? {
        return keys.firstOrNull { key ->
            key.alg == alg && key.kid == kid
        }
    }
}
