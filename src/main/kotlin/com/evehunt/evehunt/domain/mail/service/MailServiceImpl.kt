package com.evehunt.evehunt.domain.mail.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.global.common.RedisLockService
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MailServiceImpl(
    private val javaMailSender: JavaMailSender,
    private val lockService: RedisLockService,
    @Value("\${spring.mail.username}")
    val from: String
): MailService {
    @Async
    @Transactional
    override fun sendMail(email: String, mailRequest: MailRequest) {
        if(!email.matches(Regex("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) return
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.apply {
            this.setTo(email)
            this.setSubject(mailRequest.title)
            this.setText(mailRequest.content)
            this.setFrom(from)
        }
        javaMailSender.send(message)
    }
}