package com.pokit.user.port.out

import com.pokit.user.model.UserImage

interface UserImagePort {
    fun loadById(id: Int): UserImage?

    fun loadAll(): List<UserImage>
}
