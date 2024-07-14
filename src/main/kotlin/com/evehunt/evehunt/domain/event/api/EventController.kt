package com.evehunt.evehunt.domain.event.api

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.infra.aop.annotation.CheckEventLoginMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {
    @GetMapping("/{eventId}")
    fun getEvent(@PathVariable eventId: Long): ResponseEntity<EventResponse>
    {
        return eventService.getEvent(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @GetMapping()
    fun getEvents(pageRequest: PageRequest): ResponseEntity<PageResponse<EventResponse>>
    {
        return eventService.getEvents(pageRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun hostEvent(@RequestBody eventHostRequest: EventHostRequest, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<EventResponse>
    {
        return eventService.hostEvent(eventHostRequest, userDetails.username).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @CheckEventLoginMember
    @PatchMapping("/{eventId}")
    fun editEvent(@PathVariable eventId: Long, @RequestBody eventEditRequest: EventEditRequest)
    {
        return eventService.editEvent(eventId, eventEditRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @CheckEventLoginMember
    @DeleteMapping("/{eventId}")
    fun closeEvent(@PathVariable eventId: Long): ResponseEntity<Long>
    {
        return eventService.closeEvent(eventId).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }

    @GetMapping("/{eventId}/tags")
    fun getTags(@PathVariable eventId: Long): ResponseEntity<List<TagResponse>>
    {
        return eventService.getTags(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @GetMapping("/popular")
    fun getPopularEvents(): ResponseEntity<List<EventResponse>>
    {
        return eventService.getPopularEvent().let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}