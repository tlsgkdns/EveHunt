package com.evehunt.evehunt.domain.report.repository

import com.evehunt.evehunt.domain.report.model.QReport
import com.evehunt.evehunt.domain.report.model.Report
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

class QueryDslReportRepositoryImpl: QueryDslReportRepository, QueryDslSupport() {
    val report: QReport = QReport.report
    override fun searchReport(pageRequest: PageRequest): Page<Report> {
        val pageable = pageRequest.getPageable()
        val whereClause = BooleanBuilder()
        val orderBy = when(pageRequest.sortType?.lowercase()) {
            "reporter" -> {
                if(pageRequest.asc == false) report.reporter.name.desc()
                else report.reporter.name.asc()
            }

            "event" -> {
                if(pageRequest.asc == false) report.event.title.desc()
                else report.event.title.asc()
            }

            else -> {
                if(pageRequest.asc == false) report.createdAt.desc()
                else report.createdAt.asc()
            }
        }
        if (pageRequest.keyword != null)
        {
            when(pageRequest.searchType?.lowercase())
            {
                "reporter" -> {
                    whereClause.or(report.reporter.name.contains(pageRequest.keyword))
                }
                "event" -> {
                    whereClause.or(report.event.title.contains(pageRequest.keyword))
                }
                else -> {
                    whereClause.or(report.reason.contains(pageRequest.keyword))
                }
            }
        }
        val content = queryFactory.selectFrom(report)
            .where(whereClause)
            .orderBy(orderBy)
        val cnt = content.fetch().size.toLong()
        return PageImpl(content.offset(pageable.offset).limit(pageable.pageSize.toLong()).fetch(), pageable, cnt)
    }
}