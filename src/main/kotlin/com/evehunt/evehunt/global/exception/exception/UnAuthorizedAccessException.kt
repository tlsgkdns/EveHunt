package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class UnAuthorizedAccessException(
    val id: String
): CustomException(
    errorCode = ErrorCode.UNAUTHORIZED_ACCESS,
    message = "${id}에 접근할 수 없습니다."
) {
    override fun log() {
        super.logger.error("접근 권한이 없습니다.")
        super.log()
    }
}