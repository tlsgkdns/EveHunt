package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

data class UnMatchedValueException(
    val target1: String,
    val target2: String
): CustomException(
    errorCode = ErrorCode.UNMATCHED_VALUE,
    message = "$target1 와 $target2 가/이 일치하지 않습니다."
) {
    override fun log() {
        super.logger.error("값이 일치하지 않습니다.")
        super.log()
    }
}