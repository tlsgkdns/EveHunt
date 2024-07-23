package com.evehunt.evehunt.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: Long, val httpStatus: HttpStatus, val message: String)
{
    MODEL_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "해당 Model을 찾을 수 없습니다."),
    INVALID_IMAGE_NAME(1002, HttpStatus.BAD_REQUEST, "유효하지 않은 Image입니다."),
    ALREADY_EXIST_MODEL(1003, HttpStatus.BAD_REQUEST, "이미 존재합니다."),
    INVALID_EVENT(1004, HttpStatus.BAD_REQUEST, "이벤트에 참여할 수 없습니다."),
    FULL_CAPACITY(1005, HttpStatus.BAD_REQUEST, "더 이상 들어갈 수 없습니다."),
    UNMATCHED_VALUE(1006, HttpStatus.BAD_REQUEST, "값이 일치하지 않습니다."),
    INVALID_REQUEST(1007, HttpStatus.BAD_REQUEST, "유효한 값이 아닙니다."),
    UNAUTHORIZED_ACCESS(2001, HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    UNMATCHED_PASSWORD(2002, HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.")
}