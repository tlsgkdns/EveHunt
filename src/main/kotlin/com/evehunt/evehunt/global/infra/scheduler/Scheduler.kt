package com.evehunt.evehunt.global.infra.scheduler


import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.mail.service.MailService
import com.evehunt.evehunt.domain.member.service.MemberService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Scheduler(
    private val eventService: EventService,
    private val memberService: MemberService,
    private val mailService: MailService
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


    @Async
    @Scheduled(cron = "0 0 6 * * *")
    @CacheEvict(value = ["popularTags", "popularEvents"])
    fun clearCache()
    {

    }
    @Scheduled(fixedDelay = 20000)
    fun sendMails()
    {
        mailService.sendMails()
    }
}