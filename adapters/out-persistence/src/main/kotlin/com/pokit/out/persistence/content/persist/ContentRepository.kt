package com.pokit.out.persistence.content.persist

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContentRepository : JpaRepository<ContentEntity, Long> {
    @Query(
        """
        select co from ContentEntity co
        join CategoryEntity ca on co.categoryId = ca.id
        join UserEntity u on ca.userId = :userId
        where co.id = :id
    """
    )
    fun findByUserIdAndId(
        @Param("userId") userId: Long,
        @Param("id") id: Long
    ): ContentEntity?
}
