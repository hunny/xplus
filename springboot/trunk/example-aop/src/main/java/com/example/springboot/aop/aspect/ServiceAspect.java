package com.example.springboot.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAspect {

  @AfterReturning("execution(* com.example.springboot.aop..*Service.*(..))")
  public void logServiceAccess(JoinPoint joinPoint) {
    System.out.println("Completed: " + joinPoint);
  }

}
