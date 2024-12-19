package com.evehunt.evehunt.global.common

import org.springframework.data.jpa.repository.Query

interface NamedLockWithJpaRepository {
    @Query(value = "select GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
    fun getLock(key: String, timeoutSeconds: Int): Long?
    @Query(value = "select RELEASE_LOCK(:key)", nativeQuery = true)
    fun releaseLock(key: String) : Long?
}