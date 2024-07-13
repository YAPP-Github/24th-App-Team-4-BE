package com.pokit.out.persistence.category.persist

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

    @Column(name = "memo")
    var memo: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "open_type")
    val openType: OpenType,

    @OneToOne
    @JoinColumn(name = "image_id")
    val image: CategoryImageEntity,
) : BaseEntity()
