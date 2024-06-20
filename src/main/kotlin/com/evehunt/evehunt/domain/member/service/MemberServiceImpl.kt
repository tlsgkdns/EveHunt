package com.evehunt.evehunt.domain.member.service

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.dto.*
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import com.evehunt.evehunt.global.exception.exception.AlreadyExistModelException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import com.evehunt.evehunt.global.infra.security.TokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val encoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
): MemberService {
    private fun getValidatedMember(id: Long): Member
    {
        return memberRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Member", id.toString())
    }
    private fun getValidatedMember(email: String): Member
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

    override fun signIn(memberSignInRequest: MemberSignInRequest): MemberSignInResponse {
        val member = getValidatedMember(memberSignInRequest.email)
            .takeIf { encoder.matches(memberSignInRequest.password, it.password) }
            ?: throw IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")
        val token = tokenProvider.createToken("${member.email}:${member.name}")
        return MemberSignInResponse(member.email, token)
    }

    @Transactional
    override fun getMember(memberId: Long): MemberResponse {
        return getValidatedMember(memberId).let { MemberResponse.from(it) }
    }

    @Transactional
    override fun editMember(memberId: Long, memberEditRequest: MemberEditRequest): MemberResponse {
        val member = getValidatedMember(memberId)
        val name = memberEditRequest.newName ?: member.name
        val profileImage = memberEditRequest.newProfileImage.let { Image.from(it) } ?: member.profileImage
        val editedMember = member.apply { this.name = name }.apply { this.profileImage = profileImage }
        return memberRepository.save(editedMember).let { MemberResponse.from(it) }
    }

    @Transactional
    override fun withdrawMember(memberId: Long): Long {
        val member = getValidatedMember(memberId)
        memberRepository.delete(member)
        return memberId
    }

    override fun deleteAllMember() {
        memberRepository.deleteAll()
    }
}