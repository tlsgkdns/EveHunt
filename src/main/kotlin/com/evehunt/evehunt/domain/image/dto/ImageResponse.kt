package com.evehunt.evehunt.domain.image.dto

data class ImageResponse(
    val uuid: String,
    val fileName: String? = "default"
)
{
    fun getLink(): String
    {
        return uuid + "_" + fileName
    }
}
