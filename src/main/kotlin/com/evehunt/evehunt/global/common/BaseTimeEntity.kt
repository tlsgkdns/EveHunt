package com.evehunt.evehunt.global.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {
    @CreationTimestamp
    @Column(nullable = false)
    open var createdAt: ZonedDateTime = ZonedDateTime.now()

    @UpdateTimestamp
    @Column(nullable = false)
    open var updatedAt: ZonedDateTime = ZonedDateTime.now()
}