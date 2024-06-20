package com.evehunt.evehunt.domain.member.dto

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.model.Member
import org.springframework.security.crypto.password.PasswordEncoder

data class MemberRegisterRequest(
    val email: String,
    val name: String,
    val password: String,
    val profileImageName: String?
)
{
    fun to(encoder: PasswordEncoder): Member
    {
        return Member(
            email = email,
            name = name,
            profileImage = Image.from(profileImageName),
            password = encoder.encode(password)
        )
    }
}