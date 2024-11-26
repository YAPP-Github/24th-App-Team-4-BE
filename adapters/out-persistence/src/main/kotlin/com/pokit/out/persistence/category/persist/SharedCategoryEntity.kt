package com.pokit.out.persistence.category.persist

import com.pokit.category.model.SharedCategory
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "SHARED_CATEGORY")
@Entity
class SharedCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "category_id")
    val categoryId: Long,
) : BaseEntity() {
    companion object {
        fun of(sharedCategory: SharedCategory) = SharedCategoryEntity(
            userId = sharedCategory.userId,
            categoryId = sharedCategory.categoryId
        )
    }
}

internal fun SharedCategoryEntity.toDomain() = SharedCategory(
    userId = this.userId,
    categoryId = this.categoryId
)
