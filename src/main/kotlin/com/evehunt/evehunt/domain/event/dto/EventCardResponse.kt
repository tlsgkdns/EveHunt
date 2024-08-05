package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.image.model.Image
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class EventCardResponse(
    val id: Long?,
    val hostName: String,
    val title: String,
    val capacity: Int,
    val closeAt: LocalDateTime,
    val participantCount: Int,
    val eventImage: String? = null,
)
{
    constructor(id: Long?, hostName: String, title: String, capacity: Int, closeAt: ZonedDateTime, participantCount: Long, image: Image? = null)
            : this(id, hostName, title, capacity, closeAt.toLocalDateTime(), participantCount.toInt(), image?.getLink())
}