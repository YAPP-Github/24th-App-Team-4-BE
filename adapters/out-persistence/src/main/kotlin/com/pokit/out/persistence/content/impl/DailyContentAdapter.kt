package com.pokit.out.persistence.content.impl

import com.pokit.content.dto.response.RemindContentResult
import com.pokit.content.port.out.DailyContentPort
import com.pokit.out.persistence.category.persist.QCategoryEntity.categoryEntity
import com.pokit.out.persistence.content.persist.DailyContentEntity
import com.pokit.out.persistence.content.persist.DailyContentRepository
import com.pokit.out.persistence.content.persist.QContentEntity.contentEntity
import com.pokit.out.persistence.content.persist.toDomain
import org.springframework.stereotype.Component

@Component
class DailyContentAdapter(
    private val dailyContentRepository: DailyContentRepository,
) : DailyContentPort {
    override fun fetchContentIdsByUserId(userId: Long) =
        dailyContentRepository.getContentIdsByUserId(userId)

    override fun loadByUserId(userId: Long): List<RemindContentResult> {
        val contentEntityList = dailyContentRepository.getDailyContentsByUserId(userId)

        return contentEntityList.map {
            RemindContentResult.of(
                it[contentEntity]!!.toDomain(),
                it[categoryEntity.name]!!,
            )
        }
    }

    override fun persist(userId: Long, ids: List<Long>) {
        val dailyContentEntities = ids.map { contentId ->
            DailyContentEntity(
                userId = userId,
                contentId = contentId
            )
        }

        dailyContentRepository.saveAll(dailyContentEntities)
    }

    override fun deleteAll() =
        dailyContentRepository.deleteAll()

}
