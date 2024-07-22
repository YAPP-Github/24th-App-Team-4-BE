package com.pokit.content.port.out

import com.pokit.content.model.Content

interface ContentPort {
    fun loadByUserIdAndId(userId: Long, id: Long): Content?
}
