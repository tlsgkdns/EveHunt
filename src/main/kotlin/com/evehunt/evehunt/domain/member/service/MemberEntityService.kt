package com.evehunt.evehunt.domain.member.service

import com.evehunt.evehunt.domain.member.dto.*

interface MemberEntityService {
    fun registerMember(memberRegisterRequest: MemberRegisterRequest): MemberResponse
    fun signIn(memberSignInRequest: MemberSignInRequest): MemberSignInResponse
    fun getMember(memberId: Long?): MemberResponse
    fun editMember(memberId: Long, memberEditRequest: MemberEditRequest): MemberResponse
    fun withdrawMember(memberId: Long): Long
    fun deleteAllMember()
    fun getMember(username: String?): MemberResponse
    fun editPassword(memberId: Long, passwordEditRequest: MemberPasswordEditRequest): MemberResponse
    fun addAdminAuthority(memberId: Long?): MemberResponse
    fun suspendMember(memberId: Long?, suspendDay: Int): MemberResponse
    fun cancelSuspend(memberId: Long): MemberResponse
    fun cancelExpiredSuspend()
}