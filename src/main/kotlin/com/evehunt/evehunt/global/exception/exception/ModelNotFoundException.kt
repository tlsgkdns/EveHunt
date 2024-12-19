package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class ModelNotFoundException(
    modelName: String,
    findName: String?
): CustomException(
    message = "$modelName 에서 $findName 을/를 찾을 수 없습니다.",
    errorCode = ErrorCode.MODEL_NOT_FOUND
)
{
    override fun log() {
        super.logger.error("대상 Entity를 찾지 못했습니다.")
        message?.let { super.logger.info(it) }
    }
}