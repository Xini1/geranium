package by.geranium.core;

import by.geranium.annotation.Log;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public abstract class AbstractMethodCall implements MethodCall {

    private final Method method;

    @Override
    public String getMethodName() {
        return method.getName();
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public LoggingLevel getLoggingLevel() {
        return findAnnotation(Log.class)
                .map(Log::value)
                .orElse(LoggingLevel.OFF);
    }

    @Override
    public LoggingLevel getExceptionLoggingLevel() {
        return findAnnotation(Log.Error.class)
                .map(Log.Error::value)
                .orElse(LoggingLevel.OFF);
    }

    @Override
    public List<MethodArgument> getMethodArguments() {
        return IntStream.range(0, method.getParameterCount())
                .filter(index -> isArgumentEligibleForLogging(method.getParameters()[index]))
                .mapToObj(index -> new MethodArgument(method.getParameters()[index], getArguments()[index]))
                .collect(Collectors.toList());
    }

    protected Method getInvokedMethod() {
        return method;
    }

    protected abstract Object[] getArguments();

    private boolean isArgumentEligibleForLogging(Parameter parameter) {
        return Optional.ofNullable(parameter.getAnnotation(Log.Exclude.class))
                .isEmpty();
    }

    private <T extends Annotation> Optional<T> findAnnotation(Class<T> annotationClass) {
        return Optional.ofNullable(
                Optional.ofNullable(method.getAnnotation(annotationClass))
                        .orElseGet(() -> getDeclaringClass().getAnnotation(annotationClass))
        );
    }
}
