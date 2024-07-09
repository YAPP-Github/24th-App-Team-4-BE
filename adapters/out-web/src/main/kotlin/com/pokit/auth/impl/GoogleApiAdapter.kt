package com.pokit.auth.impl

import com.google.firebase.auth.FirebaseAuth
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.user.dto.UserInfo
import org.springframework.stereotype.Component

@Component
class GoogleApiAdapter : GoogleApiClient{
    override fun getUserInfo(authorizationCode: String): UserInfo {
        val decodedToken = verifyIdToken(authorizationCode)
        return UserInfo(decodedToken.email) // 로그인 한 사용자의 이메일
    }

    private fun verifyIdToken(idToken: String) = FirebaseAuth.getInstance().verifyIdToken(idToken)
}
