package com.evehunt.evehunt.global.exception

import com.evehunt.evehunt.global.exception.exception.CustomException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomException::class)
    fun customExceptionHandler(e: CustomException)
        = e.getErrorResponse()
}