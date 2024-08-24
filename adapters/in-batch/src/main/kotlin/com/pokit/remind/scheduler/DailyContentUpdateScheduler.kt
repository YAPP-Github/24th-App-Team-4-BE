package com.pokit.remind.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DailyContentUpdateScheduler(
    private val jobLauncher: JobLauncher,
    private val updateDailyContent: Job,
) {
    private val logger = KotlinLogging.logger { }

    companion object {
        private const val 매일_자정 = "0 0 0 * * *"
    }

    @Scheduled(cron = 매일_자정)
    fun updateDailyContent() {
        val jobParameters = JobParametersBuilder()
            .addLong("run.id", System.currentTimeMillis())
            .toJobParameters()

        logger.info { "[CONTENT BATCH] start daily content update job" }
        jobLauncher.run(updateDailyContent, jobParameters)
        logger.info { "[CONTENT BATCH] end daily content update job" }
    }
}
