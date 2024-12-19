package com.evehunt.evehunt.domain.member.dto

import org.hibernate.validator.constraints.Length

data class MemberPasswordEditRequest(
    val currentPassword: String,
    @field:Length(min = 7, max = 20, message = "비밀번호는 7자 이상 20자 미만으로 설정해주세요")
    val newPassword: String,
    val passwordCheck: String
)