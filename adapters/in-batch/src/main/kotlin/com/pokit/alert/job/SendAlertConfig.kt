package com.pokit.alert.job

import com.navercorp.spring.batch.plus.kotlin.configuration.BatchDsl
import com.navercorp.spring.batch.plus.kotlin.configuration.step.SimpleStepBuilderDsl
import com.pokit.alert.component.AlertProcessor
import com.pokit.alert.component.AlertReader
import com.pokit.alert.component.AlertWriter
import com.pokit.alert.model.AlertBatch
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.interceptor.DefaultTransactionAttribute


@Configuration
class SendAlertConfig(
    private val transactionManager: PlatformTransactionManager,
    private val batch: BatchDsl,
    private val alertReader: AlertReader,
    private val alertProcessor: AlertProcessor,
    private val alertWriter: AlertWriter
) {
    @Bean(name = ["sendAlertJob"])
    fun sendAlertJob() = batch {
        job("sendAlertJob") {
            step(sendAlertStep())
        }
    }

    @Bean
    fun sendAlertStep() = batch {
        step("sendAlertStep") {
            chunk(50, transactionManager, fun SimpleStepBuilderDsl<AlertBatch, AlertBatch>.() {
                reader(alertReader)
                processor(alertProcessor)
                writer(alertWriter)
                transactionAttribute(DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NOT_SUPPORTED))
            })
        }
    }
}
