package com.pokit.auth.impl

import com.google.firebase.auth.FirebaseAuth
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.token.model.AuthPlatform
import com.pokit.user.dto.UserInfo
import org.springframework.stereotype.Component

@Component
class GoogleApiAdapter(
    private val firebaseAuth: FirebaseAuth
) : GoogleApiClient {
    override fun getUserInfo(authorizationCode: String): UserInfo {
        val decodedToken = verifyIdToken(authorizationCode)
        return UserInfo(decodedToken.email, AuthPlatform.GOOGLE) // 로그인 한 사용자의 이메일
    }

    private fun verifyIdToken(idToken: String) = firebaseAuth.verifyIdToken(idToken)
}
