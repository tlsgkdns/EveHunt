package com.evehunt.evehunt.domain.mail.repository

import com.evehunt.evehunt.domain.mail.model.Mail
import com.evehunt.evehunt.domain.mail.model.QMail
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport

class QueryDslMailRepositoryImpl: QueryDslSupport(), QueryDslMailRepository {
    private val mail = QMail.mail
    private val batchCount = 50L
    override fun getUnsentMails(): List<Mail> {
        val query = queryFactory.selectFrom(mail)
            .where(mail.sent.eq(false))
        if(query.fetch().size < batchCount) return query.fetch()
        return query.limit(batchCount).fetch()
    }
}