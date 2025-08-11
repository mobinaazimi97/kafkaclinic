package com.mftplus.patient.config;

//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.dao.PermissionDeniedDataAccessException;
//import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Loggable {
//    @Before("execution(* com.mftplus.patient.controller..*.*(..))")
//    public void logBefore(JoinPoint joinPoint) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        String username = authentication.getName();
//        authentication.getAuthorities();
//        if (! user.role.permissions.contains("Save")){
//            throw new PermissionDeniedDataAccessException();
//        }
//    }

    @Around("execution(* com.mftplus.patient.controller..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info();
        System.out.println("+ Run : " + joinPoint.getSignature().getName());
        log.debug("+ Run {}" , joinPoint.getSignature().getName());
        System.out.println("\t" + "Arguments");
        for (Object arg : joinPoint.getArgs()) {
            System.out.println("\t" + arg);
        }

        Object result = joinPoint.proceed();
        System.out.println("- Done " + joinPoint.getSignature().getName());
        return result;
    }

    @After("execution(* com.mftplus.patient.controller..*.*(..))")
    public void drawLine() {
        System.out.println("--------------------------------------------");
    }
}
