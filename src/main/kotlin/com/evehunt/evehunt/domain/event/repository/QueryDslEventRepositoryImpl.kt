package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.QEvent
import com.evehunt.evehunt.domain.participateHistory.model.QParticipateHistory
import com.evehunt.evehunt.domain.tag.model.QTag
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.SearchType
import com.evehunt.evehunt.global.common.page.SortType
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.ZonedDateTime
import kotlin.math.min

class QueryDslEventRepositoryImpl: QueryDslSupport(), QueryDslEventRepository {
    private val event = QEvent.event
    private val participant = QParticipateHistory.participateHistory
    private val tag = QTag.tag
    override fun searchEvents(pageRequest: PageRequest): Page<Event>
    {
        val pageable = pageRequest.getPageable()
        val whereClause = BooleanBuilder()
        val keyword = pageRequest.keyword
        var fromEntity: EntityPathBase<*> = event
        var leftJoinEntity: EntityPathBase<*>? = null
        val orderBy: OrderSpecifier<*>
        if(keyword != null)
        {
            when(pageRequest.searchType)
            {
                SearchType.TITLE -> whereClause.or(event.title.contains(keyword))
                SearchType.DESCRIPTION -> whereClause.or(event.description.contains(keyword))
                SearchType.TITLEDESCRIPTION -> {
                    whereClause.or(event.title.contains(keyword))
                    whereClause.or(event.description.contains(keyword))
                }
                SearchType.HOST -> {
                    whereClause.or(event.host.name.contains(keyword))
                }
                SearchType.PARTICIPATE -> {
                    fromEntity = participant
                    leftJoinEntity = participant.event
                    whereClause.or(participant.participant.id.eq(keyword.toLong()))
                }
                else -> {
                    fromEntity = tag
                    leftJoinEntity = tag.event
                    whereClause.or(tag.tagName.eq(keyword))
                }
            }
        }
        when(pageRequest.sortType)
        {
            SortType.NEW -> {
                orderBy = if(pageRequest.asc == false) event.createdAt.desc()
                else event.createdAt.asc()
            }
            SortType.CLOSE -> {
                orderBy = if(pageRequest.asc == false) event.closeAt.desc()
                else event.closeAt.asc()
            }
            SortType.HOST -> {
                orderBy = if(pageRequest.asc == false) event.host.name.desc()
                else event.host.name.asc()
            }
            else -> {
                orderBy = if(pageRequest.asc == false) event.title.desc()
                else event.title.asc()
            }
        }
        val query = queryFactory.select(event)
            .from(fromEntity)
            .where(whereClause)
            .apply {
                if(leftJoinEntity != null)
                    this.leftJoin(leftJoinEntity)
            }
            .orderBy(orderBy)
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
        return list.limit(min(list.fetch().size.toLong(), 5)).fetch()
    }
}