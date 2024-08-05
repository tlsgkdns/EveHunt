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
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.FullCapacityException
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
    private final val eventHostTitleMessage = "이벤트를 성공적으로 개최했습니다."
    private final val eventParticipateSuccessTitleMessage = "이벤트에 성공적으로 참여하였습니다."
    private final val eventParticipateFailTitleMessage = "이벤트에 참여하지 못했습니다."
    private final fun resultLoseMessage(title: String) = "${title}에 당첨되지 않았습니다."
    private final fun eventHostContentMessage(title: String) = "${title}를 성공적으로 개최하였습니다."
    private final fun eventParticipateSuccessMessage(title: String) = "${title}에 성공적으로 참여하였습니다."
    private final fun eventParticipateFailMessage(title: String) = "${title}에 참여하지 못했습니다."

    private fun getEmail(memberId: Long?): String
        = memberService.getMember(memberId).email
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
        mailService.sendMail(username, MailRequest(eventHostTitleMessage, eventHostContentMessage(event.title)))
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

    override fun getEvents(pageRequest: PageRequest): PageResponse<EventCardResponse> {
        return eventEntityService.getEvents(pageRequest)
    }

    override fun closeEvent(eventId: Long?): EventResponse {
        return eventEntityService.closeEvent(eventId)
    }

    override fun setExpiredEventsClose(): List<EventIdResponse> {
        val expiredEvents = eventEntityService.setExpiredEventsClose()
        expiredEvents.forEach { participantService.setParticipantsStatusWait(it.id) }
        return expiredEvents
    }

    @Transactional
    override fun participateEvent(
        eventId: Long?,
        username: String,
        participateRequest: ParticipateRequest
    ): ParticipateResponse {
        lateinit var participateResponse: ParticipateResponse
        val event = getEvent(eventId)
        if(event.status != EventStatus.PROCEED) throw InvalidEventException(eventId)
        try {
            participateResponse = participantService.participateEvent(eventId, username, participateRequest)
        } catch (e: FullCapacityException)
        {

            mailService.sendMail(getEmail(participateResponse.memberId),
                MailRequest(eventParticipateFailTitleMessage, eventParticipateFailMessage(event.title)))

        }
        mailService.sendMail(getEmail(participateResponse.memberId),
            MailRequest(eventParticipateSuccessTitleMessage, eventParticipateSuccessMessage(event.title)))
        return participateResponse
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