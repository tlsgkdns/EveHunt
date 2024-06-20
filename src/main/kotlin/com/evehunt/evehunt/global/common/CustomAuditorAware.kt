package com.evehunt.evehunt.global.common

import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomAuditorAware(
    private val memberRepository: MemberRepository
):AuditorAware<Member> {
    override fun getCurrentAuditor(): Optional<Member> {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map { it.authentication }
            .map { it.principal as UserDetails }
            .map {
                if(it.password.isEmpty()) null
                else memberRepository.findMemberByEmail(it.username)
            }
    }

}