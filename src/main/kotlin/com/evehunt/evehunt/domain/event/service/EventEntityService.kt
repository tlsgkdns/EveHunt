package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.*
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface EventEntityService {
    fun editEvent(eventId: Long?, eventEditRequest: EventEditRequest): EventResponse
    fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse
    fun getEvent(eventId: Long?): EventResponse
    fun getEvents(pageRequest: PageRequest): PageResponse<EventCardResponse>
    fun deleteEvent(eventId: Long?): Long?
    fun getPopularEvent(): List<EventCardResponse>
    fun setExpiredEventsClose(): List<EventIdResponse>
    fun closeEvent(eventId: Long?): EventResponse
}