package com.pokit.out.persistence.log.persist

import com.pokit.log.model.LogType
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "USER_LOG")
@Entity
class UserLogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "content_id")
    val contentId: Long,

    @Column(name = "user_id")
    val userId: Long,

    @Enumerated(EnumType.STRING)
    val type: LogType,
) : BaseEntity() {
}
