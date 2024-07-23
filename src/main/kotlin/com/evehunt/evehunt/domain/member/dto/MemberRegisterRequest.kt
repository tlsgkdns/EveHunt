package com.evehunt.evehunt.domain.member.dto

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.model.Member
import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length
import org.springframework.security.crypto.password.PasswordEncoder

data class MemberRegisterRequest(
    @Email(message = "이메일을 입력해주세요")
    val email: String,
    @field:Length(min = 3, max = 20, message = "이름은 3자 이상 20자 미만입니다.")
    val name: String,
    @field:Length(min = 7, max = 20, message = "비밀번호는 7자 이상 20자 미만으로 설정해주세요")
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