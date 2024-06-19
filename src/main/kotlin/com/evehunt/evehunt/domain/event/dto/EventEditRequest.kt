package com.evehunt.evehunt.domain.event.dto

import java.time.LocalDateTime

data class EventEditRequest(
    val title: String,
    val description: String,
    val winMessage: String,
    val eventImage: String?,
    val capacity: Int,
    val closeAt: LocalDateTime
)
