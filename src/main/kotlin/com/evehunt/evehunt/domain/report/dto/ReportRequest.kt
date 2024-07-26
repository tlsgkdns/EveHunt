package com.evehunt.evehunt.domain.report.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.report.model.Report

data class ReportRequest(
    val eventId: Long?,
    val reason: String
)
{
    fun to(member: Member, event: Event): Report
    {
        return Report(
            reporter = member,
            event = event,
            reason = reason
        )
    }
}