package com.evehunt.evehunt

import com.evehunt.evehunt.domain.member.dto.MemberEditRequest
import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class MemberTests  @Autowired constructor(
    val memberService: MemberService
) {
    @Test
    fun testRegisterMember()
    {
        val memberRegisterRequest = MemberRegisterRequest(
            email = "ajtwlsgkdns@naver.com",
            name = "신하운",
            profileImageName = null,
            password = "aaaaa"
        )
        val member = memberService.registerMember(memberRegisterRequest)
        member.email shouldBe memberRegisterRequest.email
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
        testRegisterMember()
        val memberId = memberService.withdrawMember(1)
        shouldThrow<ModelNotFoundException> {
            memberService.getMember(memberId)
        }
    }
    @Test
    fun testEditMember()
    {
        testRegisterMember()
        val newName = "newName"
        val member = memberService.editMember(1, MemberEditRequest(newName = newName, null))
        member.name shouldBe newName
        memberService.getMember(1).name shouldBe newName
    }
}