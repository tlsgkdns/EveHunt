package com.evehunt.evehunt.domain.participateHistory.repository

import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory
import com.evehunt.evehunt.domain.participateHistory.model.QParticipateHistory
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class QueryDslParticipateHistoryRepositoryImpl: QueryDslParticipateHistoryRepository, QueryDslSupport() {
    val participateHistory = QParticipateHistory.participateHistory
    override fun getParticipateHistory(eventId: Long, email: String): ParticipateHistory? {
        val query = BooleanBuilder()
            .and(participateHistory.event.id.eq(eventId))
            .and(participateHistory.participant.email.eq(email))
        return queryFactory.selectFrom(participateHistory)
            .where(query)
            .fetchOne()
    }

    override fun getParticipantsByEvent(eventId: Long): List<ParticipateHistory> {
        return queryFactory.selectFrom(participateHistory)
            .where(participateHistory.event.id.eq(eventId))
            .fetch()
    }

    override fun getParticipantsByMember(pageRequest: Pageable, email: String): Page<ParticipateHistory> {
        val whereClause = participateHistory.participant.email.eq(email)
        val content = queryFactory.selectFrom(participateHistory)
            .where(whereClause)
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .fetch()
        return PageImpl(content, pageRequest, queryFactory.select(participateHistory.count())
            .where(whereClause).from(participateHistory).fetchOne() ?: 0L)
    }

}