package com.evehunt.evehunt.domain.participant.api

import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events/{eventId}/participants")
class ParticipantController(
    private val eventService: EventService
) {
    @GetMapping()
    fun getParticipant(@PathVariable eventId: Long):
            ResponseEntity<List<ParticipateResponse>>
    {
        return eventService.getParticipants(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun participantEvents(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails,
                         @RequestBody participateRequest: ParticipateRequest):
            ResponseEntity<ParticipateResponse>
    {
        return eventService.participateEvent(eventId, user.username, participateRequest).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @DeleteMapping()
    fun dropParticipantEvent(@PathVariable eventId: Long, @AuthenticationPrincipal user: UserDetails): ResponseEntity<Unit>
    {
        return eventService.resignEventParticipate(eventId, user.username).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }

    @PatchMapping("/result")
    fun setEventResult(@PathVariable eventId: Long, @RequestBody eventWinnerRequest: EventWinnerRequest):
            ResponseEntity<List<ParticipateResponse>>
    {
        return eventService.setEventResult(eventId, eventWinnerRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}