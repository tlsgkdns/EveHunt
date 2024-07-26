package com.evehunt.evehunt.domain.report.model

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Report(
    @ManyToOne(cascade = [CascadeType.ALL])
    val reporter: Member?,
    @ManyToOne(cascade = [CascadeType.ALL])
    val event: Event?,
    val reason: String
): BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    var processed: ReportStatus = ReportStatus.WAIT
    fun setProcess(accept: Boolean)
    {
        processed = if(accept) ReportStatus.ACCEPT
        else ReportStatus.REJECT
    }
}