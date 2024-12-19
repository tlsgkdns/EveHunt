package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class EventEditRequest(
    val title: String?,
    val description: String?,
    val winMessage: String?,
    val eventImage: String?,
    val capacity: Int?,
    val question: String?,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val closeAt: LocalDateTime?,
    val tagAddRequests: List<TagAddRequest>?
)
