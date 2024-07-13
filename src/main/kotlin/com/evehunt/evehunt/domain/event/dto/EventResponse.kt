package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import java.time.LocalDateTime

data class EventResponse(
    val id: Long?,
    val title: String,
    val description: String,
    val winMessage: String,
    val question: String?,
    val eventImage: String?,
    val capacity: Int,
    val status: EventStatus,
    val hostId: Long?,
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
                id = event.id,
                title = event.title,
                description = event.description,
                winMessage = event.winMessage,
                question = event.question,
                capacity = event.capacity,
                status = event.eventStatus,
                hostId = event.host?.id,
                createdAt = event.createdAt.toLocalDateTime(),
                closeAt = event.closeAt.toLocalDateTime(),
                updatedAt = event.updatedAt.toLocalDateTime(),
                eventImage = event.image?.getLink()
            )
        }
    }
}
