package com.pokit.user.port.out

import com.pokit.user.model.Interest

interface InterestPort {
    fun persist(interest: Interest): Interest

    fun delete(interest: Interest)

    fun loadByUserId(userId: Long): List<Interest>
}
