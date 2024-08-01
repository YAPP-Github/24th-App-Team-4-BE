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

    @Column(name = "is_deleted")
    var deleted: Boolean = false,

    @Column(name = "domain")
    val domain: String
) : BaseEntity() {
    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(content: Content) = ContentEntity(
            id = content.id,
            categoryId = content.categoryId,
            type = content.type,
            data = content.data,
            title = content.title,
            memo = content.memo,
            alertYn = content.alertYn,
            domain = content.domain
        )
    }
}

fun ContentEntity.toDomain() = Content(
    id = this.id,
    categoryId = this.categoryId,
    type = this.type,
    data = this.data,
    title = this.title,
    memo = this.memo,
    alertYn = this.alertYn,
    createdAt = this.createdAt,
    domain = this.domain
)
