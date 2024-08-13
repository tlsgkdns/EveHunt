package com.evehunt.evehunt.domain.mail.dto

import com.evehunt.evehunt.domain.mail.model.Mail

data class MailRequest (
    val email: String,
    val title: String,
    val content: String
)
{
    fun to(): Mail
    {
        return Mail(
            email = email,
            title = title,
            mailTxt = content
        )
    }
}