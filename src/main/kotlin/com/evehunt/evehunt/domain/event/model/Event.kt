package com.evehunt.evehunt.domain.event.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Event(
    private val title: String,
    private val description: String,
    private val image: String,
    private val capacity: Int,
    private val eventStatus: EventStatus
)
{
    @Id
    private val id: Long? = null
}

