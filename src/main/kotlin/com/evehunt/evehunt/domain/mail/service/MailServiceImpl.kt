package com.evehunt.evehunt.domain.mail.service

import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.domain.mail.repository.MailRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MailServiceImpl(
    private val javaMailSender: JavaMailSender,
    private val mailRepository: MailRepository,
    @Value("\${spring.mail.username}")
    val from: String
): MailService {
    @Transactional
    fun sendMail(email: String, title: String, content: String) {
        if(!email.matches(Regex("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) return
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.apply {
            this.setTo(email)
            this.setSubject(title)
            this.setText(content)
            this.setFrom(from)
        }
        javaMailSender.send(message)
    }
    @Transactional
    override fun sendMails(): Int {
        val mailList = mailRepository.getUnsentMails()
        mailList.forEach { mail ->
            sendMail(mail.email, mail.title, mail.mailTxt)
            mail.sent = true
        }
        return mailList.size
    }
    override fun addMail(mailRequest: MailRequest) {
        mailRepository.save(mailRequest.to())
    }

    override fun getUnsentMailCount(): Int
    {
        return mailRepository.getUnsentMails().size
    }
}