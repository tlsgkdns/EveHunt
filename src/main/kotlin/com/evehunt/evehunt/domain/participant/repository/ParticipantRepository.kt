package com.evehunt.evehunt.domain.participant.repository

import com.evehunt.evehunt.domain.participant.model.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository: JpaRepository<Participant, Long>,
    QueryDslParticipantRepository{

}