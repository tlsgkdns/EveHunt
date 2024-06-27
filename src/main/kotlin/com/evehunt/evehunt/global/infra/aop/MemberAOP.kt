package com.evehunt.evehunt.global.infra.aop

import com.evehunt.evehunt.domain.event.service.EventService
import com.evehunt.evehunt.domain.member.service.MemberService
import com.evehunt.evehunt.global.exception.exception.UnAuthorizedAccessException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Aspect
@Component
class MemberAOP(
    private val eventService: EventService,
    private val memberService: MemberService,
) {
    @Before("@annotation(com.evehunt.evehunt.global.infra.aop.annotation.CheckEventLoginMember)")
    fun checkEventMember(joinPoint: JoinPoint)
    {
        val method = (joinPoint.signature as MethodSignature).method
        val username = (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
        val parameterValues = joinPoint.args
        val parameters = method.parameters
        for(i in parameters.indices)
        {
            if(parameters[i].name.equals("eventId"))
            {
                val event = eventService.getEvent(parameterValues[i] as Long)
                val targetUsername = memberService.getMember(event.hostId!!).email
                if(targetUsername != username)
                {
                    throw UnAuthorizedAccessException(username)
                }
            }
        }
    }
}