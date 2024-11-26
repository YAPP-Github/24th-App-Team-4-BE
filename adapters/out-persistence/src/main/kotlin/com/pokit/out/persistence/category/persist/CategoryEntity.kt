package com.pokit.out.persistence.category.persist

import com.pokit.category.model.Category
import com.pokit.category.model.OpenType
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

// 포킷 엔티티
@Table(name = "CATEGORY")
@Entity
class CategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "name")
    var name: String,

    @OneToOne
    @JoinColumn(name = "image_id", foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    val image: CategoryImageEntity,

    @Column(name = "open_type")
    @Enumerated(EnumType.STRING)
    var openType: OpenType = OpenType.PRIVATE,

    @Column(name = "user_count")
    var userCount: Int = 0,

    @Column(name = "is_shared")
    var isShared: Boolean = false
) : BaseEntity() {

    @Column(name = "is_deleted")
    var deleted: Boolean = false

    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(category: Category) =
            CategoryEntity(
                id = category.categoryId,
                userId = category.userId,
                name = category.categoryName,
                image = CategoryImageEntity.of(category.categoryImage),
                openType = category.openType,
                userCount = category.userCount,
                isShared = category.isShared
            )
    }
}

fun CategoryEntity.toDomain() = Category(
    categoryId = this.id,
    categoryName = this.name,
    categoryImage = this.image.toDomain(),
    userId = this.userId,
    createdAt = this.createdAt,
    openType = this.openType,
    userCount = this.userCount,
    isShared = this.isShared
)
