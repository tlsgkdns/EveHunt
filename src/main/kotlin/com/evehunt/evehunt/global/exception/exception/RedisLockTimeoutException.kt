package com.evehunt.evehunt.global.exception.exception

import com.evehunt.evehunt.global.exception.ErrorCode

class RedisLockTimeoutException(
    lockName: String
): CustomException(
    message = "${lockName}에서 Lock을 획득 실패하였습니다.",
    errorCode = ErrorCode.REDIS_LOCK_TIMEOUT
)
{
    override fun log() {
        super.logger.error("Lock을 획득 실패했습니다.")
        message?.let { super.logger.info(it) }
    }
}