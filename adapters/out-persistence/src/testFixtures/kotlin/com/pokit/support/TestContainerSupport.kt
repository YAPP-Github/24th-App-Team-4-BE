package com.pokit.support

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

abstract class TestContainerSupport {
    companion object {
        private const val MYSQL_IMAGE = "mysql:8.0"

        private const val MYSQL_PORT = 3306

        private val MYSQL: JdbcDatabaseContainer<*> =
            MySQLContainer<Nothing>(DockerImageName.parse(MYSQL_IMAGE))
                .withExposedPorts(MYSQL_PORT)

        init {
            MYSQL.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.driver-class-name") { MYSQL.driverClassName }
            registry.add("spring.datasource.url") { MYSQL.jdbcUrl }
            registry.add("spring.datasource.username") { MYSQL.username }
            registry.add("spring.datasource.password") { MYSQL.password }
        }
    }
}
