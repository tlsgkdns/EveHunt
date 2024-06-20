package com.evehunt.evehunt.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: Long, val httpStatus: HttpStatus, val message: String)
{
    MODEL_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "해당 Model을 찾을 수 없습니다."),
    INVALID_IMAGE_NAME(1002, HttpStatus.BAD_REQUEST, "유효하지 않은 Image입니다."),
    ALREADY_EXIST_MODEL(1003, HttpStatus.BAD_REQUEST, "이미 존재합니다.")
}