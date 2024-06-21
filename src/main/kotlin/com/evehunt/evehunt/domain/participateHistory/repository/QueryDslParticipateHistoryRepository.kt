package com.evehunt.evehunt.domain.participateHistory.repository

import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QueryDslParticipateHistoryRepository {
    fun getParticipateHistory(eventId: Long, email: String): ParticipateHistory?
    fun getParticipantsByEvent(eventId: Long): List<ParticipateHistory>
    fun getParticipantsByMember(pageRequest: Pageable, email: String): Page<ParticipateHistory>
}