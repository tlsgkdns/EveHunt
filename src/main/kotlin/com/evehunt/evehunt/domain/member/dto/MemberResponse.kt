package com.evehunt.evehunt.domain.member.dto

import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.member.model.MemberType
import java.time.LocalDateTime


data class MemberResponse(
    val email: String,
    val memberId: Long?,
    val profileImageName: String?,
    val name: String,
    val registeredDate: LocalDateTime,
    val updatedDate: LocalDateTime,
    val role: Set<MemberType>,
    val suspended: LocalDateTime? = null
)
{
    companion object
    {
        fun from(member: Member): MemberResponse
        {
            return MemberResponse(
                email = member.email,
                memberId = member.id,
                name = member.name,
                profileImageName = member.profileImage?.getLink(),
                registeredDate = member.createdAt.toLocalDateTime(),
                updatedDate = member.updatedAt.toLocalDateTime(),
                role = member.roleSet,
                suspended = member.suspendedTime?.toLocalDateTime()
            )
        }
    }
}
