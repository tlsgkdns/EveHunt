package com.evehunt.evehunt.domain.event.api

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.event.service.EventEntityService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.infra.aop.annotation.CheckEventLoginMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/events")
class EventController(
    private val eventEntityService: EventEntityService
) {
    @GetMapping("/{eventId}")
    fun getEvent(@PathVariable eventId: Long): ResponseEntity<EventResponse>
    {
        return eventEntityService.getEvent(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @GetMapping()
    fun getEvents(@RequestBody pageRequest: PageRequest, keyword: String?): ResponseEntity<PageResponse<EventResponse>>
    {
        return eventEntityService.getEvents(pageRequest, keyword).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun hostEvent(@RequestBody eventHostRequest: EventHostRequest, @AuthenticationPrincipal userDetails: UserDetails)
    {
        return eventEntityService.hostEvent(eventHostRequest, userDetails.username).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @CheckEventLoginMember
    @PatchMapping("/{eventId}")
    fun editEvent(@PathVariable eventId: Long, @RequestBody eventEditRequest: EventEditRequest)
    {
        return eventEntityService.editEvent(eventId, eventEditRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @CheckEventLoginMember
    @DeleteMapping("/{eventId}")
    fun closeEvent(@PathVariable eventId: Long): ResponseEntity<Long>
    {
        return eventEntityService.closeEvent(eventId).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }
}