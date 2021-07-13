package by.geranium.adapter;

import by.geranium.annotation.Log;
import by.geranium.core.Advice;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Maxim Tereshchenko
 */
@Aspect
@RequiredArgsConstructor
public class SpringAspectAdapter {

    private final Advice geraniumAdvice;

    @Pointcut("@within(logAnnotation)")
    void classesWithLogAnnotation(Log logAnnotation) {
        //pointcut
    }

    @Pointcut("@annotation(logAnnotation)")
    void methodsWithLogAnnotation(Log logAnnotation) {
        //pointcut
    }

    @Around("methodsWithLogAnnotation(logAnnotation) || classesWithLogAnnotation(logAnnotation)")
    public Object handleMethodInvocation(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        return geraniumAdvice.logMethodCall(ProceedingJoinPointMethodCallAdapter.from(joinPoint));
    }
}
