package com.evehunt.evehunt.domain.event.repository


import com.evehunt.evehunt.domain.event.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long>, QueryDslEventRepository{

}