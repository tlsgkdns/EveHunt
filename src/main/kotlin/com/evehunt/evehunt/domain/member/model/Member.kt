package com.evehunt.evehunt.domain.member.model

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class Member(
    private val name: String,
    private val email: String,
    private val password: String,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    private val profileImage: Image
) : BaseTimeEntity(){
    @Id
    private val id: Long? = null;
}