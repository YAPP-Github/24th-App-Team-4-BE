package com.pokit.out.persistence.user.persist

import com.pokit.user.model.UserImage
import jakarta.persistence.*

@Table(name = "USER_IMAGE")
@Entity
class UserImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "url")
    val url: String
) {
    companion object {
        fun of(userImage: UserImage) =
            UserImageEntity(
                id = userImage.id,
                url = userImage.url
            )
    }
}

fun UserImageEntity.toDomain() = UserImage(this.id, this.url)
