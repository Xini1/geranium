package by.geranium.core;

import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public interface MethodCall {

    Class<?> getTargetClass();

    String getMethodName();

    Class<?> getReturnType();

    LoggingLevel getInLoggingLevel();

    LoggingLevel getOutLoggingLevel();

    LoggingLevel getExceptionLoggingLevel();

    List<MethodArgument> getMethodArguments();

    Object proceed() throws Throwable;
}
