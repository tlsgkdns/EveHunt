package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.global.common.PageRequest
import com.evehunt.evehunt.global.common.PageResponse

interface EventService {
    fun editEvent(eventId: Long, eventEditRequest: EventEditRequest): EventResponse
    fun hostEvent(eventHostRequest: EventHostRequest): EventResponse
    fun getEvent(eventId: Long): EventResponse
    fun getEvents(pageRequest: PageRequest, keyword: String?): PageResponse<EventResponse>
    fun closeEvent(eventId: Long): Long
}