package com.pokit.out.persistence.alert.persist

interface AlertJdbcRepository {
    fun bulkInsert(alertEntities: List<AlertEntity>)
}
