package com.evehunt.evehunt.domain.report.repository

import com.evehunt.evehunt.domain.report.model.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long>, QueryDslReportRepository {
    fun existsByReporterEmailAndEventId(email: String, eventId: Long?): Boolean
}