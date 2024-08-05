package com.evehunt.evehunt.domain.mail.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest

interface MailService {
    fun sendMail(email: String, mailRequest: MailRequest)
}