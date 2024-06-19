package com.evehunt.evehunt.domain.member.model

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class Member(
    var name: String,
    var email: String,
    var password: String,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var profileImage: Image?
) : BaseTimeEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}