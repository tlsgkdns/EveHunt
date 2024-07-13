package com.evehunt.evehunt.domain.event.model

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
class Event(
    var title: String,
    var description: String,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var image: Image?,
    var capacity: Int,
    var eventStatus: EventStatus,
    var winMessage: String,
    var question: String?,
    var closeAt: ZonedDateTime,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var host: Member? = null
): BaseTimeEntity()
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


}

