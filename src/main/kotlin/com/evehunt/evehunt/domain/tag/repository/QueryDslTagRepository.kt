package com.evehunt.evehunt.domain.tag.repository

import com.evehunt.evehunt.domain.tag.model.Tag

interface QueryDslTagRepository {
    fun getTagsByEvent(eventId: Long?): List<Tag>
    fun deleteTagsByEvent(eventId: Long?)
    fun getCountOfTags(tagName: String): Int
}