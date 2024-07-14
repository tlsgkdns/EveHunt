package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId

@Service
class EventEntityServiceImpl(
    private val eventRepository: EventRepository,
    private val memberRepository: MemberRepository
): EventEntityService {
    private fun getExistEvent(eventId: Long): Event
    {
        return eventRepository.findByIdOrNull(eventId) ?: throw ModelNotFoundException("Event", eventId.toString())
    }

    @Transactional
    override fun editEvent(eventId: Long, eventEditRequest: EventEditRequest): EventResponse {
        val event = getExistEvent(eventId)
        event.title = eventEditRequest.title ?: event.title
        event.winMessage = eventEditRequest.winMessage ?: event.winMessage
        event.image = Image.from(eventEditRequest.eventImage)
        event.description = eventEditRequest.description ?: event.description
        event.closeAt = eventEditRequest.closeAt?.atZone(ZoneId.of("Asia/Seoul")) ?: event.closeAt
        event.capacity = eventEditRequest.capacity ?: event.capacity
        return eventRepository.save(event).let { EventResponse.from(it) }
    }

    @Transactional
    override fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse {
        val member = memberRepository.findMemberByEmail(username)
        return eventRepository.save(eventHostRequest.to(member)).let {
            EventResponse.from(it)
        }
    }

    @Transactional
    override fun getEvent(eventId: Long): EventResponse {
        return getExistEvent(eventId).let {
            EventResponse.from(it)
        }
    }

    @Transactional
    override fun getEvents(pageRequest: PageRequest): PageResponse<EventResponse> {
        val eventPages = eventRepository.searchEvents(pageRequest)
        val content = eventPages.content.map { EventResponse.from(it) }
        return PageResponse.of(pageRequest, content, eventPages.totalElements.toInt())
    }

    @Transactional
    override fun closeEvent(eventId: Long): Long {
        val event = getExistEvent(eventId)
        eventRepository.delete(event)
        return eventId
    }

    @Transactional
    override fun setExpiredEventsClose(): List<EventResponse> {
        val eventList = eventRepository.getExpiredEvents()
        val list = mutableListOf<EventResponse>()
        for (event in eventList)
        {
            event.eventStatus = EventStatus.CLOSED
            list.add(EventResponse.from(event))
            eventRepository.save(event)
        }
        return list
    }

    @Transactional
    override fun getPopularEvent(): List<EventResponse> {
        return eventRepository.getPopularEvents().map {
            EventResponse.from(it)
        }
    }
}