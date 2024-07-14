package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.time.ZoneId

data class EventHostRequest(
    val title: String,
    val description: String,
    val winMessage: String,
    val eventImage: String?,
    val capacity: Int,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val closeAt: LocalDateTime,
    val question: String?,
    val tagAddRequests: List<TagAddRequest>?
)
{
    fun to(member: Member?): Event
    {
        return Event(
            title = title,
            description = description,
            winMessage = winMessage,
            capacity = capacity,
            eventStatus = EventStatus.PROCEED,
            closeAt = closeAt.atZone(ZoneId.of("Asia/Seoul")),
            image = Image.from(eventImage),
            question = question,
            host = member
        )
    }
}
