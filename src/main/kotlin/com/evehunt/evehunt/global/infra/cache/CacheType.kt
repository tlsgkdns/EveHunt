package com.evehunt.evehunt.global.infra.cache

enum class CacheType(
    val cacheName: String,
    val expireTime: Long,
    val maxSize: Long
) {
    POPULAR_TAGS("popularTags", 2000, 1000),
    POPULAR_EVENTS("popularEvents", 2000, 10000),
    EVENTS_LIST("eventList", 30, 10000),
    EVENT_TAGS("eventTags", 30, 10000)
}