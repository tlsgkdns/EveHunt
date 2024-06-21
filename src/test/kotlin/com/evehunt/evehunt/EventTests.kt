package com.evehunt.evehunt

import com.evehunt.evehunt.domain.event.service.EventService
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EventTests @Autowired constructor(
    eventService: EventService
) {
    companion object
    {
        @JvmStatic
        @BeforeAll
        fun registerMember()
        {

        }
    }
}