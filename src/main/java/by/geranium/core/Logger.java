package by.geranium.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maxim Tereshchenko
 */
public class Logger {

    private final LoggingStrategyFactory loggingStrategyFactory;
    private final List<ValueSerializingStrategy> valueSerializingStrategyList;

    public Logger(
            LoggingStrategyFactory loggingStrategyFactory,
            List<ValueSerializingStrategy> valueSerializingStrategyList
    ) {
        this.loggingStrategyFactory = loggingStrategyFactory;
        this.valueSerializingStrategyList = new ArrayList<>(valueSerializingStrategyList);
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
                methodCall.exceptionLoggingLevel(),
                methodCall.targetClass()
        )
                .log(() -> logExceptionMessage(methodCall, throwable), throwable);
    }

    private String logInMessage(MethodCall methodCall) {
        return String.format(
                "%s > %s",
                methodCall.methodName(),
                methodCall.methodArguments()
                        .stream()
                        .map(methodArgument -> valueSerializingStrategyList.stream()
                                .filter(methodArgument::isSupported)
                                .map(methodArgument::toString)
                                .findFirst()
                                .orElseThrow(IllegalArgumentException::new))
                        .collect(Collectors.joining(", "))
        );
    }

    private String logOutMessage(MethodCall methodCall, Object returnValue) {
        return String.format(
                "%s < %s",
                methodCall.methodName(),
                valueSerializingStrategyList.stream()
                        .filter(valueSerializer -> valueSerializer.isSupported(methodCall.returnType()))
                        .map(valueSerializer -> valueSerializer.serialize(returnValue))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

    private String logExceptionMessage(MethodCall methodCall, Throwable throwable) {
        return String.format("%s ! %s", methodCall.methodName(), throwable.getClass().getName());
    }
}
