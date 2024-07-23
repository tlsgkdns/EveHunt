package com.evehunt.evehunt.domain.tag.repository

import com.evehunt.evehunt.domain.tag.model.QTag
import com.evehunt.evehunt.domain.tag.model.Tag
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport

class QueryDslTagRepositoryImpl: QueryDslSupport(), QueryDslTagRepository {
    private val tag = QTag.tag
    override fun getTagsByEvent(eventId: Long?): List<Tag> {
        return queryFactory.selectFrom(tag)
            .where(tag.event.id.eq(eventId))
            .fetch()
    }
    override fun deleteTagsByEvent(eventId: Long?) {
        queryFactory.delete(tag)
            .where(tag.event.id.eq(eventId))
            .execute()
    }
    override fun getCountOfTags(tagName: String): Int {
        return queryFactory.selectFrom(tag)
            .where(tag.tagName.eq(tagName))
            .fetch()
            .size
    }

    override fun getPopularTags(): List<Tag> {
        val list = queryFactory.select(tag.tagName, tag.tagName.count())
            .from(tag)
            .groupBy(tag.tagName)
            .orderBy(tag.tagName.count().desc())
            .offset(0)
            .limit(10)
            .fetch()
        return list.map {
            Tag(it.get(tag.event), it.get(tag.tagName))
        }
    }

}