package com.pokit.out.persistence.content.persist

import org.springframework.data.jpa.repository.JpaRepository

interface DailyContentRepository: JpaRepository<DailyContentEntity, Long>, DailyContentQuerydslRepository
