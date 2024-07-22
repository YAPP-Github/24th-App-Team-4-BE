package com.pokit.out.persistence.category.persist

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryImageRepository : JpaRepository<CategoryImageEntity, Int> {
}
