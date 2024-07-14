package com.evehunt.evehunt.domain.member.api

import com.evehunt.evehunt.domain.member.dto.*
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {
    @GetMapping("/{memberId}")
    fun getMember(@PathVariable memberId: Long): ResponseEntity<MemberResponse>
    {
        return memberService.getMember(memberId)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }
    @GetMapping()
    fun getLoginMember(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<MemberResponse>
    {
        return memberService.getMember(userDetails.username)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }
    @GetMapping("/username/{username}")
    fun getMember(@PathVariable username: String): ResponseEntity<MemberResponse>
    {
        return memberService.getMember(username)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }
    @PostMapping("/signUp")
    fun registerMember(@RequestBody memberRegisterRequest: MemberRegisterRequest): ResponseEntity<MemberResponse>
    {
        return memberService.registerMember(memberRegisterRequest)
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }
    @PostMapping("/signIn")
    fun signInMember(@RequestBody memberSignInRequest: MemberSignInRequest): ResponseEntity<MemberSignInResponse>
    {
        return memberService.signIn(memberSignInRequest)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }

    @PatchMapping("/{memberId}")
    fun editMember(@PathVariable memberId: Long, @RequestBody memberEditRequest: MemberEditRequest)
            : ResponseEntity<MemberResponse>
    {
        return memberService.editMember(memberId, memberEditRequest)
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @DeleteMapping("/{memberId}")
    fun withdrawMember(@PathVariable memberId: Long): ResponseEntity<Long>
    {
        return memberService.withdrawMember(memberId)
            .let { ResponseEntity.status(HttpStatus.NOT_FOUND).build() }
    }
    @GetMapping("/events")
    fun getParticipateHistoryByMember(@AuthenticationPrincipal user: UserDetails, pageRequest: PageRequest): ResponseEntity<PageResponse<ParticipateResponse>> {
        return memberService.getParticipatedEvents(pageRequest, user.username).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}