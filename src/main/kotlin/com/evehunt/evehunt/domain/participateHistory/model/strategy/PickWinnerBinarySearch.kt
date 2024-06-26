package com.evehunt.evehunt.domain.participateHistory.model.strategy

import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory

class PickWinnerBinarySearch: PickWinnerStrategy {
    override fun pick(participantList: List<ParticipateHistory>, winnerList: List<Long>): List<ParticipateHistory> {
        val sortedParticipant = participantList.sortedBy { it.id }
        val sortedId = participantList.map { it.id }
        for(winner in winnerList)
        {
            sortedParticipant.get(sortedId.binarySearch(winner)).status = EventParticipateStatus.WIN
        }
        for (participant in participantList)
            if(participant.status != EventParticipateStatus.WIN)
                participant.status = EventParticipateStatus.LOSE
        return participantList
    }
}