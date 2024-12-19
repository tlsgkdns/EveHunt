package com.evehunt.evehunt.domain.report.service

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import com.evehunt.evehunt.domain.report.dto.ReportHandleRequest
import com.evehunt.evehunt.domain.report.dto.ReportRequest
import com.evehunt.evehunt.domain.report.dto.ReportResponse
import com.evehunt.evehunt.domain.report.model.Report
import com.evehunt.evehunt.domain.report.repository.ReportRepository
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.AlreadyExistModelException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportEntityServiceImpl(
    private val reportRepository: ReportRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository
): ReportEntityService {
    fun getExistMember(username: String): Member
    {
        return memberRepository.findMemberByEmail(username)
            ?: throw ModelNotFoundException("Member", username)
    }
    fun getExistEvent(eventId: Long?): Event
    {
        return eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId.toString())
    }
    fun getExistReport(reportId: Long?): Report
    {
        return reportRepository.findByIdOrNull(reportId)
            ?: throw ModelNotFoundException("Report", reportId.toString())
    }
    @Transactional
    override fun createReport(reportRequest: ReportRequest, username: String): ReportResponse {
        if(reportRepository.existsByReporterEmailAndEventId(username, reportRequest.eventId))
            throw AlreadyExistModelException("Report")
        val event = getExistEvent(reportRequest.eventId)
        val member = getExistMember(username)
        val report = reportRequest.to(member, event)
        return reportRepository.save(report).let {
            ReportResponse.from(it)
        }
    }
    @Transactional
    override fun handleReport(reportId: Long, reportHandleRequest: ReportHandleRequest): ReportResponse {
        val report = getExistReport(reportId)
        report.setProcess(reportHandleRequest.accepted)
        return reportRepository.save(report).let {
            ReportResponse.from(it)
        }
    }
    override fun getReports(pageRequest: PageRequest): PageResponse<ReportResponse> {
        val reportPage = reportRepository.searchReport(pageRequest)
        val content = reportPage.content.map { ReportResponse.from(it) }
        return PageResponse.of(pageRequest, content, reportPage.totalElements.toInt())
    }
}