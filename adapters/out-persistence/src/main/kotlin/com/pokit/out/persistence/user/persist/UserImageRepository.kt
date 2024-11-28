package com.pokit.out.persistence.user.persist

import org.springframework.data.jpa.repository.JpaRepository

interface UserImageRepository : JpaRepository<UserImageEntity, Int> {
}
