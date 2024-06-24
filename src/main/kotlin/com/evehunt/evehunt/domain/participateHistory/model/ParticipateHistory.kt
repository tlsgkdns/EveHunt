package com.evehunt.evehunt.domain.participateHistory.model

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
class ParticipateHistory(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val event: Event,
    val answer: String,
): BaseTimeEntity() {

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var participant: Member? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var status: EventParticipateStatus = EventParticipateStatus.PARTICIPATING
}