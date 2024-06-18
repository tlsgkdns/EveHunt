package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class InvalidImageNameException: CustomException(
    message = "유효하지 않은 Image 이름입니다.",
    errorCode = ErrorCode.INVALID_IMAGE_NAME
)
{
    override fun log() {
        super.logger.error("유효하지 않은 Image 이름입니다.")
        message?.let { super.logger.info(it) }
    }
}