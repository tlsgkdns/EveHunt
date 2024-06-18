package com.evehunt.evehunt.domain.participateHistory.model

import com.evehunt.evehunt.domain.event.model.Event
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class ParticipateHistory(
    @ManyToOne
    private val event: Event,
    private val answer: String
) {

    @Id
    private val id: Long? = null
}