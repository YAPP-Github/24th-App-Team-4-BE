package com.pokitmonz.pokit.common.support

import com.pokitmonz.pokit.common.config.TestAuditingConfig
import com.pokitmonz.pokit.support.TestContainerSupport
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Import(TestAuditingConfig::class)
abstract class DataJpaTestSupport : TestContainerSupport()
