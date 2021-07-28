package by.geranium.core;

import by.geranium.annotation.Log;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public abstract class AbstractMethodCall implements MethodCall {

    private static final Map<Class<? extends Annotation>, Function<? super Annotation, LoggingLevel>>
            LOGGING_LEVEL_EXTRACTION_BY_ANNOTATION_TYPE_MAP = Map.of(
            Log.class, annotation -> ((Log) annotation).value(),
            Log.In.class, annotation -> ((Log.In) annotation).value(),
            Log.Out.class, annotation -> ((Log.Out) annotation).value(),
            Log.Error.class, annotation -> ((Log.Error) annotation).value()
    );

    private final Method method;

    @Override
    public String methodName() {
        return method.getName();
    }

    @Override
    public Class<?> returnType() {
        return method.getReturnType();
    }

    @Override
    public LoggingLevel inLoggingLevel() {
        return findLoggingLevelFromAnnotationsPrioritizing(Log.In.class, Log.class);
    }

    @Override
    public LoggingLevel outLoggingLevel() {
        return findLoggingLevelFromAnnotationsPrioritizing(Log.Out.class, Log.class);
    }

    @Override
    public LoggingLevel exceptionLoggingLevel() {
        return findLoggingLevelFromAnnotationsPrioritizing(Log.Error.class);
    }

    @Override
    public List<MethodArgument> methodArguments() {
        return IntStream.range(0, method.getParameterCount())
                .filter(index -> isArgumentEligibleForLogging(method.getParameters()[index]))
                .mapToObj(index -> new MethodArgument(method.getParameters()[index], arguments()[index]))
                .collect(Collectors.toList());
    }

    protected Method invokedMethod() {
        return method;
    }

    protected abstract Object[] arguments();

    private boolean isArgumentEligibleForLogging(Parameter parameter) {
        return parameter.getAnnotation(Log.Exclude.class) == null;
    }

    @SafeVarargs
    private LoggingLevel findLoggingLevelFromAnnotationsPrioritizing(Class<? extends Annotation>... annotationClasses) {
        return findAnnotationPrioritizing(annotationClasses)
                .map(
                        annotation -> LOGGING_LEVEL_EXTRACTION_BY_ANNOTATION_TYPE_MAP.get(annotation.annotationType())
                                .apply(annotation)
                )
                .orElse(LoggingLevel.OFF);
    }

    @SafeVarargs
    private Optional<? extends Annotation> findAnnotationPrioritizing(
            Class<? extends Annotation>... annotationClasses
    ) {
        return Optional.ofNullable(
                findAnnotationPrioritizing(targetClass(), Arrays.asList(annotationClasses), method)
        );
    }

    private Annotation findAnnotationPrioritizing(
            Class<?> type,
            List<Class<? extends Annotation>> annotationClassList,
            Method... methods
    ) {
        if (type == Object.class) {
            return null;
        }

        return Arrays.stream(methods)
                .filter(declaredMethod -> declaredMethod.getName().equals(method.getName()))
                .filter(
                        declaredMethod -> Arrays.equals(
                                declaredMethod.getParameterTypes(),
                                method.getParameterTypes()
                        )
                )
                .map(declaredMethod -> findAnnotationPrioritizing(declaredMethod, annotationClassList))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(
                        () -> Optional.ofNullable(findAnnotationPrioritizing(type, annotationClassList))
                                .orElseGet(
                                        () -> findAnnotationPrioritizing(
                                                type.getSuperclass(),
                                                annotationClassList,
                                                type.getSuperclass().getDeclaredMethods()
                                        )
                                )
                );
    }

    private Annotation findAnnotationPrioritizing(
            AnnotatedElement annotatedElement,
            List<Class<? extends Annotation>> annotationClassList
    ) {
        return annotationClassList.stream()
                .map(annotatedElement::getAnnotation)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
