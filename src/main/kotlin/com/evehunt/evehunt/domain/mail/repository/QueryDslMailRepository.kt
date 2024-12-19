package com.evehunt.evehunt.domain.mail.repository

import com.evehunt.evehunt.domain.mail.model.Mail

interface QueryDslMailRepository {
    fun getUnsentMails(): List<Mail>
}