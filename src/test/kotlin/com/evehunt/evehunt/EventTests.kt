package com.evehunt.evehunt

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.EventType
import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.dto.MemberSignInRequest
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.service.ParticipateHistoryService
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
class EventTests @Autowired constructor(
    val memberService: MemberService,
    val mockMvc: MockMvc,
    val eventService: EventService,
    val participateHistoryService: ParticipateHistoryService
) {
    val objectMapper: ObjectMapper = ObjectMapper().registerModules(JavaTimeModule())
    companion object
    {
        @JvmStatic
        @BeforeAll
        fun registerMember(@Autowired memberService: MemberService)
        {
            memberService.deleteAllMember()
            for (i in 1..100) {
                val email = i.toString()
                val memberRegisterRequest = MemberRegisterRequest(
                    email = email,
                    name =  Random.nextInt().toString(),
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
            capacity = 100,
            eventType = EventType.DRAWLOT,
        )
        eventService.hostEvent(eventRequest, "1").hostId shouldBe 1L

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
            capacity = 300
        )
        val event = eventService.getEvent(1)
        val edited = eventService.editEvent(1, eventEdit)
        edited.title shouldBe eventEdit.title
        edited.winMessage shouldBe eventEdit.winMessage
        edited.description shouldBe eventEdit.description
        edited.capacity shouldBe eventEdit.capacity
        edited.closeAt shouldBe event.closeAt
        edited.eventImage shouldBe event.eventImage
    }
    @Test
    fun closeEvent()
    {
        hostEvent()
        eventService.closeEvent(1)
        shouldThrow<ModelNotFoundException> {
            eventService.getEvent(1)
        }
    }

    fun participateEvent(id: Long): List<ParticipateResponse>
    {
        val loginRequest = MemberSignInRequest(
            email = id.toString(),
            password = id.toString()
        )
        val eventId = 1L
        val jwt = memberService.signIn(loginRequest).token
        val participateJson = objectMapper.writeValueAsString(ParticipateRequest("I am here!"))
        mockMvc.perform(post("/participate-history/events/${eventId}")
            .header("Authorization", "Bearer $jwt")
            .contentType(MediaType.APPLICATION_JSON)
            .content(participateJson))
        return participateHistoryService.getParticipateHistoryByEvent(eventId)
    }
    @Test
    fun testParticipateEvent()
    {
        hostEvent()
        val eventId = 1L
        val list = participateEvent(eventId)
        list.size shouldBe 1
        list[0].eventId shouldBe eventId
        list[0].memberId shouldBe 1
    }

    @Test
    fun testPickWinner()
    {
        hostEvent()
        val eventId = 1L
        for(id in 1L .. 100L) participateEvent(id)
        val winnerList:MutableList<Long> = mutableListOf()
        for(id in 1L .. 100L step 2) winnerList.add(id)
        val list = participateHistoryService.setEventResult(eventId, EventWinnerRequest(winnerList))
        for(id in 1 .. 100)
        {
            if(id % 2 > 0) list[id - 1].status shouldBe EventParticipateStatus.WIN
            else list[id - 1].status shouldBe EventParticipateStatus.LOSE
        }
    }

    @Test
    fun testMultiThreadEnvironment()
    {
        hostEvent()
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val countDownLatch = CountDownLatch(threadCount)

        for(id in 1L .. threadCount)
        {
            val loginRequest = MemberSignInRequest(
                email = id.toString(),
                password = id.toString()
            )
            val eventId = 1L
            val jwt = memberService.signIn(loginRequest).token
            val participateJson = objectMapper.writeValueAsString(ParticipateRequest("I am here!"))
            executorService.execute(){
                try{
                    mockMvc.perform(post("/participate-history/events/${eventId}")
                        .header("Authorization", "Bearer $jwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(participateJson))
                } finally {
                    countDownLatch.countDown()
                }
            }
        }
        countDownLatch.await()
        val list = participateHistoryService.getParticipateHistoryByEvent(1L)
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
            eventType = EventType.DRAWLOT,
        )
        val loginRequest = MemberSignInRequest(
            email = "1",
            password = "1"
        )
        val jwt = memberService.signIn(loginRequest).token
        val eventJson = objectMapper.writeValueAsString(eventRequest)
        mockMvc.perform(post("/events")
            .header("Authorization", "Bearer $jwt")
            .contentType(MediaType.APPLICATION_JSON)
            .content(eventJson))
            .andExpect(MockMvcResultMatchers.status().isOk)
        Thread.sleep(5001)
        eventService.getEvent(1L).status shouldBe EventStatus.CLOSED
        participateEvent(1L)
        participateHistoryService.getParticipateHistoryByEvent(1L).size shouldBe 0
    }

    @Test
    fun testLoginCheckAnnotation()
    {
        hostEvent()
        val loginRequest = MemberSignInRequest(
            email = "4",
            password = "4"
        )
        val jwt = memberService.signIn(loginRequest).token
        val eventEditRequest = EventEditRequest(
            description = "Hello New World!",
            title = "new Title",
            winMessage = "Winnnnnnnn",
            closeAt = null,
            eventImage = null,
            capacity = 300
        )
        val eventEditJson = objectMapper.writeValueAsString(eventEditRequest)
        mockMvc.perform(patch("/events/1")
            .header("Authorization", "Bearer $jwt")
            .contentType(MediaType.APPLICATION_JSON)
            .content(eventEditJson))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
        eventService.getEvent(1L).title shouldNotBe eventEditRequest.title
    }
}