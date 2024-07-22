package com.pokit.out.persistence.category.persist

import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import jakarta.persistence.*

@Table(name = "CATEGORY_IMAGE")
@Entity
class CategoryImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "url")
    val url: String,
) {
    companion object {
        fun of(categoryImage: CategoryImage) =
            CategoryImageEntity(
                id = categoryImage.imageId,
                url = categoryImage.imageUrl
            )
    }
}

fun CategoryImageEntity.toDomain() = CategoryImage(
    imageId = this.id,
    imageUrl = this.url
)

