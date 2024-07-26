package com.evehunt.evehunt.global.infra.scheduler


import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.service.MemberService
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Scheduler(
    private val eventService: EventService,
    private val memberService: MemberService
) {

    @Async
    @Scheduled(fixedRate = 1000)
    fun closeExpiredEvents()
    {
        eventService.setExpiredEventsClose()
    }

    @Async
    @Scheduled(cron = "0 0 6 * * *")
    fun cancelExpiredSuspend()
    {
        memberService.cancelExpiredSuspend()
    }
}