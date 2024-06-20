package com.evehunt.evehunt

import com.evehunt.evehunt.domain.member.dto.MemberEditRequest
import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.dto.MemberSignInRequest
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
class MemberTests  @Autowired constructor(
    val memberService: MemberService,
    val mockMvc: MockMvc
) {
    companion object
    {
        @JvmStatic
        @BeforeAll
        fun registerMemberBeforeTest(@Autowired memberService: MemberService) {
            for (i in 1..100) {
                val memberRegisterRequest = MemberRegisterRequest(
                    email = i.toString(),
                    name =  Random.nextInt().toString(),
                    profileImageName = null,
                    password =  Random.nextInt().toString()
                )
                memberService.registerMember(memberRegisterRequest)
            }
        }
        @JvmStatic
        @AfterAll
        fun deleteDataAfterTest(@Autowired memberService: MemberService) {
            memberService.deleteAllMember()
        }}

    @Test
    fun testRegisterMember()
    {
        val memberRegisterRequest = MemberRegisterRequest(
            email = "aaaa",
            name = "aaa",
            profileImageName = null,
            password = "aaaaa"
        )
        val member = memberService.registerMember(memberRegisterRequest)
        member.email shouldBe "aaaa"
    }

    @Test
    fun testGetMember()
    {
        val member = memberService.getMember(1)
        member.email shouldBe "1"
    }

    @Test
    fun testDeleteMember()
    {
        val memberId = memberService.withdrawMember(2)
        shouldThrow<ModelNotFoundException> {
            memberService.getMember(memberId)
        }
    }
    @Test
    fun testEditMember()
    {
        val newName = "newName"
        val member = memberService.editMember(3, MemberEditRequest(newName = newName, null))
        member.name shouldBe newName
        memberService.getMember(3).name shouldBe newName
    }
}