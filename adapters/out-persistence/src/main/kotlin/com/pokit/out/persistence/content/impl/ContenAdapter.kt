package com.pokit.out.persistence.content.impl

import com.pokit.content.model.Content
import com.pokit.content.port.out.ContentPort
import com.pokit.out.persistence.content.persist.ContentEntity
import com.pokit.out.persistence.content.persist.ContentRepository
import com.pokit.out.persistence.content.persist.toDomain
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ContenAdapter(
    private val contentRepository: ContentRepository
) : ContentPort {
    override fun loadByUserIdAndId(userId: Long, id: Long) = contentRepository.findByUserIdAndIdAndDeleted(userId, id)
        ?.run { toDomain() }

    override fun persist(content: Content): Content {
        val contentEntity = ContentEntity.of(content)
        return contentRepository.save(contentEntity).toDomain()
    }

    override fun delete(content: Content) {
        contentRepository.findByIdOrNull(content.id)
            ?.delete()
    }

    override fun deleteByUserId(userId: Long) {
        contentRepository.deleteByUserId(userId)
    }
}
