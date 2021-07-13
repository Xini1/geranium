package by.geranium.core;

import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public interface MethodCall {

    Class<?> getDeclaringClass();

    String getMethodName();

    boolean hasReturnValue();

    LoggingLevel getLoggingLevel();

    LoggingLevel getExceptionLoggingLevel();

    List<MethodArgument> getMethodArguments();

    Object proceed() throws Throwable;
}
