package com.evehunt.evehunt.domain.mail.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.domain.mail.dto.MailResponse

interface MailService {
    fun sendMail(email: String, mailRequest: MailRequest): MailResponse
}