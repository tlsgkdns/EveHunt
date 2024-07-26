package com.evehunt.evehunt.domain.report.service

import com.evehunt.evehunt.domain.report.dto.ReportHandleRequest
import com.evehunt.evehunt.domain.report.dto.ReportRequest
import com.evehunt.evehunt.domain.report.dto.ReportResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse

interface ReportEntityService {
    fun createReport(reportRequest: ReportRequest, username: String): ReportResponse
    fun getReports(pageRequest: PageRequest): PageResponse<ReportResponse>
    fun handleReport(reportId: Long, reportHandleRequest: ReportHandleRequest): ReportResponse
}