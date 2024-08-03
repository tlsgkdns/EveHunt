package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.dto.EventCardResponse
import com.evehunt.evehunt.domain.event.dto.EventIdResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import org.springframework.data.domain.Page


interface QueryDslEventRepository {
    fun searchEvents(pageRequest: PageRequest): Page<EventCardResponse>
    fun setExpiredEventsClosed(): List<EventIdResponse>
    fun getPopularEvents(): List<EventCardResponse>
}