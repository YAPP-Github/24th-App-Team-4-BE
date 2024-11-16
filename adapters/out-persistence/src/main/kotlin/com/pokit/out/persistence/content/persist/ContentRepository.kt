package com.pokit.out.persistence.content.persist

import com.pokit.content.model.ContentWithUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContentRepository : JpaRepository<ContentEntity, Long>, ContentJdbcRepository {
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

    fun countByCategoryIdAndDeleted(id: Long, deleted: Boolean): Int

    @Modifying(clearAutomatically = true)
    @Query(
        """
        update ContentEntity c set c.deleted = true
        where c.categoryId in
        (select ct.id from CategoryEntity ct where ct.userId = :userId)
    """
    )
    fun deleteByUserId(@Param("userId") userId: Long)

    fun findByIdIn(ids: List<Long>): List<ContentEntity>

    @Query(
        """
        select count(co) from ContentEntity co
        join CategoryEntity ca on ca.id = co.categoryId
        join UserEntity u on u.id = ca.userId
        where u.id = :userId and co.deleted = false
    """
    )
    fun countByUserId(userId: Long): Int

    @Query(
        """
        select new com.pokit.content.model.ContentWithUser(c.id, u.id, c.title, c.thumbNail) from ContentEntity c
        join CategoryEntity ca on ca.id = c.categoryId
        join UserEntity u on u.id = ca.userId
        where c.id in :ids
    """
    )
    fun findByIdInWithUser(ids: List<Long>): List<ContentWithUser>

    @Modifying(clearAutomatically = true)
    @Query(
        """
        update ContentEntity c set c.deleted = true
        where c.id in :contentIds
    """
    )
    fun deleteByContentIds(@Param("contentIds") contentIds: List<Long>)
}
