package com.pokit.remind.job

import com.navercorp.spring.batch.plus.kotlin.configuration.BatchDsl
import com.pokit.content.port.`in`.DailyContentUseCase
import com.pokit.user.port.`in`.UserUseCase
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class DailyContentConfig(
    private val transactionManager: PlatformTransactionManager,
    private val dailyContentUseCase: DailyContentUseCase,
    private val userUseCase: UserUseCase,
    private val batch: BatchDsl,
) {

    @Bean
    fun updateDailyContentJob(): Job = batch {
        job("updateDailyContentJob") {
            step(deleteAllStep()) {
                on("COMPLETED") {
                    step(updateDailyContentStep())
                }
            }
        }
    }

    @Bean
    fun deleteAllStep(): Step = batch {
        step("deleteAllStep") {
            tasklet({ _, _ ->
                dailyContentUseCase.deleteAll()
                RepeatStatus.FINISHED
            }, transactionManager)
        }
    }

    @Bean
    fun updateDailyContentStep(): Step = batch {
        step("updateDailyContentStep") {
            tasklet({ _, _ ->
                val userIds: List<Long> = userUseCase.fetchAllUserId()

                for (userId in userIds) {
                    dailyContentUseCase.registerDailyContent(userId)
                }
                RepeatStatus.FINISHED
            }, transactionManager)
        }
    }
}
