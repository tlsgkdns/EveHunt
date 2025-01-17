package com.evehunt.evehunt

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
class EveHuntApplication
fun main(args: Array<String>) {
    runApplication<EveHuntApplication>(*args)
}
@PostConstruct
fun started() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}
