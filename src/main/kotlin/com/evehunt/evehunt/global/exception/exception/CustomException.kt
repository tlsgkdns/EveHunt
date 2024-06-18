package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode
import com.evehunt.evehunt.global.exception.dto.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CustomException(
    override val message: String? = null,
    val errorCode: ErrorCode
) : RuntimeException() {

    open val logger: Logger = LoggerFactory.getLogger(this::class.java)

    open fun log(){}

    open fun getErrorResponse() =
        ErrorResponse(
            message = message?.let { message } ?: errorCode.message,
            errorCode = errorCode.code
        )
}