package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.EventType
import java.time.LocalDateTime

data class EventResponse(
    val title: String,
    val description: String,
    val winMessage: String,
    val eventImage: String?,
    val capacity: Int,
    val status: EventStatus,
    val eventType: EventType,
    val hostId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val closeAt: LocalDateTime
)
{
    companion object
    {
        fun from(event: Event): EventResponse
        {
            return EventResponse(
                title = event.title,
                description = event.description,
                winMessage = event.winMessage,
                capacity = event.capacity,
                status = event.eventStatus,
                eventType = event.eventType,
                hostId = event.host?.id!!,
                createdAt = event.createdAt.toLocalDateTime(),
                closeAt = event.closeAt.toLocalDateTime(),
                updatedAt = event.updatedAt.toLocalDateTime(),
                eventImage = event.image?.getLink()
            )
        }
    }
}
