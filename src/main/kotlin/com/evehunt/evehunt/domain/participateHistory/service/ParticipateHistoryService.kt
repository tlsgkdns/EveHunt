package com.evehunt.evehunt.domain.participateHistory.service

import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateEditRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface ParticipateHistoryService {
    fun participateEvent(eventId: Long, username: String, participateRequest: ParticipateRequest): ParticipateResponse
    fun resignEventParticipate(eventId: Long, username: String)
    fun setEventResult(eventId: Long, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse>
    fun getParticipateHistoryByMember(pageRequest: PageRequest, username: String): PageResponse<ParticipateResponse>
    fun getParticipateHistoryByEvent(eventId: Long?): List<ParticipateResponse>
    fun getParticipateHistory(eventId: Long, username: String): ParticipateResponse
    fun editParticipateAnswer(eventId: Long, participateEditRequest: ParticipateEditRequest, username: String): ParticipateResponse
    fun setParticipantsStatusWait(eventId: Long?): List<ParticipateResponse>
}