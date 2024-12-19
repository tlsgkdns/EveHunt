package com.evehunt.evehunt.domain.event.dto

import com.evehunt.evehunt.domain.event.model.Event
import com.evehunt.evehunt.domain.event.model.EventStatus
import com.evehunt.evehunt.domain.image.model.Image
import com.evehunt.evehunt.domain.member.model.Member
import com.evehunt.evehunt.domain.tag.dto.TagAddRequest
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.time.ZoneId

data class EventHostRequest(
    @field:Length(min = 3, max = 20, message = "제목의 길이는 3자 이상, 20자 미만으로 설정해주세요")
    @field:NotNull(message = "제목을 입력해주세요.")
    val title: String,
    @field:Length(min = 3, max = 1000, message = "설명은 3자 이상 1000자 미만으로 설정해주세요.")
    @field:NotNull(message = "설명을 입력해주세요.")
    val description: String,
    @field:Length(min = 3, max = 1000, message = "당첨 메시지는 3자 이상 1000자 미만으로 설정해주세요.")
    @field:NotNull(message = "당첨 메시지를 입력해주세요.")
    val winMessage: String,
    val eventImage: String?,
    @field:Min(1, message = "최소 한명은 참여해주세여.")
    @field:Max(1000, message = "참여 인원은 1000명을 넘길 수 없습니다.")
    @field:NotNull(message = "정원을 입력해주세요")
    val capacity: Int,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @field:Future(message = "종료 날짜는 미래 날짜로 설정해주세요.")
    @field:NotNull(message = "종료 날짜를 입력해주세요.")
    val closeAt: LocalDateTime,
    val question: String?,
    val tagAddRequests: List<TagAddRequest>?
)
{
    fun to(member: Member?): Event
    {
        return Event(
            title = title,
            description = description,
            winMessage = winMessage,
            capacity = capacity,
            eventStatus = EventStatus.PROCEED,
            closeAt = closeAt.atZone(ZoneId.of("Asia/Seoul")),
            image = Image.from(eventImage),
            question = question,
            host = member
        )
    }
}
