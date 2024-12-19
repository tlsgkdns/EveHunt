package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class AlreadyExistModelException(
    id: String
): CustomException(
    message = "${id}는 이미 존재합니다.",
    errorCode = ErrorCode.ALREADY_EXIST_MODEL
) {
    override fun log() {
        super.logger.error("이미 존재합니다.")
        message?.let { super.logger.info(it) }
    }
}