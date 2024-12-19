package com.evehunt.evehunt.domain.report.model

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class Report(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    val reporter: Member?,
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    val event: Event?,
    val reason: String,
    @Enumerated(EnumType.STRING)
    var processed: ReportStatus = ReportStatus.WAIT
): BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun setProcess(accept: Boolean)
    {
        processed = if(accept) ReportStatus.ACCEPT
        else ReportStatus.REJECT
    }
}