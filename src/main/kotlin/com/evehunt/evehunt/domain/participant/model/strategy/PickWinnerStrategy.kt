package com.evehunt.evehunt.domain.participant.model.strategy

import com.evehunt.evehunt.domain.participant.model.Participant

interface PickWinnerStrategy {
    fun pick(participantList: List<Participant>, winnerList: List<Long>): List<Participant>
}