package com.evehunt.evehunt.domain.image.dto

import org.springframework.web.multipart.MultipartFile

data class ImageRequest(
    val file: MultipartFile?
)