package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.dto.EventCardResponse
import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.QEvent
import com.evehunt.evehunt.domain.participateHistory.model.QParticipateHistory
import com.evehunt.evehunt.domain.tag.model.QTag
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.ZonedDateTime

class QueryDslEventRepositoryImpl: QueryDslSupport(), QueryDslEventRepository {
    private val event = QEvent.event
    private val participant = QParticipateHistory.participateHistory
    private val tag = QTag.tag
    override fun searchEvents(pageRequest: PageRequest): Page<EventCardResponse>
    {
        val pageable = pageRequest.getPageable()
        val keyword = pageRequest.keyword
        val query = queryFactory.select(Projections.constructor(
            EventCardResponse::class.java,
            event.id,
            event.host.name,
            event.title,
            event.capacity,
            event.closeAt,
            queryFactory.select(participant.count()).from(participant).where(participant.event.eq(event))
        )).from(event)
        if(keyword != null)
        {
            when(pageRequest.searchType?.lowercase())
            {
                "description" -> query.from(event).where(event.description.contains(keyword))
                "titledescription" -> {
                    query.from(event)
                    query.where(event.title.contains(keyword).or(event.description.contains(keyword)))
                }
                "host" -> {
                    query.from(event)
                    query.where(event.host.name.contains(keyword))
                }
                "tag" -> {
                    query.from(tag).leftJoin(tag.event).where(tag.tagName.eq(keyword))
                }
                "hostid" -> {
                    query.from(event)
                    query.where(event.host.id.eq(keyword.toLong()))
                }
                "participate" -> {
                    query.from(participant).leftJoin(participant.event).where(participant.participant.id.eq(keyword.toLong()))
                }
                else -> {
                    query.from(event)
                    query.where(event.title.contains(keyword))
                }
            }
        }
        when(pageRequest.sortType?.lowercase())
        {
            "close" -> {
                if(pageRequest.asc == false) query.orderBy(event.closeAt.desc())
                else query.orderBy(event.closeAt.desc())
            }
            "host" -> {
                if(pageRequest.asc == false) query.orderBy(event.host.name.desc())
                else query.orderBy(event.host.name.asc())
            }
            "title" -> {
                if(pageRequest.asc == false) query.orderBy(event.title.desc())
                else query.orderBy(event.title.asc())
            }
            else -> {
                if(pageRequest.asc == false) query.orderBy(event.createdAt.desc())
                else query.orderBy(event.createdAt.asc())
            }
        }
        val cnt = query.fetch().size.toLong()
        return PageImpl(query.offset(pageable.offset).limit(pageable.pageSize.toLong()).fetch(), pageable, cnt)
    }

    override fun getExpiredEvents(): List<Event> {
        val whereClause = BooleanBuilder(event.eventStatus.eq(EventStatus.PROCEED))
            .and(event.closeAt.before(ZonedDateTime.now()))
        return queryFactory.selectFrom(event)
            .where(whereClause)
            .fetch()
    }
    override fun getPopularEvents(): List<Event> {
        val list = queryFactory.select(event)
            .from(participant)
            .leftJoin(participant.event, event)
            .groupBy(participant.event.id)
            .orderBy(event.count().desc())
        return if(list.fetch().size > 5) list.limit(5).fetch()
        else list.fetch()
    }
}