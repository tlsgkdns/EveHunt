package com.evehunt.evehunt.domain.participant.model.strategy

import com.evehunt.evehunt.domain.participant.model.Participant
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus

class PickWinnerBinarySearch: PickWinnerStrategy {
    override fun pick(participantList: List<Participant>, winnerList: List<Long>): List<Participant> {
        val sortedParticipant = participantList.sortedBy { it.id }
        val sortedId = participantList.map { it.id }
        for(winner in winnerList)
        {
            sortedParticipant.get(sortedId.binarySearch(winner)).status = ParticipantStatus.WIN
        }
        for (participant in participantList)
            if(participant.status != ParticipantStatus.WIN)
                participant.status = ParticipantStatus.LOSE
        return sortedParticipant
    }
}