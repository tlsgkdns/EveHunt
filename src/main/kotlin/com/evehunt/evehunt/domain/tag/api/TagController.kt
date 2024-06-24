package com.evehunt.evehunt.domain.tag.api

import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.domain.tag.service.TagService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("events/{eventId}/tags")
class TagController(
    private val tagService: TagService
) {
    @GetMapping()
    fun getTags(@PathVariable eventId: Long): ResponseEntity<List<TagResponse>>
    {
        return tagService.getTags(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @PostMapping()
    fun addTag(@PathVariable eventId: Long, @RequestBody tagAddRequest: TagAddRequest): ResponseEntity<TagResponse>
    {
        return tagService.addTag(eventId, tagAddRequest).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @DeleteMapping()
    fun deleteTags(@PathVariable eventId: Long): ResponseEntity<Unit>
    {
        return tagService.deleteTags(eventId).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }
    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable eventId: Long, @PathVariable tagId: Long): ResponseEntity<Unit>
    {
        return tagService.deleteTag(eventId, tagId).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }
}