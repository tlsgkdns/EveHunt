package com.evehunt.evehunt.domain.participant.service

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.member.repository.MemberRepository
import com.evehunt.evehunt.domain.participant.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateEditRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participant.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participant.model.Participant
import com.evehunt.evehunt.domain.participant.model.ParticipantStatus
import com.evehunt.evehunt.domain.participant.model.strategy.PickWinnerStrategy
import com.evehunt.evehunt.domain.participant.model.strategy.PickWinnerTwoPointer
import com.evehunt.evehunt.domain.participant.repository.ParticipantRepository
import com.evehunt.evehunt.global.common.RedisLockService
import com.evehunt.evehunt.global.common.page.PageRequest
import com.evehunt.evehunt.global.common.page.PageResponse
import com.evehunt.evehunt.global.exception.exception.AlreadyExistModelException
import com.evehunt.evehunt.global.exception.exception.FullCapacityException
import com.evehunt.evehunt.global.exception.exception.InvalidEventException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import com.evehunt.evehunt.global.infra.aop.annotation.StopWatch
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParticipantServiceImpl(
    private val participantRepository: ParticipantRepository,
    private val eventRepository: EventRepository,
    private val memberRepository: MemberRepository,
    private val redisLockService: RedisLockService
): ParticipantService {
    val winPickWinnerStrategy: PickWinnerStrategy = PickWinnerTwoPointer()
    fun getExistEvent(eventId: Long?): Event
    {
        return eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId.toString())
    }
    fun getExistParticipateHistory(eventId: Long?, memberEmail: String): Participant
    {
        return participantRepository.getParticipant(eventId, memberEmail)
            ?: throw ModelNotFoundException("Participate", memberEmail)
    }
    @Transactional
    override fun participateEvent(eventId: Long?, username: String, participateRequest: ParticipateRequest): ParticipateResponse {
        lateinit var participateHistoryResponse: ParticipateResponse
        redisLockService.tryLockWith("Participant $eventId")
        {
            if(participantRepository.getParticipant(eventId, username) != null)
                throw AlreadyExistModelException(username)
            val event = getExistEvent(eventId)
            val member = memberRepository.findMemberByEmail(username)
            if(event.eventStatus != EventStatus.PROCEED) throw InvalidEventException(eventId)
            if(participantRepository.getParticipantsByEvent(eventId).size + 1 > event.capacity)
                throw FullCapacityException("Event", eventId.toString(), event.capacity)
            val participateHistory = participateRequest.to(event, member)
            participateHistoryResponse = participantRepository.save(participateHistory)
                .let { ParticipateResponse.from(it) }
        }
        return participateHistoryResponse
    }

    @Transactional
    override fun getParticipant(eventId: Long?, username: String): ParticipateResponse {
        return getExistParticipateHistory(eventId, username)
            .let { ParticipateResponse.from(it) }
    }

    @Transactional
    override fun editParticipateAnswer(eventId: Long?, participateEditRequest: ParticipateEditRequest,
        username: String): ParticipateResponse {
        val participateHistory = getExistParticipateHistory(eventId, username)
        participateHistory.answer = participateEditRequest.answer
        return participantRepository.save(participateHistory).let {
            ParticipateResponse.from(it)
        }
    }

    @Transactional
    override fun setParticipantsStatusWait(eventId: Long?): List<ParticipateResponse> {
        val list = participantRepository.getParticipantsByEvent(eventId)
        list.forEach { it.status = ParticipantStatus.WAIT_RESULT }
        list.forEach { participantRepository.save(it) }
        return list.map { ParticipateResponse.from(it) }
    }

    override fun getParticipantCount(eventId: Long?): Int {
        return participantRepository.getParticipantsByEvent(eventId).size
    }

    @Transactional
    override fun resignEventParticipate(eventId: Long?, username: String) {
        val participateHistory = getExistParticipateHistory(eventId, username)
        participantRepository.delete(participateHistory)
    }
    @StopWatch
    @Transactional
    override fun setEventResult(eventId: Long?, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse> {
        val participantList = participantRepository.getParticipantsByEvent(eventId)
        val list = winPickWinnerStrategy.pick(participantList, eventWinnerRequest.eventWinners)
            .map { participantRepository.save(it) }
        return list.map { ParticipateResponse.from(it) }
    }

    @Transactional
    override fun getParticipateHistoryByMember(username: String, pageRequest: PageRequest): PageResponse<ParticipateResponse> {
        val pages = participantRepository.getParticipantsByMember(pageRequest.getPageable(), username)
        val content = pages.content.map { ParticipateResponse.from(it) }
        return PageResponse.of(pageRequest, content, pages.totalElements.toInt())
    }

    override fun getParticipantsByEvent(eventId: Long?): List<ParticipateResponse> {
        return participantRepository.getParticipantsByEvent(eventId).map {
            ParticipateResponse.from(it)
        }

    }
}