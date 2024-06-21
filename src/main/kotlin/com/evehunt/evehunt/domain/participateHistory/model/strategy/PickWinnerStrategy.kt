package com.evehunt.evehunt.domain.participateHistory.model.strategy

import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory

interface PickWinnerStrategy {
    fun pick(participantList: List<ParticipateHistory>, winnerList: List<Long>): List<ParticipateHistory>
}