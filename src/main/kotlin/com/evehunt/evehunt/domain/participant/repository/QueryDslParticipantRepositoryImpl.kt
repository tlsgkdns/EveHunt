package com.evehunt.evehunt.domain.participant.repository

import com.evehunt.evehunt.domain.participant.model.Participant
import com.evehunt.evehunt.domain.participant.model.QParticipant
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class QueryDslParticipantRepositoryImpl: QueryDslParticipantRepository, QueryDslSupport() {
    val participant = QParticipant.participant1
    override fun getParticipant(eventId: Long?, email: String): Participant? {
        val query = BooleanBuilder()
            .and(participant.event.id.eq(eventId))
            .and(participant.participant.email.eq(email))
        return queryFactory.selectFrom(participant)
            .where(query)
            .fetchOne()
    }

    override fun getParticipantsByEvent(eventId: Long?): List<Participant> {
        eventId ?: return listOf()
        return queryFactory.selectFrom(participant)
            .where(participant.event.id.eq(eventId))
            .fetch()
    }

    override fun getParticipantsByMember(pageRequest: Pageable, email: String): Page<Participant> {
        val whereClause = participant.participant.email.eq(email)
        val content = queryFactory.selectFrom(participant)
            .where(whereClause)
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .fetch()
        return PageImpl(content, pageRequest, queryFactory.select(participant.count())
            .where(whereClause).from(participant).fetchOne() ?: 0L)
    }

}