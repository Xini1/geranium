package by.geranium.adapter;

import by.geranium.core.Geranium;
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

    private final Geranium geranium;

    @Pointcut("@within(by.geranium.annotation.Log) || @annotation(by.geranium.annotation.Log)")
    void classesAndMethodsWithLogAnnotation() {
        //pointcut
    }

    @Pointcut("@within(by.geranium.annotation.Log.In) || @annotation(by.geranium.annotation.Log.In)")
    void classesAndMethodsWithLogInAnnotation() {
        //pointcut
    }

    @Pointcut("@within(by.geranium.annotation.Log.Out) || @annotation(by.geranium.annotation.Log.Out)")
    void classesAndMethodsWithLogOutAnnotation() {
        //pointcut
    }

    @Pointcut("@within(by.geranium.annotation.Log.Error) || @annotation(by.geranium.annotation.Log.Error)")
    void classesAndMethodsWithLogErrorAnnotation() {
        //pointcut
    }

    @Around("classesAndMethodsWithLogAnnotation() || " +
            "classesAndMethodsWithLogInAnnotation() ||" +
            "classesAndMethodsWithLogOutAnnotation() ||" +
            "classesAndMethodsWithLogErrorAnnotation()")
    public Object handleMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        return geranium.logMethodCall(ProceedingJoinPointMethodCallAdapter.from(joinPoint));
    }
}
