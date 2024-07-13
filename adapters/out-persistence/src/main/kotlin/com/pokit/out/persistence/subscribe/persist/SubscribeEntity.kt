package com.pokit.out.persistence.subscribe.persist

import jakarta.persistence.*

@Table(name = "SUBSCRIBE")
@Entity
class SubscribeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "category_id")
    val categoryId: Long,

    @Column(name = "user_id")
    val userId: Long,
)
