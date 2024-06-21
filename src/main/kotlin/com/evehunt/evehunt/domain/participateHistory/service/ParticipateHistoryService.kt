package com.evehunt.evehunt.domain.participateHistory.service

import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.global.common.PageRequest
import com.evehunt.evehunt.global.common.PageResponse

interface ParticipateHistoryService {
    fun participateEvent(eventId: Long, memberEmail: String, participateRequest: ParticipateRequest): ParticipateResponse
    fun dropEventParticipate(eventId: Long, memberEmail: String)
    fun setEventResult(eventId: Long, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse>
    fun getParticipateHistoryByMember(pageRequest: PageRequest, memberEmail: String): PageResponse<ParticipateResponse>
    fun getParticipateHistoryByEvent(eventId: Long): List<ParticipateResponse>
}