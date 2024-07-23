package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class InvalidRequestException(
    private val modelName: String,
    messages: String
): CustomException(message = messages, errorCode = ErrorCode.INVALID_REQUEST) {

    override fun log() {
        super.logger.error("${modelName}에 적합하지 않는 요청입니다.")
        message?.let { super.logger.info(it) }
    }
}