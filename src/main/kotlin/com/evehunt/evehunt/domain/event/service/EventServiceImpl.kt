package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.*
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.domain.mail.service.MailService
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus
import com.evehunt.evehunt.domain.participant.service.ParticipantService
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.domain.tag.service.TagService
import com.evehunt.evehunt.global.common.RedisLockService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.InvalidEventException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImpl(
    private val eventEntityService: EventEntityService,
    private val memberService: MemberService,
    private val mailService: MailService,
    private val participantService: ParticipantService,
    private val tagService: TagService
): EventService {

    private final val resultTitleMessage = "이벤트 결과 안내드립니다."
    private final fun resultLoseMessage(title: String) = "${title}에 당첨되지 않았습니다."
    @Transactional
    override fun editEvent(eventId: Long?, eventEditRequest: EventEditRequest): EventResponse {
        tagService.deleteTags(eventId)
        eventEditRequest.tagAddRequests?.forEach {
            tagService.addTag(eventId, it)
        }
        return eventEntityService.editEvent(eventId, eventEditRequest)
    }

    @Transactional
    override fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse {
        val event = eventEntityService.hostEvent(eventHostRequest, username)
        val tagList = eventHostRequest.tagAddRequests
        tagList?.forEach {
            tagService.addTag(event.id!!, it)
        }
        return event
    }

    override fun getEvent(eventId: Long?): EventResponse {
        val event = eventEntityService.getEvent(eventId)
        event.eventTags = tagService.getTags(eventId)
        event.participantCount = participantService.getParticipantCount(eventId)
        return event
    }

    override fun getEvents(pageRequest: PageRequest): PageResponse<EventCardResponse>
        = eventEntityService.getEvents(pageRequest)

    override fun closeEvent(eventId: Long?): EventResponse =
        eventEntityService.closeEvent(eventId)

    override fun setExpiredEventsClose(): List<EventIdResponse> {
        val expiredEvents = eventEntityService.setExpiredEventsClose()
        expiredEvents.forEach { participantService.setParticipantsStatusWait(it.id) }
        return expiredEvents
    }

    override fun participateEvent(
        eventId: Long?,
        username: String,
        participateRequest: ParticipateRequest
    ): ParticipateResponse {
        val event = getEvent(eventId)
        if(event.status != EventStatus.PROCEED) throw InvalidEventException(eventId)
        return participantService.participateEvent(eventId, username, participateRequest)
    }

    @Transactional
    override fun resignEventParticipate(eventId: Long?, username: String) {
        participantService.resignEventParticipate(eventId, username)
    }

    override fun getPopularEvent(): List<EventCardResponse> {
        return eventEntityService.getPopularEvent()
    }

    @Transactional
    override fun setEventResult(eventId: Long?, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse> {
        val list = participantService.setEventResult(eventId, eventWinnerRequest)
        val title = eventEntityService.getEvent(eventId).title
        for(participant in list)
        {
            val email = memberService.getMember(participant.memberId!!).email
            val resultMessage = if(participant.status == ParticipantStatus.LOSE) resultLoseMessage(title)
            else
            {
                eventWinnerRequest.let {
                    it.winMessages[it.eventWinners.indexOf(participant.id)]
                }
            }
            mailService.sendMail(email, MailRequest(resultTitleMessage, resultMessage))
        }
        eventEntityService.announceEvent(eventId)
        return list
    }

    override fun getParticipants(eventId: Long?): List<ParticipateResponse> {
        return participantService.getParticipantsByEvent(eventId)
    }

    override fun getParticipant(eventId: Long?, username: String): ParticipateResponse {
        return participantService.getParticipant(eventId, username)
    }

    override fun getTags(eventId: Long?): List<TagResponse> {
        return tagService.getTags(eventId)
    }
    override fun addTag(eventId: Long?, tagAddRequest: TagAddRequest): TagResponse {
        return tagService.addTag(eventId, tagAddRequest)
    }
    override fun deleteTag(eventId: Long?, tagId: Long?) {
        return tagService.deleteTag(eventId, tagId)
    }
}