package com.pokit.user.port.out

import com.pokit.user.model.FcmToken

interface FcmTokenPort {
    fun persist(fcmToken: FcmToken): FcmToken
}
