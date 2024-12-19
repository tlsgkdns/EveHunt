package com.evehunt.evehunt

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.dto.MemberSignInRequest
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.service.ParticipantService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTests @Autowired constructor(
    val memberService: MemberService,
    val mockMvc: MockMvc,
    val eventService: EventService,
    val participantService: ParticipantService
)  {
    companion object {
        const val memberNum = 100
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
    val objectMapper: ObjectMapper = ObjectMapper().registerModules(JavaTimeModule())
    fun hostEvent()
    {
        val eventRequest = EventHostRequest(
            title = "Hello World!",
            description = "I am Here",
            eventImage = null,
            closeAt = LocalDateTime.now().plusDays(2),
            winMessage = "Winner Winner Chicken Dinner",
            question = "Do you like me?",
            capacity = 1000,
            tagAddRequests = null
        )
        eventService.hostEvent(eventRequest, "11@naver.com")
    }
    @Test
    fun testLoginCheckAnnotation()
    {
        hostEvent()
        val loginRequest = MemberSignInRequest(
            email = "44@naver.com",
            password = "4"
        )
        val jwt = memberService.signIn(loginRequest).token
        val eventEditRequest = EventEditRequest(
            description = "Hello New World!",
            title = "new Title",
            winMessage = "Winnnnnnnn",
            closeAt = null,
            eventImage = null,
            capacity = 300,
            question = "Hello",
            tagAddRequests = null
        )
        val eventEditJson = objectMapper.writeValueAsString(eventEditRequest)
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/events/1")
            .header("Authorization", "Bearer $jwt")
            .contentType(MediaType.APPLICATION_JSON)
            .content(eventEditJson))
            .andExpect(MockMvcResultMatchers.status().isForbidden)
        eventService.getEvent(1L).title shouldNotBe eventEditRequest.title
    }
    @Test
    fun testUnAuthorizedPickWinner()
    {
        hostEvent()
        val eventId = 1L
        for(id in 2L ..memberNum)  participantService.participateEvent(eventId, id.toString().repeat(2) + "@naver.com", ParticipateRequest(answer = "Winner Pick"))
        val winnerList:MutableList<Long> = mutableListOf()
        for(id in 2L ..memberNum step 2) winnerList.add(id)
        val loginAnotherMemberRequest = MemberSignInRequest(
            email = "3",
            password = "3"
        )
        val jwt = memberService.signIn(loginAnotherMemberRequest).token
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/events/${eventId}/participants/result")
                .header("Authorization", "Bearer $jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(EventWinnerRequest(winnerList, winnerList.map { it.toString() }))))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}