package com.evehunt.evehunt

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus
import com.evehunt.evehunt.domain.participant.service.ParticipantService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.exception.exception.InvalidEventException
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.random.Random

@SpringBootTest
@ActiveProfiles("test")
class EventServiceTests @Autowired constructor(
    val eventService: EventService,
    val participantService: ParticipantService
) {


    var eventId: Long? = 0L
    val eventCapacity = 1000
    companion object {
        const val memberNum = 300
        @JvmStatic
        @BeforeAll
        fun registerMemberAndHostEvent(
            @Autowired memberService: MemberService
        ) {
            for (i in 1..memberNum) {
                val memberRegisterRequest = MemberRegisterRequest(
                    email = i.toString().repeat(2) + "@naver.com",
                    name = Random.nextInt().toString(),
                    profileImageName = null,
                    password = i.toString()
                )
                memberService.registerMember(memberRegisterRequest)
            }
        }
    }
    @Test
    fun hostEvent()
    {
        val eventRequest = EventHostRequest(
            title = "Hello World!",
            description = "I am Here",
            eventImage = null,
            closeAt = LocalDateTime.now().plusDays(2),
            winMessage = "Winner Winner Chicken Dinner",
            question = "Do you like me?",
            capacity = eventCapacity,
            tagAddRequests = null
        )
        eventId = eventService.hostEvent(eventRequest, "11@naver.com").id
    }
    @Test
    fun getEvents()
    {
        eventService.getEvents(PageRequest())
    }
    @Test
    fun getEvent()
    {
        hostEvent()
        val event = eventService.getEvent(1)
        event.title shouldBe "Hello World!"
        event.winMessage shouldBe "Winner Winner Chicken Dinner"
    }

    @Test
    fun editEvent()
    {
        hostEvent()
        val eventEdit = EventEditRequest(
            description = "Hello New World!",
            title = "new Title",
            winMessage = "Winnnnnnnn",
            closeAt = null,
            eventImage = null,
            capacity = 300,
            question = "Hello",
            tagAddRequests = null
        )
        val event = eventService.getEvent(eventId)
        val edited = eventService.editEvent(eventId, eventEdit)
        edited.title shouldBe eventEdit.title
        edited.winMessage shouldBe eventEdit.winMessage
        edited.description shouldBe eventEdit.description
        edited.capacity shouldBe eventEdit.capacity
        edited.closedAt shouldBe event.closedAt
        edited.eventImage shouldBe event.eventImage
    }
    @Test
    fun closeEvent()
    {
        hostEvent()
        eventService.closeEvent(eventId)
        eventService.getEvent(eventId).status shouldBe EventStatus.CLOSED
    }
    @Test
    fun testParticipateEvent()
    {
        hostEvent()
        participantService.participateEvent(eventId, 1.toString().repeat(2) + "@naver.com", ParticipateRequest(answer = "Test"))
        val list = eventService.getParticipants(eventId)
        list.size shouldBe 1
        list[0].eventId shouldBe eventId
        list[0].memberId shouldBe 1L
    }

    @Test
    fun testPickWinner()
    {
        hostEvent()
        for(id in 1L .. memberNum) participantService.participateEvent(eventId, id.toString().repeat(2) + "@naver.com", ParticipateRequest(answer = "Winner Pick"))
        val winnerList:MutableList<Long> = mutableListOf()
        for(id in 1L .. memberNum step 2) winnerList.add(id)
        val list = eventService.setEventResult(eventId, EventWinnerRequest(winnerList, winnerList.map { it.toString() }))
        for(id in 1 .. memberNum)
        {
            if(id % 2 > 0) list[id - 1].status shouldBe ParticipantStatus.WIN
            else list[id - 1].status shouldBe ParticipantStatus.LOSE
        }
    }

    @Test
    fun testMultiThreadEnvironment()
    {
        hostEvent()
        val threadCount = memberNum
        val executorService = Executors.newFixedThreadPool(32)
        val countDownLatch = CountDownLatch(threadCount)

        for(id in 1L .. threadCount)
        {
            executorService.execute(){
                try{
                    participantService.participateEvent(eventId, id.toString().repeat(2) + "@naver.com", ParticipateRequest(answer = "Winner Pick"))
                } finally {
                    countDownLatch.countDown()
                }
            }
        }
        countDownLatch.await()
        val list = participantService.getParticipantsByEvent(1L)
        list.size shouldBe 70
    }
    @Test
    fun testExpiredEvent()
    {
        val eventRequest = EventHostRequest(
            title = "Hello World!",
            description = "I am Here",
            eventImage = null,
            closeAt = LocalDateTime.now().plusSeconds(3),
            winMessage = "Winner Winner Chicken Dinner",
            question = "Do you like me?",
            capacity = 700,
            tagAddRequests = null
        )
        val eId = eventService.hostEvent(eventRequest,  "11@naver.com").id
        for (id in 3 until memberNum)  participantService.participateEvent(eId, id.toString().repeat(2) + "@naver.com", ParticipateRequest(answer = "Winner Pick"))
        Thread.sleep(5001)
        eventService.getEvent(1L).status shouldBe EventStatus.CLOSED
        assertThrows<InvalidEventException> {
            participantService.participateEvent(eId, "22@naver.com", ParticipateRequest(answer = "Winner Pick"))
        }
        val list = participantService.getParticipantsByEvent(1L)
        list.size shouldBe memberNum - 3
        list.forEach { it.status shouldBe ParticipantStatus.WAIT_RESULT }
    }


}