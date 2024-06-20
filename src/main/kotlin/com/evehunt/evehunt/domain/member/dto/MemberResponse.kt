package com.evehunt.evehunt.domain.member.dto

import com.evehunt.evehunt.domain.member.model.Member
import java.time.LocalDateTime


data class MemberResponse(
    val email: String,
    val profileImageName: String?,
    val name: String,
    val registeredDate: LocalDateTime,
    val updatedDate: LocalDateTime
)
{
    companion object
    {
        fun from(member: Member): MemberResponse
        {
            return MemberResponse(
                email = member.email,
                name = member.name,
                profileImageName = member.profileImage?.getLink(),
                registeredDate = member.createdAt.toLocalDateTime(),
                updatedDate = member.updatedAt.toLocalDateTime()
            )
        }
    }
}
