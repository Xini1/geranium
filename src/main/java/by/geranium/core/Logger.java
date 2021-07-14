package by.geranium.core;

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
class Logger {

    private final LoggingStrategyFactory loggingStrategyFactory;

    void logIn(MethodCall methodCall) {
        getStrategy(methodCall).log(() -> getLogInMessage(methodCall));
    }

    void logOut(MethodCall methodCall, Object returnValue) {
        getStrategy(methodCall).log(() -> getLogOutMessage(methodCall, returnValue));
    }

    void logThrowable(MethodCall methodCall, Throwable throwable) {
        getStrategyForException(methodCall).log(() -> getLogExceptionMessage(methodCall, throwable), throwable);
    }

    private LoggingStrategy getStrategy(MethodCall methodCall) {
        return loggingStrategyFactory.getStrategy(methodCall.getLoggingLevel(), methodCall.getDeclaringClass());
    }

    private LoggingStrategy getStrategyForException(MethodCall methodCall) {
        return loggingStrategyFactory.getStrategy(
                methodCall.getExceptionLoggingLevel(),
                methodCall.getDeclaringClass()
        );
    }

    private String getLogInMessage(MethodCall methodCall) {
        return String.format(
                "%s > %s",
                methodCall.getMethodName(),
                methodCall.getMethodArguments()
                        .stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", "))
        );
    }

    private String getLogOutMessage(MethodCall methodCall, Object returnValue) {
        return String.format("%s < %s", methodCall.getMethodName(), methodCall.hasReturnValue() ? returnValue : "");
    }

    private String getLogExceptionMessage(MethodCall methodCall, Throwable throwable) {
        return String.format("%s ! %s", methodCall.getMethodName(), throwable.getClass().getName());
    }
}
