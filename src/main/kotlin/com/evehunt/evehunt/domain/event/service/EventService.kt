package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface EventService {
    fun editEvent(eventId: Long?, eventEditRequest: EventEditRequest): EventResponse
    fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse
    fun getEvent(eventId: Long?): EventResponse
    fun getEvents(pageRequest: PageRequest): PageResponse<EventResponse>
    fun deleteEvent(eventId: Long?): Long?
    fun setExpiredEventsClose(): List<EventResponse>
    fun participateEvent(eventId: Long?, username: String, participateRequest: ParticipateRequest): ParticipateResponse
    fun resignEventParticipate(eventId: Long?, username: String)
    fun getPopularEvent(): List<EventResponse>
    fun setEventResult(eventId: Long?, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse>
    fun getParticipateHistories(eventId: Long?): List<ParticipateResponse>
    fun getParticipateHistory(eventId: Long?, username: String): ParticipateResponse
    fun getTags(eventId: Long?): List<TagResponse>
    fun closeEvent(eventId: Long?): EventResponse
    fun addTag(eventId: Long?, tagAddRequest: TagAddRequest): TagResponse
    fun deleteTag(eventId: Long?, tagId: Long?)
}