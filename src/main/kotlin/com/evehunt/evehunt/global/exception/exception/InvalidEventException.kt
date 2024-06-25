package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class InvalidEventException(
    val eventId: Long
): CustomException(
    errorCode = ErrorCode.INVALID_EVENT,
    message = "${eventId}에 참여할 수 없습니다."
) {
    override fun log() {
        super.logger.error("이벤트에 참여할 수 없습니다.")
        message?.let { super.logger.info(it) }
    }
}