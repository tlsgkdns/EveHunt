package com.evehunt.evehunt.domain.tag.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.tag.model.Tag

data class TagAddRequest(
    val tagName: String
)
{
    fun to(event: Event): Tag
    {
        return Tag(
            event = event,
            tagName = tagName
        )
    }
}
