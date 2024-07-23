package com.evehunt.evehunt.domain.tag.dto

import com.evehunt.evehunt.domain.tag.model.Tag

data class TagResponse(
    val tagName: String?
)
{
    companion object
    {
        fun from(tag: Tag): TagResponse
        {
            return TagResponse(tag.tagName)
        }
    }

}