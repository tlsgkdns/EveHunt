package com.evehunt.evehunt.global.common.page

import kotlin.math.ceil
import kotlin.math.min

data class PageResponse<T>(
    val page: Int,
    val size: Int,
    val sortType: String?,
    val keyword: String?,
    val searchType: String?,
    val asc: Boolean?,
    val total: Int,
    val start: Int,
    val end: Int,
    val prev: Boolean,
    val next: Boolean,
    val dtoList: List<T>
)
{
    companion object
    {
        fun<T> of(pageRequestDTO: PageRequest, dtoList: List<T>, total: Int): PageResponse<T>
        {
            val page = pageRequestDTO.page
            val size = pageRequestDTO.size
            val last = ceil(total / size.toDouble())
            val end = min(ceil(page / 10.0) * 10, last).toInt()
            val start = (ceil(page / 10.0) * 10).toInt() - 9
            return PageResponse(
                page = pageRequestDTO.page,
                size = size,
                asc = pageRequestDTO.asc,
                sortType = pageRequestDTO.sortType,
                total = total,
                dtoList = dtoList,
                start = start,
                end = end,
                next = total > end * size,
                prev = (start > 1),
                searchType = pageRequestDTO.searchType,
                keyword = pageRequestDTO.keyword
            )
        }
    }
}