package com.evehunt.evehunt.domain.member.api

import com.evehunt.evehunt.domain.member.dto.MemberRegisterRequest
import com.evehunt.evehunt.domain.member.dto.MemberResponse
import com.evehunt.evehunt.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members/admin")
class AdminController(
    private val memberService: MemberService
) {
    @PostMapping()
    fun registerAdmin(@RequestBody memberRegisterRequest: MemberRegisterRequest): ResponseEntity<MemberResponse>
    {
        return memberService.registerAdmin(memberRegisterRequest).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
}