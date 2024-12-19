package com.evehunt.evehunt.domain.tag.service

import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse

interface TagService {
    fun getTags(eventId: Long?): List<TagResponse>
    fun addTag(eventId: Long?, tagAddRequest: TagAddRequest): TagResponse
    fun deleteTags(eventId: Long?)
    fun deleteTag(eventId: Long?, tagId: Long?)
    fun getPopularTags(): List<TagResponse>
}