package com.evehunt.evehunt

import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.model.EventType
import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.dto.MemberSignInRequest
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.repository.TagRepository
import com.evehunt.evehunt.domain.tag.service.TagService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import kotlin.random.Random
@SpringBootTest
@AutoConfigureMockMvc
class TagTests @Autowired constructor(
    private val tagService: TagService,
    private val tagRepository: TagRepository
) {
    companion object
    {
        private val objectMapper: ObjectMapper = ObjectMapper().registerModules(JavaTimeModule())

        @JvmStatic
        @BeforeAll
        fun registerMemberAndHostEvent(@Autowired memberService: MemberService,
                                       @Autowired mockMvc: MockMvc)
        {
            memberService.deleteAllMember()
            for (i in 1..5) {
                val memberRegisterRequest = MemberRegisterRequest(
                    email = i.toString(),
                    name =  Random.nextInt().toString(),
                    profileImageName = null,
                    password = i.toString()
                )
                memberService.registerMember(memberRegisterRequest)
            }
            for(i in 1..2) {
            val eventRequest = EventHostRequest(
                title = "Hello World! $i",
                description = "I am Here $i",
                eventImage = null,
                closeAt = LocalDateTime.now().plusDays(i.toLong()),
                winMessage = "Winner Winner Chicken Dinner",
                question = "Do you like me?",
                capacity = 700,
                eventType = EventType.DRAWLOT,
            )
            val loginRequest = MemberSignInRequest(
                email = i.toString(),
                password = i.toString()
            )
            val jwt = memberService.signIn(loginRequest).token
            val eventJson = objectMapper.writeValueAsString(eventRequest)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/events")
                    .header("Authorization", "Bearer $jwt")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(eventJson))
                .andExpect(MockMvcResultMatchers.status().isOk)
            }
        }
    }

    @Test
    fun testAddTag()
    {
        tagService.addTag(1L, TagAddRequest("Dinner"))
        tagRepository.findByIdOrNull(1L)?.tagName shouldBe "Dinner"
    }

    private fun addTags(eventId: Long)
    {
        for(n in 1L..10L)
            tagService.addTag(eventId, TagAddRequest("$n Tag"))
    }
    @Test
    fun testGetTags()
    {
        addTags(1L)
        val list = tagRepository.getTagsByEvent(1L)
        list.size shouldBe 10
        for(i in 0 .. 9)
            list[i].tagName shouldStartWith (i + 1).toString()
    }

    @Test
    fun deleteTag()
    {
        addTags(1L)
        for (i in 1L .. 10L step 2)
            tagService.deleteTag(1L, i)
        val list = tagService.getTags(1L)
        list.size shouldBe 5
        for(i in list.indices)
            list[i].tagName shouldStartWith ((i + 1) * 2).toString()
    }

    @Test
    fun deleteAllTags()
    {
        addTags(1L)
        addTags(2L)
        tagRepository.getTagsByEvent(1L).size shouldBe 10
        tagService.deleteTags(1L)
        tagRepository.getTagsByEvent(1L) shouldBe emptyList()
        tagRepository.getTagsByEvent(2L).size shouldBe 10
    }

}