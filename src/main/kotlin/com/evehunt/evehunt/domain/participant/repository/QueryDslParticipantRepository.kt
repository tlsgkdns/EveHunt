package com.evehunt.evehunt.domain.participant.repository

import com.evehunt.evehunt.domain.participant.model.Participant
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QueryDslParticipantRepository {
    fun getParticipant(eventId: Long?, email: String): Participant?
    fun getParticipantsByEvent(eventId: Long?): List<Participant>
    fun getParticipantsByMember(pageRequest: Pageable, email: String): Page<Participant>
}