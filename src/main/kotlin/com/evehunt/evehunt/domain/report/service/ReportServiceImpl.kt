package com.evehunt.evehunt.domain.report.service

import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.report.dto.ReportHandleRequest
import com.evehunt.evehunt.domain.report.dto.ReportRequest
import com.evehunt.evehunt.domain.report.dto.ReportResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportServiceImpl(
    private val reportEntityService: ReportEntityService,
    private val eventService: EventService,
    private val memberService: MemberService
): ReportService {
    @Transactional
    override fun createReport(reportRequest: ReportRequest, username: String): ReportResponse {
        return reportEntityService.createReport(reportRequest, username)
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    override fun getReports(pageRequest: PageRequest): PageResponse<ReportResponse> {
        return reportEntityService.getReports(pageRequest)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    override fun handleReport(reportId: Long, reportHandleRequest: ReportHandleRequest): ReportResponse {
        val report = reportEntityService.handleReport(reportId, reportHandleRequest)
        if(reportHandleRequest.accepted)
        {
            eventService.closeEvent(report.eventId)
            val hostId = eventService.getEvent(report.eventId).hostId
            memberService.suspendMember(hostId, reportHandleRequest.suspendDay!!)
        }
        return report
    }
}