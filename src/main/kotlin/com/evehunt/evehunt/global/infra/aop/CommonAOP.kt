package com.evehunt.evehunt.global.infra.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class CommonAOP {
    @Around("@annotation(com.evehunt.evehunt.global.infra.aop.annotation.StopWatch)")
    fun measureTime(proceedingJoinPoint: ProceedingJoinPoint): Any
    {
        val stopWatch = StopWatch()
        stopWatch.start()
        val ret = proceedingJoinPoint.proceed()
        stopWatch.stop()
        println(proceedingJoinPoint.signature.name + ": " + stopWatch.totalTimeSeconds)
        return ret
    }
}