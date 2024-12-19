package com.evehunt.evehunt.domain.member.repository

import com.evehunt.evehunt.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findMemberByEmail(email: String?): Member?
    fun existsByEmail(email: String): Boolean
    fun getMembersBySuspendedTimeIsNotNull(): List<Member>
}