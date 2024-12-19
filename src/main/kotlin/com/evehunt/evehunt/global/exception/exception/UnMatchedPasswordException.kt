package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class UnMatchedPasswordException(): CustomException(
    errorCode = ErrorCode.UNAUTHORIZED_ACCESS,
    message = "비밀번호가 일치하지 않습니다."
) {
    override fun log() {
        super.logger.error("비밀번호가 일치하지 않습니다.")
        super.log()
    }
}
