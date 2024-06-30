package com.evehunt.evehunt.global.infra.scheduler


import com.evehunt.evehunt.domain.event.service.EventService
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Scheduler(
    private val eventService: EventService
) {

    @Async
    @Scheduled(fixedRate = 1000)
    fun closeExpiredEvents()
    {
        eventService.setExpiredEventsClose()
    }
}