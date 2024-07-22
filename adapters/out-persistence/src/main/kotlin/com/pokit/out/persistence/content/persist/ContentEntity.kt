package com.pokit.out.persistence.content.persist

import com.pokit.content.model.Content
import com.pokit.content.model.ContentType
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "CONTENT")
@Entity
class ContentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "category_id")
    val categoryId: Long,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: ContentType,

    @Column(name = "data")
    val data: String, // ex. LINK - url

    @Column(name = "title")
    val title: String,

    @Column(name = "memo")
    val memo: String,

    @Column(name = "alert_yn")
    val alertYn: String,
) : BaseEntity()

fun ContentEntity.toDomain() = Content(
    categoryId = this.categoryId,
    type = this.type,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn,
)
