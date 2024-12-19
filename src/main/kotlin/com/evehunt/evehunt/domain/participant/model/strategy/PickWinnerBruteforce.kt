package com.evehunt.evehunt.domain.participant.model.strategy

import com.evehunt.evehunt.domain.participant.model.Participant
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus

class PickWinnerBruteforce: PickWinnerStrategy {
    override fun pick(participantList: List<Participant>, winnerList: List<Long>): List<Participant> {
        for(winner in winnerList)
        {
            for (participant in participantList)
            {
                if(winner == participant.participant?.id)
                {
                    participant.status = ParticipantStatus.WIN
                    break
                }
            }
        }
        for (participant in participantList)
            if(participant.status != ParticipantStatus.WIN)
                participant.status = ParticipantStatus.LOSE
        return participantList
    }
}