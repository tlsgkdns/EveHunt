package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.EventType
import com.evehunt.evehunt.domain.image.model.Image
import java.time.LocalDateTime
import java.time.ZoneId

data class EventHostRequest(
    val title: String,
    val description: String,
    val winMessage: String,
    val eventImage: String,
    val capacity: Int,
    val status: EventStatus,
    val eventType: EventType,
    val closeAt: LocalDateTime
)
{
    fun to(): Event
    {
        return Event(
            title = title,
            description = description,
            winMessage = winMessage,
            capacity = capacity,
            eventType = eventType,
            eventStatus = EventStatus.PROCEED,
            closeAt = closeAt.atZone(ZoneId.of("Asia/Seoul")),
            image = Image.from(eventImage)
        )
    }
}
