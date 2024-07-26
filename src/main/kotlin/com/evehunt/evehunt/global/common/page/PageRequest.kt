package com.evehunt.evehunt.global.common.page

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

data class PageRequest(
    val page: Int = 1,
    val size: Int = 10,
    val link: String? = null,
    val asc: Boolean? = true,
    val sortType: String? = null,
    val keyword: String? = null,
    val searchType: String? = null
)
{
    fun getPageable(): Pageable
    {
        return PageRequest.of(page - 1, size)
    }
}