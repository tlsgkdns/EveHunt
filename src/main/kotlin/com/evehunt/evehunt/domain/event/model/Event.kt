package com.evehunt.evehunt.domain.event.model

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import java.time.ZonedDateTime

@Entity
class Event(
    var title: String,
    var description: String,
    @OneToOne(fetch = FetchType.LAZY)
    var image: Image?,
    var capacity: Int,
    var eventStatus: EventStatus,
    var winMessage: String,
    var question: String?,
    var closeAt: ZonedDateTime,
    var eventType: EventType
): BaseTimeEntity()
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var host: Member? = null
}

