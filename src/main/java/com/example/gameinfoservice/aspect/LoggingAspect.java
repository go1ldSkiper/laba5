package com.example.gameinfoservice.aspect;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** The type Logging aspect. */
@Aspect
@Component
public class LoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

  /** Init aspect. */
  @PostConstruct
  public void initAspect() {
    LOGGER.info("Aspect is initialized");
  }

  /** Destroy aspect. */
  @PreDestroy
  public void destroyAspect() {
    LOGGER.info("Aspect is destroyed");
  }

  /** Call at my service annotation. */
  @Pointcut("@annotation(AspectAnnotation)")
  public void callAtMyServiceAnnotation() {}

  /** Error. */
  @Pointcut("@annotation(com.example.gameinfoservice.aspect.ExceptionLoggerAnnotation)")
  public void error() {}

  /**
   * Log after.
   *
   * @param joinPoint the join point
   * @param result the result
   */
  @AfterReturning(value = "callAtMyServiceAnnotation()", returning = "result")
  public void logAfter(final JoinPoint joinPoint, final Object result) {
    String methodName = joinPoint.toString();
    LOGGER.info("<< {}() - {}", methodName, result);
  }

  /**
   * Log exception.
   *
   * @param joinPoint the join point
   * @param exception the exception
   */
  @AfterThrowing(pointcut = "callAtMyServiceAnnotation()", throwing = "exception")
  public void logException(final JoinPoint joinPoint, final Throwable exception) {
    String methodName = joinPoint.toString();
    LOGGER.error("<< {}() - {}", methodName, exception.getMessage());
  }

  /**
   * Log exception.
   *
   * @param joinPoint the join point
   */
  @Before(value = "error()")
  public void logException(final JoinPoint joinPoint) {
    String methodName = joinPoint.toString();
    LOGGER.error("<< {}() - ", methodName);
  }

  /**
   * Log before.
   *
   * @param joinPoint the join point
   */
  @Before(value = "callAtMyServiceAnnotation()")
  public void logBefore(final JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    String methodName = joinPoint + " " + joinPoint.getSignature().getName();
    LOGGER.info(">> {}() - {}\n", methodName, Arrays.toString(args));
  }
}
