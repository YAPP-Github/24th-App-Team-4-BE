package com.pokit.category.port.out

import com.pokit.category.model.CategoryImage

interface CategoryImagePort {
    fun loadById(id: Int): CategoryImage?
    fun loadAll(): List<CategoryImage>
}
