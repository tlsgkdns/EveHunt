package com.evehunt.evehunt.domain.participant.dto

data class EventWinnerRequest(
    val eventWinners: List<Long>,
    val winMessages: List<String>
)
