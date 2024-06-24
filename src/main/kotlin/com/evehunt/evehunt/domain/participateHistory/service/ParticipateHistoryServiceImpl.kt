package com.evehunt.evehunt.domain.participateHistory.service

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.repository.EventRepository
import com.evehunt.evehunt.domain.participateHistory.dto.EventWinnerRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateRequest
import com.evehunt.evehunt.domain.participateHistory.dto.ParticipateResponse
import com.evehunt.evehunt.domain.participateHistory.model.ParticipateHistory
import com.evehunt.evehunt.domain.participateHistory.model.strategy.PickWinnerBruteforce
import com.evehunt.evehunt.domain.participateHistory.model.strategy.PickWinnerStrategy
import com.evehunt.evehunt.domain.participateHistory.repository.ParticipateHistoryRepository
import com.evehunt.evehunt.global.common.PageRequest
import com.evehunt.evehunt.global.common.PageResponse
import com.evehunt.evehunt.global.exception.exception.AlreadyExistModelException
import com.evehunt.evehunt.global.exception.exception.FullCapacityException
import com.evehunt.evehunt.global.exception.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParticipateHistoryServiceImpl(
    private val participateHistoryRepository: ParticipateHistoryRepository,
    private val eventRepository: EventRepository
): ParticipateHistoryService {
    val winPickWinnerStrategy: PickWinnerStrategy = PickWinnerBruteforce()
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
    override fun participateEvent(eventId: Long, memberEmail: String, participateRequest: ParticipateRequest): ParticipateResponse {
        lateinit var participateHistoryResponse: ParticipateResponse
        if(participateHistoryRepository.getParticipateHistory(eventId, memberEmail) != null)
            throw AlreadyExistModelException(eventId.toString())
        val event = getExistEvent(eventId)
        try {
            participateHistoryRepository.getLock("Lock $eventId", 3000)
            if(getParticipateHistoryByEvent(eventId).size + 1 > event.capacity)
                throw FullCapacityException("Event", eventId.toString(), event.capacity)
            val participateHistory = participateRequest.to(event)
            participateHistoryResponse = participateHistoryRepository.save(participateHistory)
                .let { ParticipateResponse.from(it) }
        } finally {
            participateHistoryRepository.releaseLock("Lock $eventId")
        }
        return participateHistoryResponse
    }

    @Transactional
    override fun dropEventParticipate(eventId: Long, memberEmail: String) {
        val participateHistory = getExistParticipateHistory(eventId, memberEmail)
        participateHistoryRepository.delete(participateHistory)
    }

    @Transactional
    override fun setEventResult(eventId: Long, eventWinnerRequest: EventWinnerRequest): List<ParticipateResponse> {
        val participantList = participateHistoryRepository.getParticipantsByEvent(eventId)
        val list = winPickWinnerStrategy.pick(participantList, eventWinnerRequest.eventWinners).map { participateHistoryRepository.save(it) }
        return list.map { ParticipateResponse.from(it) }
    }

    @Transactional
    override fun getParticipateHistoryByMember(pageRequest: PageRequest, memberEmail: String): PageResponse<ParticipateResponse> {
        val pages = participateHistoryRepository.getParticipantsByMember(pageRequest.getPageable(), memberEmail)
        val content = pages.content.map { ParticipateResponse.from(it) }
        return PageResponse.of(pageRequest, content, pages.totalElements.toInt())
    }

    override fun getParticipateHistoryByEvent(eventId: Long): List<ParticipateResponse> {
        return participateHistoryRepository.getParticipantsByEvent(eventId).map { ParticipateResponse.from(it) }
    }
}