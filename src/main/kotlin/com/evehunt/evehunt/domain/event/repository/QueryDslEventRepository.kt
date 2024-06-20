package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.model.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface QueryDslEventRepository {

    fun searchEvents(pageRequest: Pageable, keyword: String?): Page<Event>
}