package com.evehunt.evehunt.domain.event.api

import com.evehunt.evehunt.domain.event.dto.EventCardResponse
import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.InvalidRequestException
import com.evehunt.evehunt.global.infra.aop.annotation.CheckEventLoginMember
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {
    fun checkBindingResult(bindingResult: BindingResult)
    {
        if(bindingResult.hasErrors()) {
            var messages = ""
            val list = bindingResult.fieldErrors
            for ((idx, message) in list.map { it.defaultMessage }.withIndex())
            {
                messages += message
                if(idx != list.size - 1)
                    messages += '\n'
            }
            throw InvalidRequestException("Event", messages)
        }
    }
    @GetMapping("/{eventId}")
    fun getEvent(@PathVariable eventId: Long): ResponseEntity<EventResponse>
    {
        return eventService.getEvent(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @GetMapping()
    fun getEvents(pageRequest: PageRequest): ResponseEntity<PageResponse<EventCardResponse>>
    {
        return eventService.getEvents(pageRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PostMapping()
    fun hostEvent(@RequestBody @Valid eventHostRequest: EventHostRequest, bindingResult: BindingResult,
                  @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<EventResponse>
    {
        checkBindingResult(bindingResult)
        return eventService.hostEvent(eventHostRequest, userDetails.username).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }
    @CheckEventLoginMember
    @PatchMapping("/{eventId}")
    fun editEvent(@PathVariable eventId: Long, @RequestBody @Valid eventEditRequest: EventEditRequest, bindingResult: BindingResult)
    {
        checkBindingResult(bindingResult)
        return eventService.editEvent(eventId, eventEditRequest).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
    @PatchMapping("/{eventId}/close")
    fun closeEvent(@PathVariable eventId: Long): ResponseEntity<EventResponse>
    {
        return eventService.closeEvent(eventId).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
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
    fun getPopularEvents(): ResponseEntity<List<EventCardResponse>>
    {
        return eventService.getPopularEvent().let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }
}