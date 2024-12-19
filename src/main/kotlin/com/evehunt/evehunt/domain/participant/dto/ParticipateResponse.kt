package com.evehunt.evehunt.domain.participant.dto

import com.evehunt.evehunt.domain.participant.model.Participant
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus

data class ParticipateResponse(
    val id: Long?,
    val eventId: Long?,
    val memberId: Long?,
    val answer: String,
    var status: ParticipantStatus,
    val email: String?
)
{
    companion object
    {
        fun from(participant: Participant): ParticipateResponse
        {
            return ParticipateResponse(participant.id, participant.event.id,
                participant.participant?.id, participant.answer, participant.status, participant.participant?.email)
        }
    }
}
