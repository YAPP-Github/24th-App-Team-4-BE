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

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @Column(name = "alert_enabled")
    var alertEnabled: Boolean = true
) : BaseEntity() {
    fun delete() {
        this.isDeleted = true
    }

    fun updateAlertEnabled(enabled: Boolean) {
        this.alertEnabled = enabled
    }

    companion object {
        fun of(sharedCategory: SharedCategory) = SharedCategoryEntity(
            id = sharedCategory.id,
            userId = sharedCategory.userId,
            categoryId = sharedCategory.categoryId,
            alertEnabled = sharedCategory.alertEnabled
        )
    }
}

internal fun SharedCategoryEntity.toDomain() = SharedCategory(
    id = this.id,
    userId = this.userId,
    categoryId = this.categoryId,
    alertEnabled = this.alertEnabled
)
