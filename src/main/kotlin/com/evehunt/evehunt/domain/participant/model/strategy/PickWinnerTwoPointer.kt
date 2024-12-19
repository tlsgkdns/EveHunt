package com.evehunt.evehunt.domain.participant.model.strategy

import com.evehunt.evehunt.domain.participant.model.Participant
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus

class PickWinnerTwoPointer : PickWinnerStrategy{
    override fun pick(participantList: List<Participant>, winnerList: List<Long>): List<Participant> {
        val sortedParticipant = participantList.sortedBy { it.id }
        val sortedId = participantList.map { it.id }
        var winnerIdx = 0
        for((idx, id) in sortedId.withIndex())
        {
            if(winnerIdx >= winnerList.size || winnerList[winnerIdx] != id)
                sortedParticipant[idx].status = ParticipantStatus.LOSE
            else {
                sortedParticipant[idx].status = ParticipantStatus.WIN
                winnerIdx++
            }
        }
        return sortedParticipant
    }
}