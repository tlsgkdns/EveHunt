package com.evehunt.evehunt.domain.mail.repository

import com.evehunt.evehunt.domain.mail.model.Mail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MailRepository: JpaRepository<Mail, Long>, QueryDslMailRepository {

}