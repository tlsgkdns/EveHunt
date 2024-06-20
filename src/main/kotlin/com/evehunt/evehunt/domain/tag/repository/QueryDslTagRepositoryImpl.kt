package com.evehunt.evehunt.domain.tag.repository

import com.evehunt.evehunt.domain.tag.model.QTag
import com.evehunt.evehunt.domain.tag.model.Tag
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport

class QueryDslTagRepositoryImpl: QueryDslSupport(), QueryDslTagRepository {
    private val tag = QTag.tag
    override fun getTagsByEvent(eventId: Long): List<Tag> {
        return queryFactory.selectFrom(tag)
            .where(tag.event.id.eq(eventId))
            .fetch()
    }

    override fun deleteTagsByEvent(eventId: Long) {
        queryFactory.delete(tag)
            .where(tag.event.id.eq(eventId))
            .execute()
    }
}