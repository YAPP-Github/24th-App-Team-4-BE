package com.pokit.alert.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SendAlertScheduler(
    private val jobLauncher: JobLauncher,
    @Qualifier("sendAlertJob") private val sendAlertJob: Job
) {
    private val logger = KotlinLogging.logger { }

    companion object {
        private const val 매일_오전_8시 = "0 0 8 * * *"
    }

    @Scheduled(cron = 매일_오전_8시)
    fun sendAlert() {
        val jobParameters = JobParametersBuilder()
            .addLong("run.id", System.currentTimeMillis())
            .toJobParameters()

        logger.info { "[ALERT BATCH] start daily send alert job" }
        jobLauncher.run(sendAlertJob, jobParameters)
        logger.info { "[ALERT BATCH] end daily send alert job" }
    }
}
