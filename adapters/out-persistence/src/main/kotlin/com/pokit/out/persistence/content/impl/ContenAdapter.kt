package com.pokit.out.persistence.content.impl

import com.pokit.content.port.out.ContentPort
import com.pokit.out.persistence.content.persist.ContentRepository
import com.pokit.out.persistence.content.persist.toDomain
import org.springframework.stereotype.Repository

@Repository
class ContenAdapter(
    private val contentRepository: ContentRepository
) : ContentPort {
    override fun loadByUserIdAndId(userId: Long, id: Long) = contentRepository.findByUserIdAndId(userId, id)
        ?.run { toDomain() }
}
