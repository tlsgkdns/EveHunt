package com.evehunt.evehunt.domain.participateHistory.api

import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateEditRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participateHistory.service.ParticipateHistoryService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.infra.aop.annotation.CheckEventLoginMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events/{eventId}/participants")
class ParticipateHistoryController(
    private val participateHistoryService: ParticipateHistoryService
) {
    @GetMapping()
    fun getParticipateHistoryByEvent(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails):
            ResponseEntity<List<ParticipateResponse>>
    {
        return participateHistoryService.getParticipateHistoryByEvent(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun participateEvent(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails,
                         @RequestBody participateRequest: ParticipateRequest):
            ResponseEntity<ParticipateResponse>
    {
        return participateHistoryService.participateEvent(eventId, user.username, participateRequest).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @DeleteMapping()
    fun dropParticipantEvent(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails): ResponseEntity<Unit>
    {
        return participateHistoryService.resignEventParticipate(eventId, user.username).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }
    @CheckEventLoginMember
    @PatchMapping("/result")
    fun setEventResult(@PathVariable eventId: Long, @RequestBody eventWinnerRequest: EventWinnerRequest):
            ResponseEntity<List<ParticipateResponse>>
    {
        return participateHistoryService.setEventResult(eventId, eventWinnerRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PatchMapping()
    fun editEventAnswer(@PathVariable eventId: Long, @RequestBody editRequest: ParticipateEditRequest,
                        @AuthenticationPrincipal user: UserDetails)
    {
        return participateHistoryService.editParticipateAnswer(eventId, editRequest, user.username).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}