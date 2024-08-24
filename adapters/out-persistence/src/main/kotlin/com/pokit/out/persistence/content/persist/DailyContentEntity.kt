package com.pokit.out.persistence.content.persist

import jakarta.persistence.*

@Table(name = "DAILY_CONTENT")
@Entity
class DailyContentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    val userId: Long,

    val contentId: Long,
)
