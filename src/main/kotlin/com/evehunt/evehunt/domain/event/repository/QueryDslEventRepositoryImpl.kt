package com.evehunt.evehunt.domain.event.repository

import com.evehunt.evehunt.domain.event.dto.EventCardResponse
import com.evehunt.evehunt.domain.event.dto.EventIdResponse
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.model.QEvent
import com.evehunt.evehunt.domain.image.model.QImage
import com.evehunt.evehunt.domain.participant.model.QParticipant
import com.evehunt.evehunt.domain.tag.model.QTag
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.ZonedDateTime


class QueryDslEventRepositoryImpl: QueryDslSupport(), QueryDslEventRepository {
    private val event = QEvent.event
    private val participant = QParticipant.participant1
    private val tag = QTag.tag
    private val image = QImage.image
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
            queryFactory.select(participant.count()).from(participant).where(participant.event.eq(event)),
            event.image
        )).from(event).leftJoin(event.image, image)
        if(keyword.isNotEmpty())
        {
            when(pageRequest.searchType.lowercase())
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
                    query.leftJoin(tag).on(tag.event.id.eq(event.id))
                        .where(tag.tagName.eq(keyword))
                }
                "hostid" -> {
                    query.from(event)
                    query.where(event.host.id.eq(keyword.toLong()))
                }
                "participate" -> {
                    query.leftJoin(participant).on(participant.event.id.eq(event.id))
                        .where(participant.participant.id.eq(keyword.toLong()))
                }
                else -> {
                    query.from(event)
                    query.where(event.title.contains(keyword))
                }
            }
        }
        when(pageRequest.sortType.lowercase())
        {
            "close" -> {
                if(!pageRequest.asc) query.orderBy(event.closeAt.desc())
                else query.orderBy(event.closeAt.desc())
            }
            "host" -> {
                if(!pageRequest.asc) query.orderBy(event.host.name.desc())
                else query.orderBy(event.host.name.asc())
            }
            "title" -> {
                if(!pageRequest.asc) query.orderBy(event.title.desc())
                else query.orderBy(event.title.asc())
            }
            else -> {
                if(!pageRequest.asc) query.orderBy(event.createdAt.desc())
                else query.orderBy(event.createdAt.asc())
            }
        }
        val cnt = query.fetch().size.toLong()
        return PageImpl(query.offset(pageable.offset).limit(pageable.pageSize.toLong()).fetch(), pageable, cnt)
    }

    override fun setExpiredEventsClosed(): List<EventIdResponse> {
        val whereClause = BooleanBuilder(event.eventStatus.eq(EventStatus.PROCEED))
            .and(event.closeAt.before(ZonedDateTime.now()))
        val list = queryFactory.select(Projections.constructor(
            EventIdResponse::class.java,
            event.id
        )).from(event)
            .where(whereClause)
            .fetch()
        queryFactory.update(event)
                .set(event.eventStatus, EventStatus.CLOSED)
                .where(whereClause)
                .execute()

        entityManager.clear()
        entityManager.flush()

        return list
    }
    override fun getPopularEvents(): List<EventCardResponse> {
        val participantCount = Expressions.numberPath(Long::class.java, "participantCount")
        val list = queryFactory.select(Projections.constructor(
                EventCardResponse::class.java,
                event.id,
                event.host.name,
                event.title,
                event.capacity,
                event.closeAt,
                ExpressionUtils.`as`(queryFactory.select(participant.count()).from(participant).where(participant.event.eq(event)), participantCount),
                event.image
            ))
            .from(event)
            .leftJoin(event.image, image)
            .where(event.eventStatus.eq(EventStatus.PROCEED))
            .orderBy(participantCount.desc())
        return if(list.fetch().size > 5) list.limit(5).fetch()
        else list.fetch()
    }
}