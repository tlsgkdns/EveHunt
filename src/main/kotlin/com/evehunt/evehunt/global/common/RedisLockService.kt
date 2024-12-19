package com.evehunt.evehunt.global.common

import com.evehunt.evehunt.global.exception.exception.RedisLockTimeoutException
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.util.concurrent.TimeUnit

@Service
class RedisLockService(
    private val redissonClient: RedissonClient,
    private val transactionManager: PlatformTransactionManager
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
        val status = transactionManager.getTransaction(DefaultTransactionDefinition())
        val available : Boolean = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)
        try {
            if(!available){
                throw RedisLockTimeoutException(lockName)
            }
            try {
                val ret = task()
                transactionManager.commit(status)
                return ret
            } catch (e: RuntimeException) {
                transactionManager.rollback(status)
                throw RedisLockTimeoutException(lockName)
            }
        } finally {
                if(rLock.isHeldByCurrentThread){
                    rLock.unlock()
                } else{
                    throw RedisLockTimeoutException(lockName)
                }
            }
        }
    }