package com.evehunt.evehunt.domain.report.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.report.model.Report
import com.evehunt.evehunt.domain.report.model.ReportStatus

data class ReportResponse(
    val reportId: Long?,
    val eventTitle: String?,
    val reporterName: String?,
    val eventId: Long?,
    val reporterId: Long?,
    val reason: String,
    val processed: ReportStatus
)
{
    companion object
    {
        fun from(report: Report): ReportResponse
        {
            return ReportResponse(
                reportId = report.id,
                eventId = report.event?.id,
                eventTitle = report.event?.title,
                reporterId = report.reporter?.id,
                reporterName = report.reporter?.name,
                reason = report.reason,
                processed = report.processed
            )
        }
    }
}