package com.evehunt.evehunt.domain.participateHistory.dto

import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory

data class ParticipateResponse(
    val eventId: Long?,
    val memberId: Long?,
    val status: EventParticipateStatus
)
{
    companion object
    {
        fun from(participateHistory: ParticipateHistory): ParticipateResponse
        {
            return ParticipateResponse(participateHistory.event.id, participateHistory.participant?.id, participateHistory.status)
        }
    }
}