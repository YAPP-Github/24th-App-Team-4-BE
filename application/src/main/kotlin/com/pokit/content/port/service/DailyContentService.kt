package com.pokit.content.port.service

import com.pokit.content.dto.response.RemindContentResult
import com.pokit.content.port.`in`.DailyContentUseCase
import com.pokit.content.port.out.DailyContentPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DailyContentService(
    private val dailyContentPort: DailyContentPort,
) : DailyContentUseCase {
    companion object {
        private const val MIN_CONTENT_COUNT = 5
        private const val RANDOM_CONTENT_COUNT = 3
    }

    @Transactional
    override fun registerDailyContent(userId: Long) {
        val contentIds = dailyContentPort.fetchContentIdsByUserId(userId)
        val randomIds = getRandomIds(contentIds)
        dailyContentPort.persist(userId, randomIds)
    }

    @Transactional
    override fun deleteAll() =
        dailyContentPort.deleteAll()

    override fun getDailyContents(userId: Long): List<RemindContentResult> =
        dailyContentPort.loadByUserId(userId)

    private fun getRandomIds(ids: List<Long>): List<Long> {
        if (ids.isEmpty() || ids.size <= MIN_CONTENT_COUNT) {
            return emptyList()
        }

        return ids.shuffled().take(RANDOM_CONTENT_COUNT)
    }

}
