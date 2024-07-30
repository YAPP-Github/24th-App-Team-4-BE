package com.pokit.out.persistence.log.persist

import com.pokit.log.model.LogType
import com.pokit.log.model.UserLog
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
    val contentId: Long?,

    @Column(name = "user_id")
    val userId: Long,

    @Enumerated(EnumType.STRING)
    val type: LogType,

    @Column(name = "search_keyword")
    val searchKeyword: String?
) : BaseEntity() {
    companion object {
        fun of(userLog: UserLog) = UserLogEntity(
            contentId = userLog.contentId,
            userId = userLog.userId,
            type = userLog.type,
            searchKeyword = userLog.searchKeyword
        )
    }
}

internal fun UserLogEntity.toDomain() = UserLog(
    contentId = this.contentId,
    userId = this.userId,
    type = this.type,
    searchKeyword = this.searchKeyword
)
