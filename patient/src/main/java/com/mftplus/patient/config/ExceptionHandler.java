package com.mftplus.patient.config;

import com.mftplus.patient.exception.ExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionHandler {

//    @Around("execution(* com.mftplus.patient.controller..*.*(..))")
//    public ResponseEntity around(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            System.out.println("Starting run method "+joinPoint.getSignature().getName());
//            System.out.println("Parameter: "+joinPoint.getArgs()[0]);
//
//            Object result = joinPoint.proceed();   // اجرای متد
//            System.out.println("Info : " + result);
////            log.info("Info : " + joinPoint.getSignature().getName());
//
//            System.out.println("Starting executed "+joinPoint.getSignature().getName());
//            System.out.println("Output : "+result);
//            return ResponseEntity.ok(result);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
////            log.error(e.getMessage());
//            return ResponseEntity.status(500).body(ExceptionWrapper.getMessage(e));
//        }
//    }


//    @After("execution(* com.mftplus.appointment..*.*(..))")
//    public void drawLine() {
//        System.out.println("--------------------------------------------");
//    }
}
