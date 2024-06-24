package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class FullCapacityException(
    val name: String,
    val id: String,
    val capacity: Int,
): CustomException(
    errorCode = ErrorCode.FULL_CAPACITY,
    message = "${name}의 ${id}에서 ${capacity}의 용량이 초과하였습니다."
) {
    override fun log() {
        super.logger.error("용량이 초과하였습니다.")
        message?.let { super.logger.info(it) }
    }
}