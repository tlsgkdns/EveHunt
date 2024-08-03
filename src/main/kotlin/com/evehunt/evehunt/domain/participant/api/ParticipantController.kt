package com.evehunt.evehunt.domain.participant.api

import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateEditRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participant.service.ParticipantService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events/{eventId}/participants")
class ParticipantController(
    private val participantService: ParticipantService
) {
    @GetMapping()
    fun getParticipant(@PathVariable eventId: Long):
            ResponseEntity<List<ParticipateResponse>>
    {
        return participantService.getParticipantsByEvent(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun participantEvents(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails,
                         @RequestBody participateRequest: ParticipateRequest):
            ResponseEntity<ParticipateResponse>
    {
        return participantService.participateEvent(eventId, user.username, participateRequest).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @DeleteMapping()
    fun dropParticipantEvent(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails): ResponseEntity<Unit>
    {
        return participantService.resignEventParticipate(eventId, user.username).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }

    @PatchMapping("/result")
    fun setEventResult(@PathVariable eventId: Long, @RequestBody eventWinnerRequest: EventWinnerRequest):
            ResponseEntity<List<ParticipateResponse>>
    {
        return participantService.setEventResult(eventId, eventWinnerRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PatchMapping()
    fun editEventAnswer(@PathVariable eventId: Long, @RequestBody editRequest: ParticipateEditRequest,
                        @AuthenticationPrincipal user: UserDetails)
    {
        return participantService.editParticipateAnswer(eventId, editRequest, user.username).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @GetMapping("/count")
    fun getParticipantsCount(@PathVariable eventId: Long): ResponseEntity<Int>
    {
        return participantService.getParticipantCount(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}