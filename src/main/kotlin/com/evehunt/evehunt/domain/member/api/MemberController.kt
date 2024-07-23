package com.evehunt.evehunt.domain.member.api

import com.evehunt.evehunt.domain.member.dto.*
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.InvalidRequestException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {
    fun checkBindingResult(bindingResult: BindingResult)
    {
        if(bindingResult.hasErrors()) {
            var messages: String = ""
            val list = bindingResult.fieldErrors
            for ((idx, message) in list.map { it.defaultMessage }.withIndex())
            {
                messages += message
                if(idx != list.size - 1)
                    messages += '\n'
            }
            throw InvalidRequestException("Member", messages)
        }
    }
    @GetMapping("/{memberId}")
    fun getMember(@PathVariable memberId: Long): ResponseEntity<MemberResponse>
    {
        return memberService.getMember(memberId)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }
    @GetMapping("/isLogin")
    fun isLogin(@AuthenticationPrincipal userDetails: UserDetails?): ResponseEntity<Boolean>
    {
        var body = true
        if(userDetails == null || userDetails.username == "anonymous") body = false
        return ResponseEntity.status(HttpStatus.OK).body(body)
    }
    @GetMapping()
    fun getLoginMember(@AuthenticationPrincipal userDetails: UserDetails?): ResponseEntity<MemberResponse>
    {
        return memberService.getMember(userDetails?.username)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }
    @GetMapping("/username/{username}")
    fun getMember(@PathVariable username: String): ResponseEntity<MemberResponse>
    {
        return memberService.getMember(username)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }
    @PostMapping("/signUp")
    fun registerMember(@RequestBody @Valid memberRegisterRequest: MemberRegisterRequest, bindingResult: BindingResult): ResponseEntity<MemberResponse>
    {
        checkBindingResult(bindingResult)
        return memberService.registerMember(memberRegisterRequest)
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }
    @PostMapping("/signIn")
    fun signInMember(@RequestBody memberSignInRequest: MemberSignInRequest): ResponseEntity<MemberSignInResponse>
    {
        return memberService.signIn(memberSignInRequest)
            .let { ResponseEntity.status(HttpStatus.OK).body(it) }
    }

    @PatchMapping("/{memberId}/profile")
    fun editMember(@PathVariable @Valid memberId: Long, @RequestBody memberEditRequest: MemberEditRequest,
                   bindingResult: BindingResult)
            : ResponseEntity<MemberResponse>
    {
        checkBindingResult(bindingResult)
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
    @PatchMapping("/{memberId}/password")
    fun editPassword(@RequestBody @Valid passwordEditRequest: MemberPasswordEditRequest,
                     @PathVariable memberId: Long, bindingResult: BindingResult)
    :ResponseEntity<MemberResponse>
    {
        checkBindingResult(bindingResult)
        return memberService.editPassword(memberId, passwordEditRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}