package com.evehunt.evehunt.domain.mail.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Mail(
    var email: String,
    var title: String,
    var mailTxt: String,
    var sent: Boolean = false
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}