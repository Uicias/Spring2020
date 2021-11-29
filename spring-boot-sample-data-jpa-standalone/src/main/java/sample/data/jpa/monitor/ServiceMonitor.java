package sample.data.jpa.monitor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceMonitor {
    /**
     * Creates an appropriate logger depending on the joinPoint
     *
     * @param joinPoint the joinPoint for which we need a logger
     * @return the logger
     */
    private Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     *
     */
    @Pointcut("within(sample.data.jpa.controler..*)")
    public void applicationPointCut() {
        // Method is empty as this is just a Pointcut, the implementations are in the method annotated @Around.
    }

    /**
     * Log when we enter and exit a method
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("applicationPointCut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = getLogger(joinPoint);
        if (log.isDebugEnabled())
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled())
                log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
