package com.evehunt.evehunt.domain.tag.api

import com.evehunt.evehunt.domain.tag.service.TagService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {
    @GetMapping("/popular")
    fun getPopularTags()
    {

    }
}