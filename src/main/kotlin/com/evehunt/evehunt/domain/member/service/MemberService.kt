package com.evehunt.evehunt.domain.member.service

import com.evehunt.evehunt.domain.member.dto.*

interface MemberService {
    fun registerMember(memberRegisterRequest: MemberRegisterRequest): MemberResponse
    fun signIn(memberSignInRequest: MemberSignInRequest): MemberSignInResponse
    fun getMember(memberId: Long): MemberResponse
    fun editMember(memberId: Long, memberEditRequest: MemberEditRequest): MemberResponse
    fun withdrawMember(memberId: Long): Long

    fun deleteAllMember()
}