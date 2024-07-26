package com.evehunt.evehunt.domain.report.repository

import com.evehunt.evehunt.domain.report.model.Report
import com.evehunt.evehunt.global.common.page.PageRequest
import org.springframework.data.domain.Page

interface QueryDslReportRepository {
    fun searchReport(pageRequest: PageRequest): Page<Report>
}