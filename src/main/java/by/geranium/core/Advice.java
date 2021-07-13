package by.geranium.core;

import lombok.RequiredArgsConstructor;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class Advice {

    private final Logger logger;

    public Object logMethodCall(MethodCall methodCall) throws Throwable {
        logger.logIn(methodCall);
        Object returnValue;
        try {
            returnValue = methodCall.proceed();
        } catch (Throwable throwable) {
            logger.logThrowable(methodCall, throwable);
            throw throwable;
        }
        logger.logOut(methodCall, returnValue);
        return returnValue;
    }
}
