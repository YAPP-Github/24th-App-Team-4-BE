package com.pokit.out.persistence.category.persist

import jakarta.persistence.*

@Table(name = "CATEGORY_IMAGE")
@Entity
class CategoryImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "path")
    val path: String,
)
