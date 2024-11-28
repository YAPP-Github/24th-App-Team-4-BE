package com.pokit.out.persistence.user.persist

import com.pokit.user.model.Interest
import com.pokit.user.model.InterestType
import jakarta.persistence.*

@Table(name = "INTEREST")
@Entity
class InterestEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "interest_type")
    @Enumerated(EnumType.STRING)
    var interestType: InterestType
) {
    companion object {
        fun of(interest: Interest) = InterestEntity(
            id = interest.id,
            userId = interest.userId,
            interestType = interest.interestType
        )
    }
}

internal fun InterestEntity.toDomain() = Interest(
    id = this.id,
    userId = this.userId,
    interestType = this.interestType
)
