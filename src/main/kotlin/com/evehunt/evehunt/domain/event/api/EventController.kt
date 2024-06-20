package com.evehunt.evehunt.domain.event.api

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.global.common.PageRequest
import com.evehunt.evehunt.global.common.PageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/events")
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
    fun getEvents(pageRequest: PageRequest, keyword: String?): ResponseEntity<PageResponse<EventResponse>>
    {
        return eventService.getEvents(pageRequest, keyword).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun hostEvent(eventHostRequest: EventHostRequest)
    {
        return eventService.hostEvent(eventHostRequest).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @PatchMapping("/{eventId}")
    fun editEvent(@PathVariable eventId: Long, eventEditRequest: EventEditRequest)
    {
        return eventService.editEvent(eventId, eventEditRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @DeleteMapping("/{eventId}")
    fun closeEvent(@PathVariable eventId: Long): ResponseEntity<Long>
    {
        return eventService.closeEvent(eventId).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(it)
        }
    }
}