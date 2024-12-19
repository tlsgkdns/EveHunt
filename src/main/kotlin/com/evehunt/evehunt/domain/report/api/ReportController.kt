package com.evehunt.evehunt.domain.report.api

import com.evehunt.evehunt.domain.report.dto.ReportHandleRequest
import com.evehunt.evehunt.domain.report.dto.ReportRequest
import com.evehunt.evehunt.domain.report.dto.ReportResponse
import com.evehunt.evehunt.domain.report.service.ReportService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val reportService: ReportService
) {
    @PostMapping()
    fun createReport(@RequestBody reportRequest: ReportRequest, @AuthenticationPrincipal userDetails: UserDetails):
            ResponseEntity<ReportResponse>
    {
        return reportService.createReport(reportRequest, userDetails.username).let {
                ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }

    @GetMapping()
    fun getReports(pageRequest: PageRequest):
            ResponseEntity<PageResponse<ReportResponse>>
    {
        return reportService.getReports(pageRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @PatchMapping("/{reportId}")
    fun processReport(@PathVariable reportId: Long,
                      @RequestBody reportHandleRequest: ReportHandleRequest): ResponseEntity<ReportResponse>
    {
        return reportService.handleReport(reportId, reportHandleRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}