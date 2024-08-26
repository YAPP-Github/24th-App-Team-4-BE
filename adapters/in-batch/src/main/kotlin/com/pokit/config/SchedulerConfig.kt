package com.pokit.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
@EnableScheduling
class SchedulerConfig {
    companion object {
        private const val POOL_SIZE = 3
    }

    @Bean("schedulerTask")
    fun taskScheduler(): TaskScheduler {
        val executor = ThreadPoolTaskScheduler()
        executor.poolSize = POOL_SIZE
        executor.threadNamePrefix = "scheduler-thread-"
        executor.initialize()
        return executor
    }
}
