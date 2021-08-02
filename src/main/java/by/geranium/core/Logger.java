package by.geranium.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Maxim Tereshchenko
 */
public class Logger {

    private final LoggingStrategyFactory loggingStrategyFactory;
    private final List<ValueSerializingStrategy> valueSerializingStrategyList;
    private final Template inLoggingTemplate;
    private final Template outLoggingTemplate;
    private final Template throwableLoggingTemplate;

    public Logger(
            LoggingStrategyFactory loggingStrategyFactory,
            List<ValueSerializingStrategy> valueSerializingStrategyList,
            Template inLoggingTemplate,
            Template outLoggingTemplate,
            Template throwableLoggingTemplate
    ) {
        this.loggingStrategyFactory = loggingStrategyFactory;
        this.valueSerializingStrategyList = new ArrayList<>(valueSerializingStrategyList);
        this.inLoggingTemplate = inLoggingTemplate;
        this.outLoggingTemplate = outLoggingTemplate;
        this.throwableLoggingTemplate = throwableLoggingTemplate;
    }

    void logIn(MethodCall methodCall) {
        loggingStrategyFactory.getStrategy(methodCall.inLoggingLevel(), methodCall.targetClass())
                .log(() -> logInMessage(methodCall));
    }

    void logOut(MethodCall methodCall, Object returnValue) {
        loggingStrategyFactory.getStrategy(methodCall.outLoggingLevel(), methodCall.targetClass())
                .log(() -> logOutMessage(methodCall, returnValue));
    }

    void logThrowable(MethodCall methodCall, Throwable throwable) {
        loggingStrategyFactory.getStrategy(
                        methodCall.throwableLoggingLevel(),
                        methodCall.targetClass()
                )
                .log(
                        () -> logThrowableMessage(methodCall, throwable),
                        Optional.ofNullable(throwable)
                                .filter(unused -> methodCall.isThrowableIncluded())
                                .orElse(null)
                );
    }

    private String logInMessage(MethodCall methodCall) {
        return inLoggingTemplate.replace(
                Map.of(
                        SupportedPlaceholders.METHOD_NAME, methodCall.methodName(),
                        SupportedPlaceholders.ARGUMENTS, serializedMethodArgumentsString(methodCall)
                )
        );
    }

    private String serializedMethodArgumentsString(MethodCall methodCall) {
        return methodCall.methodArguments()
                .stream()
                .map(
                        methodArgument -> valueSerializingStrategyList.stream()
                                .filter(methodArgument::isSupported)
                                .map(methodArgument::toString)
                                .findFirst()
                                .orElseThrow(
                                        () -> new IllegalArgumentException(
                                                "Could not find any suitable value serializing strategy for argument " +
                                                        methodArgument.name()
                                        )
                                )
                )
                .collect(Collectors.joining(", "));
    }

    private String logOutMessage(MethodCall methodCall, Object returnValue) {
        return outLoggingTemplate.replace(
                Map.of(
                        SupportedPlaceholders.METHOD_NAME, methodCall.methodName(),
                        SupportedPlaceholders.RETURN_VALUE, serializedReturnValueString(methodCall, returnValue)
                )
        );
    }

    private String serializedReturnValueString(MethodCall methodCall, Object returnValue) {
        return valueSerializingStrategyList.stream()
                .filter(valueSerializer -> valueSerializer.isSupported(methodCall.returnType()))
                .map(valueSerializer -> valueSerializer.serialize(returnValue))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                String.format(
                                        "Could not find any suitable value serializing strategy for return value " +
                                                "of %s.%s",
                                        methodCall.targetClass().getName(),
                                        methodCall.methodName()
                                )
                        )
                );
    }

    private String logThrowableMessage(MethodCall methodCall, Throwable throwable) {
        return throwableLoggingTemplate.replace(
                Map.of(
                        SupportedPlaceholders.METHOD_NAME, methodCall.methodName(),
                        SupportedPlaceholders.THROWABLE_CLASS, throwable.getClass().getName(),
                        SupportedPlaceholders.THROWABLE_MESSAGE, throwable.getMessage()
                )
        );
    }
}
