package com.evehunt.evehunt.domain.tag.service

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.domain.tag.model.Tag
import com.evehunt.evehunt.domain.tag.repository.TagRepository
import com.evehunt.evehunt.global.exception.exception.EventHasFullTagException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TagServiceImpl(
    @Value("\${event.tag.capacity}")
    private val tagCapacity: Int,
    private val eventRepository: EventRepository,
    private val tagRepository: TagRepository
): TagService{

    private fun getExistEvent(eventId: Long): Event
    {
        return eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId.toString())
    }
    private fun getExistTag(tagId: Long): Tag
    {
        return tagRepository.findByIdOrNull(tagId)
            ?: throw ModelNotFoundException("Tag", tagId.toString())
    }
    override fun getTags(eventId: Long): List<TagResponse> {
        return tagRepository.getTagsByEvent(eventId).map {
            TagResponse.from(it)
        }
    }

    override fun addTag(eventId: Long, tagAddRequest: TagAddRequest): TagResponse {
        val event = getExistEvent(eventId)
        if(getTags(eventId).size >= tagCapacity) throw EventHasFullTagException(event.title)
        val tag = tagRepository.save(tagAddRequest.to(event))
        return TagResponse.from(tag)
    }

    override fun deleteTags(eventId: Long)
    {
        tagRepository.deleteTagsByEvent(eventId)
    }

    override fun deleteTag(eventId: Long, tagId: Long) {
        getExistEvent(eventId)
        val tag = getExistTag(tagId)
        tagRepository.delete(tag)
    }
}