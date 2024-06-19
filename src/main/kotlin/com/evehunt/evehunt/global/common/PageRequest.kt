package com.evehunt.evehunt.global.common

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

data class PageRequest(
    val page: Int = 1,
    val size: Int = 10,
    val link: String? = null
)
{
    fun getPageable(): Pageable
    {
        return PageRequest.of(page - 1, size)
    }
}