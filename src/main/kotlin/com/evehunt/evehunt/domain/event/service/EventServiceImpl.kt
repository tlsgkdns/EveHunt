package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.global.common.PageRequest
import com.evehunt.evehunt.global.common.PageResponse
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository
): EventService {
    private fun getValidatedEvent(eventId: Long): Event
    {
        return eventRepository.findByIdOrNull(eventId) ?: throw ModelNotFoundException("Event", eventId.toString())
    }
    @Transactional
    override fun editEvent(eventId: Long, eventEditRequest: EventEditRequest): EventResponse {
        val event = getValidatedEvent(eventId)
        event.title = eventEditRequest.title
        event.winMessage = eventEditRequest.winMessage
        event.image = Image.from(eventEditRequest.eventImage)
        event.description = eventEditRequest.description
        event.closeAt = eventEditRequest.closeAt.atZone(ZoneId.of("Asia/Seoul"))
        return eventRepository.save(event).let { EventResponse.from(it) }
    }

    @Transactional
    override fun hostEvent(eventHostRequest: EventHostRequest): EventResponse {
        return eventHostRequest.to().let {
            EventResponse.from(it)
        }
    }

    @Transactional
    override fun getEvent(eventId: Long): EventResponse {
        return getValidatedEvent(eventId).let {
            EventResponse.from(it)
        }
    }

    @Transactional
    override fun getEvents(pageRequest: PageRequest, keyword: String?): PageResponse<EventResponse> {
        val eventPages = eventRepository.searchEvents(pageRequest.getPageable(), keyword)
        val content = eventPages.content.map { EventResponse.from(it) }
        return PageResponse.of(pageRequest, content, eventPages.totalElements.toInt())
    }

    @Transactional
    override fun closeEvent(eventId: Long): Long {
        val event = getValidatedEvent(eventId)
        eventRepository.delete(event)
        return eventId
    }
}