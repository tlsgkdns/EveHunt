package com.evehunt.evehunt.domain.participateHistory.repository

import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory
import com.evehunt.evehunt.global.common.NamedLockWithJpaRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipateHistoryRepository: JpaRepository<ParticipateHistory, Long>,
    QueryDslParticipateHistoryRepository, NamedLockWithJpaRepository{

}