package com.evehunt.evehunt.domain.event.service

import com.evehunt.evehunt.domain.event.dto.EventEditRequest
import com.evehunt.evehunt.domain.event.dto.EventHostRequest
import com.evehunt.evehunt.domain.event.dto.EventResponse
import com.evehunt.evehunt.domain.mail.dto.MailRequest
import com.evehunt.evehunt.domain.mail.service.MailService
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.service.ParticipateHistoryService
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import com.evehunt.evehunt.domain.tag.dto.TagResponse
import com.evehunt.evehunt.domain.tag.service.TagService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.FullCapacityException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImpl(
    private val eventEntityService: EventEntityService,
    private val memberService: MemberService,
    private val mailService: MailService,
    private val participateHistoryService: ParticipateHistoryService,
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
    override fun editEvent(eventId: Long, eventEditRequest: EventEditRequest): EventResponse {
        return eventEntityService.editEvent(eventId, eventEditRequest)
    }

    @Transactional
    override fun hostEvent(eventHostRequest: EventHostRequest, username: String): EventResponse {
        val event = eventEntityService.hostEvent(eventHostRequest, username)
        // mailService.sendMail(username, MailRequest(eventHostTitleMessage, eventHostContentMessage(event.title)))
        val tagList = eventHostRequest.tagAddRequests
        tagList?.forEach {
            tagService.addTag(event.id!!, it)
        }
        return event
    }

    override fun getEvent(eventId: Long): EventResponse {
        val event = eventEntityService.getEvent(eventId)
        event.eventTags = tagService.getTags(eventId)
        return event
    }

    override fun getEvents(pageRequest: PageRequest): PageResponse<EventResponse> {
        return eventEntityService.getEvents(pageRequest)
    }

    override fun closeEvent(eventId: Long): Long {
        return eventEntityService.closeEvent(eventId)
    }

    override fun setExpiredEventsClose(): List<EventResponse> {
        val expiredEvents = eventEntityService.setExpiredEventsClose()
        expiredEvents.forEach { participateHistoryService.setParticipantsStatusWait(it.id) }
        return expiredEvents
    }

    @Transactional
    override fun participateEvent(
        eventId: Long,
        username: String,
        participateRequest: ParticipateRequest
    ): ParticipateResponse {
        lateinit var participateResponse: ParticipateResponse
        val event = getEvent(eventId)
        try {
            participateResponse = participateHistoryService.participateEvent(eventId, username, participateRequest)
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
    override fun resignEventParticipate(eventId: Long, username: String) {
        participateHistoryService.resignEventParticipate(eventId, username)
    }

    override fun getPopularEvent(): List<EventResponse> {
        return eventEntityService.getPopularEvent()
    }

    @Transactional
    override fun setEventResult(eventId: Long, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse> {
        val list = participateHistoryService.setEventResult(eventId, eventWinnerRequest)
        val event = eventEntityService.getEvent(eventId)
        for(participant in list)
        {
            val email = memberService.getMember(participant.memberId!!).email
            val resultMessage = if(participant.status == EventParticipateStatus.WIN) event.winMessage
                else resultLoseMessage(event.title)
            mailService.sendMail(email, MailRequest(resultTitleMessage, resultMessage))
        }
        return list
    }

    override fun getParticipateHistories(eventId: Long): List<ParticipateResponse> {
        return participateHistoryService.getParticipateHistoryByEvent(eventId)
    }

    override fun getParticipateHistory(eventId: Long, username: String): ParticipateResponse {
        return participateHistoryService.getParticipateHistory(eventId, username)
    }

    override fun getTags(eventId: Long): List<TagResponse> {
        return tagService.getTags(eventId)
    }
    override fun addTag(eventId: Long, tagAddRequest: TagAddRequest): TagResponse {
        return tagService.addTag(eventId, tagAddRequest)
    }
    override fun deleteTag(eventId: Long, tagId: Long) {
        return tagService.deleteTag(eventId, tagId)
    }
}