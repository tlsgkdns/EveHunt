package com.evehunt.evehunt.domain.participant.service

import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateEditRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface ParticipantService {
    fun participateEvent(eventId: Long?, username: String, participateRequest: ParticipateRequest): ParticipateResponse
    fun resignEventParticipate(eventId: Long?, username: String)
    fun setEventResult(eventId: Long?, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse>
    fun getParticipateHistoryByMember(username: String, pageRequest: PageRequest): PageResponse<ParticipateResponse>
    fun getParticipantsByEvent(eventId: Long?):List<ParticipateResponse>
    fun getParticipant(eventId: Long?, username: String): ParticipateResponse
    fun editParticipateAnswer(eventId: Long?, participateEditRequest: ParticipateEditRequest, username: String): ParticipateResponse
    fun setParticipantsStatusWait(eventId: Long?): List<ParticipateResponse>
    fun getParticipantCount(eventId: Long?): Int
}