package com.pokit.support

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MySQLContainer

class TestContainerSupport : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        val mysqlContainer: MySQLContainer<*> = MySQLContainer("mysql:8.0").apply {
            withExposedPorts(3306)
            start()
        }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${mysqlContainer.jdbcUrl}",
            "spring.datasource.username=${mysqlContainer.username}",
            "spring.datasource.password=${mysqlContainer.password}",
            "spring.jpa.hibernate.ddl-auto=create"
        ).applyTo(applicationContext.environment)
    }
}
