package com.evehunt.evehunt.global.exception.dto

data class ErrorResponse(
    val message: String,
    val errorCode: Long
)