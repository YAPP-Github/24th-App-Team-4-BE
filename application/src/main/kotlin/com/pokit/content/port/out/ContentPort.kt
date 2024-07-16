package com.pokit.content.port.out

import com.pokit.content.model.Content

interface ContentPort {
    fun loadById(id: Long): Content?
}
