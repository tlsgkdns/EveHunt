package com.evehunt.evehunt.domain.mail.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest


interface MailService {
    fun sendMails(): Int
    fun addMail(mailRequest: MailRequest)
    fun getUnsentMailCount(): Int
}