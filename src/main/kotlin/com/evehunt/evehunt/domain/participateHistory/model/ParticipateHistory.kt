package com.evehunt.evehunt.domain.participateHistory.model

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
class ParticipateHistory(
    @ManyToOne
    val event: Event,
    val answer: String,
) {

    @CreatedBy
    @ManyToOne
    val participant: Member? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}