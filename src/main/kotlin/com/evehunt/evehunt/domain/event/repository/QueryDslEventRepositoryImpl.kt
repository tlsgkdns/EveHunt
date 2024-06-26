package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.QEvent
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.ZonedDateTime

class QueryDslEventRepositoryImpl: QueryDslSupport(), QueryDslEventRepository {
    private val event = QEvent.event
    override fun searchEvents(pageRequest: Pageable, keyword: String?): Page<Event>
    {
        val whereClause = BooleanBuilder()
        keyword?.let { whereClause.or(event.title.contains(it)) }
        keyword?.let { whereClause.or(event.description.contains(it)) }
        val content = queryFactory.selectFrom(event)
            .where(whereClause)
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .orderBy(event.createdAt.desc())
            .fetch()
        return PageImpl(content, pageRequest, queryFactory.select(event.count()).where(whereClause).from(event).fetchOne() ?: 0L)
    }

    override fun getExpiredEvents(): List<Event> {
        val whereClause = BooleanBuilder(event.eventStatus.eq(EventStatus.PROCEED))
            .and(event.closeAt.before(ZonedDateTime.now()))
        return queryFactory.selectFrom(event)
            .where(whereClause)
            .fetch()
    }
}