package com.evehunt.evehunt.domain.tag.model

import com.evehunt.evehunt.domain.event.model.Event
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Tag (
    @ManyToOne
    private val event: Event,
    private val tagName: String
){
    @Id
    private val id: Long? = null
}