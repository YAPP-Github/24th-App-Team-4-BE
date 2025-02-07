package com.pokit.out.persistence.category.persist

import com.pokit.category.model.OpenType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun existsByNameAndUserIdAndDeleted(name: String, userId: Long, deleted: Boolean): Boolean
    fun findByUserIdAndDeleted(userId: Long, deleted: Boolean, pageable: Pageable): Slice<CategoryEntity>
    fun findByIdAndUserIdAndDeleted(id: Long, userId: Long, deleted: Boolean): CategoryEntity?
    fun countByUserIdAndDeleted(userId: Long, deleted: Boolean): Int
    fun findByIdAndOpenTypeAndDeleted(id: Long, openType: OpenType, deleted: Boolean): CategoryEntity?
    fun findByNameAndUserId(name: String, userId: Long): CategoryEntity?

    @Query(
        """
            select ca from CategoryEntity ca
            join ContentEntity co on co.categoryId = ca.id
            where ca.id in :categoryIds and ca.deleted = :isDeleted
            group by ca.id
            order by max(co.createdAt) desc
        """
    )
    fun findAllByIdInAndDeleted(
        @Param("categoryIds") categoryIds: List<Long>,
        pageable: Pageable,
        @Param("isDeleted") isDeleted: Boolean
    ): Slice<CategoryEntity>
}
