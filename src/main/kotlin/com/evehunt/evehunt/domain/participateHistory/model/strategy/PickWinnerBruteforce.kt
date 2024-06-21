package com.evehunt.evehunt.domain.participateHistory.model.strategy

import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory

class PickWinnerBruteforce: PickWinnerStrategy {
    override fun pick(participantList: List<ParticipateHistory>, winnerList: List<Long>): List<ParticipateHistory> {
        for(winner in winnerList)
        {
            for (participant in participantList)
            {
                if(winner == participant.participant?.id)
                {
                    participant.status = EventParticipateStatus.WIN
                    break
                }
            }
        }
        for (participant in participantList)
            if(participant.status != EventParticipateStatus.WIN)
                participant.status = EventParticipateStatus.LOSE
        return participantList
    }
}