package com.evehunt.evehunt.global.common

import com.evehunt.evehunt.domain.member.model.Member
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@MappedSuperclass
@EnableJpaAuditing
abstract class CreatorAuditEntity: BaseTimeEntity()
{
    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    @JoinColumn(name = "creator", referencedColumnName = "email")
    open var creator: Member? = null
}