package com.evehunt.evehunt.domain.participateHistory.dto

import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory

data class ParticipateResponse(
    val id: Long?,
    val eventId: Long?,
    val memberId: Long?,
    val answer: String,
    var status: EventParticipateStatus,
    val email: String?
)
{
    companion object
    {
        fun from(participateHistory: ParticipateHistory): ParticipateResponse
        {
            return ParticipateResponse(participateHistory.id, participateHistory.event.id,
                participateHistory.participant?.id, participateHistory.answer, participateHistory.status, participateHistory.participant?.email)
        }
    }
}
