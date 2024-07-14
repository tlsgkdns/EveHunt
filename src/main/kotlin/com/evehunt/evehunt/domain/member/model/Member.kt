package com.evehunt.evehunt.domain.member.model

import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*

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
    val id: Long? = null
}