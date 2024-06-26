package com.evehunt.evehunt.domain.participateHistory.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory

data class ParticipateRequest(
    val answer: String
)
{
    fun to(event: Event, member: Member?): ParticipateHistory
    {
        return ParticipateHistory(event, member, answer)
    }
}
