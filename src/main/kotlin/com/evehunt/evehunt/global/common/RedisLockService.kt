package com.evehunt.evehunt.global.common

import com.evehunt.evehunt.global.exception.exception.RedisLockTimeoutException
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisLockService(
    private val redissonClient: RedissonClient
) {
    @Value("\${lock.wait-time}")
    var waitTime: Long = 0
    @Value("\${lock.lease-time}")
    var leaseTime: Long = 0
    @Value("\${lock.prefix}")
    lateinit var lockPrefix: String

    fun <R> tryLockWith(
        lockName: String,
        task: () -> R
    ): R = tryLockWith(
        lockName = lockName,
        waitTime = waitTime,
        leaseTime = leaseTime,
        task = task
    )

    fun <R> tryLockWith(
        lockName: String,
        waitTime: Long,
        leaseTime: Long,
        task: () -> R
    ): R {
        val rLock: RLock = redissonClient.getLock(lockPrefix + lockName)
        val available : Boolean = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)
        if(!available){
            throw RedisLockTimeoutException(lockName)
        }
        try {
            return task()
        } finally {
            if(rLock.isHeldByCurrentThread){
                rLock.unlock()
            } else{
                throw RedisLockTimeoutException(lockName)
            }
        }
    }
}