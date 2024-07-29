package com.pokit.content.port.out

import com.pokit.content.model.Content

interface ContentPort {
    fun loadByUserIdAndId(userId: Long, id: Long): Content?

    fun persist(content: Content): Content

    fun delete(content: Content)

    fun deleteByUserId(userId: Long)
}
