package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.dto.EventCardResponse
import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.global.common.page.PageRequest
import org.springframework.data.domain.Page


interface QueryDslEventRepository {
    fun searchEvents(pageRequest: PageRequest): Page<EventCardResponse>
    fun getExpiredEvents(): List<Event>
    fun getPopularEvents(): List<Event>

}