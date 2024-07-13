package com.evehunt.evehunt.domain.member.service

import com.evehunt.evehunt.domain.member.dto.*
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.global.common.available.dto.AvailableRequest
import com.evehunt.evehunt.global.common.available.dto.AvailableResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface MemberService {
    fun registerMember(memberRegisterRequest: MemberRegisterRequest): MemberResponse
    fun signIn(memberSignInRequest: MemberSignInRequest): MemberSignInResponse
    fun getMember(memberId: Long?): MemberResponse
    fun editMember(memberId: Long, memberEditRequest: MemberEditRequest): MemberResponse
    fun getMember(username: String): MemberResponse
    fun withdrawMember(memberId: Long): Long
    fun deleteAllMember()
    fun getParticipatedEvents(pageRequest: PageRequest, username: String): PageResponse<ParticipateResponse>
}