package com.evehunt.evehunt.domain.mail.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.domain.mail.dto.MailResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailServiceImpl(
    private val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    val from: String
): MailService {
    override fun sendMail(email: String, mailRequest: MailRequest): MailResponse {
        if(!email.matches(Regex("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")))
            return MailResponse("", "", "")
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.apply {
            this.setTo(email)
            this.setSubject(mailRequest.title)
            this.setText(mailRequest.content)
            this.setFrom(from)
        }
        javaMailSender.send(message)
        return MailResponse(email, mailRequest.title, mailRequest.content)
    }
}