package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class EventHasFullTagException(
    eventTitle: String
): CustomException(
    errorCode = ErrorCode.EVENT_HAS_FULL_TAG,
    message = "${eventTitle}에 이 이상 태그를 추가할 수 없습니다."
) {
    override fun log() {
        super.logger.error("태그가 꽉 찼습니다.")
        message?.let { super.logger.info(it) }
    }
}