package com.evehunt.evehunt.domain.member.service

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.dto.*
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.member.model.MemberType
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import com.evehunt.evehunt.global.exception.exception.AlreadyExistModelException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import com.evehunt.evehunt.global.exception.exception.UnMatchedPasswordException
import com.evehunt.evehunt.global.exception.exception.UnMatchedValueException
import com.evehunt.evehunt.global.infra.security.TokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.ZonedDateTime

@Service
class MemberEntityServiceImpl(
    private val memberRepository: MemberRepository,
    private val encoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
): MemberEntityService {
    private fun getExistMember(id: Long?): Member
    {
        return memberRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Member", id.toString())
    }
    private fun getExistMember(email: String?): Member
    {
        return memberRepository.findMemberByEmail(email) ?: throw ModelNotFoundException("Member", email)
    }
    @Transactional
    override fun registerMember(memberRegisterRequest: MemberRegisterRequest): MemberResponse {
        if(memberRepository.existsByEmail(memberRegisterRequest.email))
            throw AlreadyExistModelException(memberRegisterRequest.email)
        val member = memberRegisterRequest.to(encoder)
            .let { memberRepository.save(it) }
        return member.let { MemberResponse.from(it) }
    }

    @Transactional
    override fun signIn(memberSignInRequest: MemberSignInRequest): MemberSignInResponse {
        val member = getExistMember(memberSignInRequest.email)
            .takeIf { encoder.matches(memberSignInRequest.password, it.password) }
            ?: throw UnMatchedValueException("아이디", "비밀번호")
        val token = tokenProvider.createToken("${member.email}:${member.roleSet}:${member.name}")
        return MemberSignInResponse(member.email, token)
    }

    @Transactional
    override fun getMember(memberId: Long?): MemberResponse {
        return getExistMember(memberId).let { MemberResponse.from(it) }
    }

    override fun getMember(username: String?): MemberResponse {
        return getExistMember(username).let { MemberResponse.from(it) }
    }

    @Transactional
    override fun editMember(memberId: Long, memberEditRequest: MemberEditRequest): MemberResponse {
        val member = getExistMember(memberId)
        val name = memberEditRequest.newName ?: member.name
        val profileImage = memberEditRequest.newProfileImage.let { Image.from(it) } ?: member.profileImage
        val editedMember = member.apply { this.name = name }.apply { this.profileImage = profileImage }
        return memberRepository.save(editedMember).let { MemberResponse.from(it) }
    }

    @Transactional
    override fun withdrawMember(memberId: Long): Long {
        val member = getExistMember(memberId)
        memberRepository.delete(member)
        return memberId
    }

    override fun deleteAllMember() {
        memberRepository.deleteAll()
    }

    @Transactional
    override fun editPassword(memberId: Long, passwordEditRequest: MemberPasswordEditRequest): MemberResponse {
        if(passwordEditRequest.passwordCheck != passwordEditRequest.newPassword)
            throw UnMatchedValueException("새 비밀번호", "새 비밀번호 확인")
        val member = getExistMember(memberId)
        if(!encoder.matches(passwordEditRequest.currentPassword, member.password))
            throw UnMatchedPasswordException()
        member.password = encoder.encode(passwordEditRequest.newPassword)
        return memberRepository.save(member).let {
            MemberResponse.from(it)
        }
    }

    @Transactional
    override fun addAdminAuthority(memberId: Long?): MemberResponse {
        val member = getExistMember(memberId)
        member.roleSet.add(MemberType.ADMIN)
        return memberRepository.save(member).let {
            MemberResponse.from(it)
        }
    }

    @Transactional
    override fun suspendMember(memberId: Long?, suspendDay: Int): MemberResponse {
        val member = getExistMember(memberId)
        val suspendUntil = member.suspendedTime?.plusDays(suspendDay.toLong())
            ?: ZonedDateTime.now().plusDays(suspendDay.toLong())
        member.suspendedTime = suspendUntil
        return memberRepository.save(member).let {
            MemberResponse.from(it)
        }
    }

    override fun cancelSuspend(memberId: Long): MemberResponse {
        val member = getExistMember(memberId)
        member.suspendedTime = null
        return memberRepository.save(member).let {
            MemberResponse.from(it)
        }
    }

    override fun cancelExpiredSuspend() {
        val members = memberRepository.getMembersBySuspendedTimeIsNotNull()
        for(member in members)
            if (member.suspendedTime?.toLocalTime()?.isBefore(LocalTime.now()) == true)
            {
                member.suspendedTime = null
                memberRepository.save(member)
            }
    }
}