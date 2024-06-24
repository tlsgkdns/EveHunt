package com.evehunt.evehunt.domain.event.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class EventEditRequest(
    val title: String?,
    val description: String?,
    val winMessage: String?,
    val eventImage: String?,
    val capacity: Int?,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val closeAt: LocalDateTime?
)
