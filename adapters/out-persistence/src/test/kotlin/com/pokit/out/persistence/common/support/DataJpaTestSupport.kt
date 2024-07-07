package com.pokit.out.persistence.common.support

import com.pokit.out.persistence.common.config.TestAuditingConfig
import com.pokit.support.TestContainerSupport
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Import(TestAuditingConfig::class)
abstract class DataJpaTestSupport : TestContainerSupport()
