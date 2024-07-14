package com.evehunt.evehunt.domain.participateHistory.service

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateEditRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participateHistory.model.EventParticipateStatus
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory
import com.evehunt.evehunt.domain.participateHistory.model.strategy.PickWinnerBinarySearch
import com.evehunt.evehunt.domain.participateHistory.model.strategy.PickWinnerStrategy
import com.evehunt.evehunt.domain.participateHistory.repository.ParticipateHistoryRepository
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.AlreadyExistModelException
import com.evehunt.evehunt.global.exception.exception.FullCapacityException
import com.evehunt.evehunt.global.exception.exception.InvalidEventException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParticipateHistoryServiceImpl(
    private val participateHistoryRepository: ParticipateHistoryRepository,
    private val eventRepository: EventRepository,
    private val memberRepository: MemberRepository
): ParticipateHistoryService {
    val winPickWinnerStrategy: PickWinnerStrategy = PickWinnerBinarySearch()
    fun getExistEvent(eventId: Long): Event
    {
        return eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId.toString())
    }
    fun getExistParticipateHistory(eventId: Long, memberEmail: String): ParticipateHistory
    {
        return participateHistoryRepository.getParticipateHistory(eventId, memberEmail)
            ?: throw ModelNotFoundException("Participate", memberEmail)
    }
    override fun participateEvent(eventId: Long, username: String, participateRequest: ParticipateRequest): ParticipateResponse {
        lateinit var participateHistoryResponse: ParticipateResponse
        if(participateHistoryRepository.getParticipateHistory(eventId, username) != null)
            throw AlreadyExistModelException(eventId.toString())
        val event = getExistEvent(eventId)
        val member = memberRepository.findMemberByEmail(username)
        try {
            participateHistoryRepository.getLock("Lock $eventId", 3000)
            if(event.eventStatus != EventStatus.PROCEED) throw InvalidEventException(eventId)
            if(getParticipateHistoryByEvent(eventId).size + 1 > event.capacity)
                throw FullCapacityException("Event", eventId.toString(), event.capacity)
            val participateHistory = participateRequest.to(event, member)
            participateHistoryResponse = participateHistoryRepository.save(participateHistory)
                .let { ParticipateResponse.from(it) }
        } finally {
            participateHistoryRepository.releaseLock("Lock $eventId")
        }
        return participateHistoryResponse
    }

    @Transactional
    override fun getParticipateHistory(eventId: Long, username: String): ParticipateResponse {
        return getExistParticipateHistory(eventId, username)
            .let { ParticipateResponse.from(it) }
    }

    @Transactional
    override fun editParticipateAnswer(eventId: Long, participateEditRequest: ParticipateEditRequest,
        username: String): ParticipateResponse {
        val participateHistory = getExistParticipateHistory(eventId, username)
        participateHistory.answer = participateEditRequest.answer
        return participateHistoryRepository.save(participateHistory).let {
            ParticipateResponse.from(it)
        }
    }

    override fun setParticipantsStatusWait(eventId: Long?): List<ParticipateResponse> {
        val list = participateHistoryRepository.getParticipantsByEvent(eventId)
        list.forEach { it.status = EventParticipateStatus.WAIT_RESULT }
        list.forEach { participateHistoryRepository.save(it) }
        return list.map { ParticipateResponse.from(it) }
    }

    @Transactional
    override fun resignEventParticipate(eventId: Long, username: String) {
        val participateHistory = getExistParticipateHistory(eventId, username)
        participateHistoryRepository.delete(participateHistory)
    }
    @Transactional
    override fun setEventResult(eventId: Long, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse> {
        val participantList = participateHistoryRepository.getParticipantsByEvent(eventId)
        val list = winPickWinnerStrategy.pick(participantList, eventWinnerRequest.eventWinners)
            .map { participateHistoryRepository.save(it) }
        return list.map { ParticipateResponse.from(it) }
    }

    @Transactional
    override fun getParticipateHistoryByMember(pageRequest: PageRequest, username: String): PageResponse<ParticipateResponse> {
        val pages = participateHistoryRepository.getParticipantsByMember(pageRequest.getPageable(), username)
        val content = pages.content.map { ParticipateResponse.from(it) }
        return PageResponse.of(pageRequest, content, pages.totalElements.toInt())
    }

    override fun getParticipateHistoryByEvent(eventId: Long?): List<ParticipateResponse> {
        return participateHistoryRepository.getParticipantsByEvent(eventId)
            .map { ParticipateResponse.from(it) }
    }
}