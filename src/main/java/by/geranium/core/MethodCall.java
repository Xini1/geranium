package by.geranium.core;

import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public interface MethodCall {

    Class<?> getDeclaringClass();

    String getMethodName();

    Class<?> getReturnType();

    LoggingLevel getLoggingLevel();

    LoggingLevel getExceptionLoggingLevel();

    List<MethodArgument> getMethodArguments();

    Object proceed() throws Throwable;
}
