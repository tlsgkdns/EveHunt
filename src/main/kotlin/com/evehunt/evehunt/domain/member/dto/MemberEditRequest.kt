package com.evehunt.evehunt.domain.member.dto

import org.hibernate.validator.constraints.Length

data class MemberEditRequest(
    @field:Length(min = 3, max = 20, message = "이름은 3자 이상 20자 미만입니다.")
    val newName: String?,
    val newProfileImage: String?
)
