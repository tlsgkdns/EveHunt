package com.evehunt.evehunt.domain.participateHistory.model

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
class ParticipateHistory(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val event: Event,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var participant: Member? = null,
    val answer: String,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var status: EventParticipateStatus = EventParticipateStatus.PARTICIPATING
}