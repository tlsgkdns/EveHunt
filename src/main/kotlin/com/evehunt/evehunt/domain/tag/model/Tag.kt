package com.evehunt.evehunt.domain.tag.model

import com.evehunt.evehunt.domain.event.model.Event
import jakarta.persistence.*

@Entity
class Tag (
    @ManyToOne
    private val event: Event,
    private val tagName: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}