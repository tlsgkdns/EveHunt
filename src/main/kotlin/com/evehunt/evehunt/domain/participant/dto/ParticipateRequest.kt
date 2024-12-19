package com.evehunt.evehunt.domain.participant.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.participant.model.Participant

data class ParticipateRequest(
    val answer: String
)
{
    fun to(event: Event, member: Member?): Participant
    {
        return Participant(event, member, answer)
    }
}
