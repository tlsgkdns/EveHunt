package com.evehunt.evehunt.domain.tag.model

import com.evehunt.evehunt.domain.event.model.Event
import jakarta.persistence.*

@Entity
class Tag (
    @ManyToOne(fetch = FetchType.LAZY)
    var event: Event,
    var tagName: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}