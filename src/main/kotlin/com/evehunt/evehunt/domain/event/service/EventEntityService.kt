package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface EventEntityService {
    fun editEvent(eventId: Long, eventEditRequest: EventEditRequest): EventResponse
    fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse
    fun getEvent(eventId: Long): EventResponse
    fun getEvents(pageRequest: PageRequest): PageResponse<EventResponse>
    fun closeEvent(eventId: Long): Long
    fun getPopularEvent(): List<EventResponse>
    fun setExpiredEventsClose(): List<EventResponse>
}