package com.pokit.out.persistence.content.persist

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContentRepository : JpaRepository<ContentEntity, Long> {
    @Query(
        """
        select co from ContentEntity co
        join CategoryEntity ca on ca.id = co.categoryId
        join UserEntity u on u.id = ca.userId
        where co.id = :id and u.id = :userId and co.deleted = false
    """
    )
    fun findByUserIdAndIdAndDeleted(
        @Param("userId") userId: Long,
        @Param("id") id: Long
    ): ContentEntity?

    fun countByCategoryId(id: Long): Int
}
