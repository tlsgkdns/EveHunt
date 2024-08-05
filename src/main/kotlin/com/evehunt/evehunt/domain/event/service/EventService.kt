package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.*
import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateResponse
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface EventService {
    fun editEvent(eventId: Long?, eventEditRequest: EventEditRequest): EventResponse
    fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse
    fun getEvent(eventId: Long?): EventResponse
    fun getEvents(pageRequest: PageRequest): PageResponse<EventCardResponse>
    fun setExpiredEventsClose(): List<EventIdResponse>
    fun participateEvent(eventId: Long?, username: String, participateRequest: ParticipateRequest): ParticipateResponse
    fun resignEventParticipate(eventId: Long?, username: String)
    fun getPopularEvent(): List<EventCardResponse>
    fun setEventResult(eventId: Long?, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse>
    fun getParticipants(eventId: Long?): List<ParticipateResponse>
    fun getParticipant(eventId: Long?, username: String): ParticipateResponse
    fun getTags(eventId: Long?): List<TagResponse>
    fun closeEvent(eventId: Long?): EventResponse
    fun addTag(eventId: Long?, tagAddRequest: TagAddRequest): TagResponse
    fun deleteTag(eventId: Long?, tagId: Long?)
}