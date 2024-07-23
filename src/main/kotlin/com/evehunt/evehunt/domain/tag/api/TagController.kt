package com.evehunt.evehunt.domain.tag.api

import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.domain.tag.service.TagService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {
    @GetMapping("/popular")
    fun getPopularTags(): ResponseEntity<List<TagResponse>>
    {
        return tagService.getPopularTags().let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}