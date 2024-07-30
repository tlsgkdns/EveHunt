package com.evehunt.evehunt

import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.service.ParticipateHistoryService
import com.evehunt.evehunt.domain.report.dto.ReportRequest
import com.evehunt.evehunt.domain.report.service.ReportService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.random.Random

@SpringBootTest
class EveHuntApplicationTests @Autowired constructor(
    val memberService: MemberService,
    val eventService: EventService,
    val participateHistoryService: ParticipateHistoryService,
    val reportService: ReportService
) {
    val memberNum = 1000
    @Test
    fun contextLoads() {

    }
    fun getRandomUsername(): String
    {
        return "${Random.nextInt(1, memberNum + 1)}@gmail.com"
    }
    @Test
    fun insertData()
    {
        for (i in 1 .. memberNum)
        {
            try {
                participateHistoryService.participateEvent(
                    Random.nextLong(1, memberNum + 1L),
                    username = getRandomUsername(), participateRequest = ParticipateRequest(UUID.randomUUID().toString())
                )
            } catch (e: Exception) {

            }

        }
        for (i in 1 .. memberNum / 10)
        {
            reportService.createReport(ReportRequest(Random.nextLong(1, memberNum + 1L)
                , "I hate $i"), getRandomUsername())
        }
    }
}
