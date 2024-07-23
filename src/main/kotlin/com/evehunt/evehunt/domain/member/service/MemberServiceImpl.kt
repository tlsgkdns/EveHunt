package com.evehunt.evehunt.domain.member.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.domain.mail.service.MailService
import com.evehunt.evehunt.domain.member.dto.*
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participateHistory.service.ParticipateHistoryService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl(
    private val memberService: MemberEntityService,
    private val participateHistoryService: ParticipateHistoryService,
    private val mailService: MailService
): MemberService {
    private final val welcomeTitleMessage = "가입을 환영합니다!"
    private final fun welcomeContentMessage(name: String) = "${name}님 가입을 환영합니다!"

    private final val withdrawTitleMessage = "탈퇴가 완료되었습니다."
    private final fun withdrawContentMessage(name: String) = "${name}님의 탈퇴가 무사히 완료되었습니다. 다시 만날 날을 기대하겠습니다."

    override fun registerMember(memberRegisterRequest: MemberRegisterRequest): MemberResponse {
        val member = memberService.registerMember(memberRegisterRequest)
        // mailService.sendMail(member.email, MailRequest(welcomeTitleMessage, welcomeContentMessage(member.name)))
        return member
    }

    override fun signIn(memberSignInRequest: MemberSignInRequest): MemberSignInResponse {
        return memberService.signIn(memberSignInRequest)
    }

    override fun getMember(memberId: Long?): MemberResponse {
        return memberService.getMember(memberId)
    }

    override fun editMember(memberId: Long, memberEditRequest: MemberEditRequest): MemberResponse {
        return memberService.editMember(memberId, memberEditRequest)
    }

    override fun getMember(username: String?): MemberResponse {
        return memberService.getMember(username)
    }

    override fun withdrawMember(memberId: Long): Long {
        val email = getMember(memberId).email
        val name = getMember(memberId).name
        val ret = memberService.withdrawMember(memberId)
        mailService.sendMail(email, MailRequest(withdrawTitleMessage, withdrawContentMessage(name)))
        return ret
    }

    override fun deleteAllMember() {
        return memberService.deleteAllMember()
    }

    override fun getParticipatedEvents(pageRequest: PageRequest, username: String): PageResponse<ParticipateResponse> {
        return participateHistoryService.getParticipateHistoryByMember(username, pageRequest)
    }

    override fun editPassword(memberId: Long, memberPasswordEditRequest: MemberPasswordEditRequest): MemberResponse {
        return memberService.editPassword(memberId, memberPasswordEditRequest)
    }
}