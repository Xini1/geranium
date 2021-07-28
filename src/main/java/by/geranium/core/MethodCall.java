package by.geranium.core;

import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public interface MethodCall {

    Class<?> targetClass();

    String methodName();

    Class<?> returnType();

    LoggingLevel inLoggingLevel();

    LoggingLevel outLoggingLevel();

    LoggingLevel exceptionLoggingLevel();

    List<MethodArgument> methodArguments();

    Object proceed() throws Throwable;
}
